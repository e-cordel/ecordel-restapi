/*
 * Copyright 2026 Projeto e-cordel (http://ecordel.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package br.com.itsmemario.ecordel.review;

import br.com.itsmemario.ecordel.cordel.Cordel;
import br.com.itsmemario.ecordel.cordel.CordelNotFoundException;
import br.com.itsmemario.ecordel.cordel.CordelService;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AiReviewService {

  private static final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory();
  public static final String REVIEW_CACHE_NAME = "reviews";

  private final ChatClient.Builder chatClientBuilder;
  private final CordelService cordelService;
  private final Cache cache;

  public AiReviewService(ChatClient.Builder chatClientBuilder, CordelService cordelService, CacheManager cacheManager) {
    this.chatClientBuilder = chatClientBuilder;
    this.cordelService = cordelService;
    this.cache = cacheManager.getCache(REVIEW_CACHE_NAME);
  }

  @Value("${app.ai.review.systemPrompt}")
  private String systemPrompt;

  @Value("${app.ai.review.userPrompt}")
  private String userPrompt;

  @Value("${spring.ai.openai.chat.options.model:unknown}")
  private String model;

  public Optional<AiReviewResponse> getReview(String reviewId) {
    return Optional.ofNullable(cache.get(reviewId, AiReviewResponse.class));
  }

  @Async
  public void review(Long cordelId, String reviewId) {
    log.info("starting AI review for cordel id {} with model {}", cordelId, model);

    var cordel = cordelService.findById(cordelId).orElseThrow(CordelNotFoundException::new);
    var finalUserPrompt = createUserPrompt(cordel);

    log.info("sending AI review request for cordel id {} with prompt length {}", cordelId, finalUserPrompt.length());
    cache.put(reviewId, new AiReviewResponse(false, null));

    CompletableFuture.supplyAsync(() ->
      chatClientBuilder.build()
          .prompt()
          .system(systemPrompt)
          .user(finalUserPrompt)
          .call()
          .content()
    ).thenAccept(text -> {
      log.info("AI review result received for cordel id {}", cordelId);
      cache.put(reviewId, new AiReviewResponse(true, text));
    });
  }

  private String createUserPrompt(Cordel cordel) {
    Mustache mustache = MUSTACHE_FACTORY.compile(new StringReader(userPrompt), "ai-review-user-prompt");
    StringWriter writer = new StringWriter();
    mustache.execute(writer, buildContext(cordel));
    String prompt = writer.toString();
    log.trace("generated prompt for cordel id {}: {}", cordel.getId(), prompt);
    return prompt;
  }

  private Map<String, Object> buildContext(Cordel cordel) {
    Map<String, Object> context = new LinkedHashMap<>();
    putIfPresent(context, "title", cordel.getTitle());
    if (cordel.getAuthor() != null) {
      Map<String, Object> author = new LinkedHashMap<>();
      putIfPresent(author, "name", cordel.getAuthor().getName());
      if (!author.isEmpty()) {
        context.put("author", author);
      }
    }
    if (cordel.getYear() != null) {
      context.put("year", cordel.getYear());
    }
    putIfPresent(context, "source", cordel.getSource());
    putIfPresent(context, "content", cordel.getContent());

    log.trace("built context for cordel id {}: {}", cordel.getId(), context);

    return context;
  }

  private static void putIfPresent(Map<String, Object> context, String key, String value) {
    if (value != null && !value.isBlank()) {
      context.put(key, value);
    }
  }

}
