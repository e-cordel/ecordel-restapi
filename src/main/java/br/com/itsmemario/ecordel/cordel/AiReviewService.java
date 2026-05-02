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
package br.com.itsmemario.ecordel.cordel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiReviewService {

  private final ChatClient.Builder chatClientBuilder;
  private final CordelService cordelService;

  @Value("${app.ai.review.prompt}")
  private String reviewPrompt;

  public String review(Long cordelId) {
    Cordel cordel = cordelService.findById(cordelId).orElseThrow(CordelNotFoundException::new);

    return chatClientBuilder.build()
        .prompt()
        .user(createPrompt(cordel))
        .call()
        .content();
  }

  private String createPrompt(Cordel cordel) {
    String prompt = reviewPrompt.formatted(
        defaultValue(cordel.getTitle()),
        cordel.getAuthor() != null ? defaultValue(cordel.getAuthor().getName()) : "desconhecido",
        cordel.getYear() != null ? cordel.getYear() : "desconhecido",
        defaultValue(cordel.getSource()),
        defaultValue(cordel.getContent())
    );
    log.debug("generated prompt for cordel id {}: {}", cordel.getId(), prompt);
    return prompt;
  }

  private String defaultValue(String value) {
    return value == null || value.isBlank() ? "desconhecido" : value;
  }
}
