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

import br.com.itsmemario.ecordel.review.AiReviewService;
import br.com.itsmemario.ecordel.review.AiReviewResponse;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiReviewServiceTest {

  @Mock
  private CordelService cordelService;
  @Mock
  private CacheManager cacheManager;
  @Mock
  private Cache cache;

  private ChatClient.Builder chatClientBuilder;
  private AiReviewService aiReviewService;

  @BeforeEach
  void setUp() {
    chatClientBuilder = mock(ChatClient.Builder.class, RETURNS_DEEP_STUBS);
    when(cacheManager.getCache(AiReviewService.REVIEW_CACHE_NAME)).thenReturn(cache);
    aiReviewService = new AiReviewService(chatClientBuilder, cordelService, cacheManager);
  }

  @Test
  void shouldThrowBadRequestWhenCordelDoesNotExist() {
    when(cordelService.findById(999L)).thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> aiReviewService.review(999L, "review-999"))
    .isInstanceOf(CordelNotFoundException.class);
  }

  @Test
  void shouldPutPendingReviewInCacheWithNullContent() {
    var cordel = new Cordel();
    cordel.setId(1L);
    cordel.setTitle("Cordel title");
    cordel.setContent("Cordel content");
    when(cordelService.findById(1L)).thenReturn(Optional.of(cordel));

    ReflectionTestUtils.setField(aiReviewService, "systemPrompt", "system prompt");
    ReflectionTestUtils.setField(aiReviewService, "userPrompt", "{{title}} - {{content}}");

    when(chatClientBuilder.build()
        .prompt()
        .system("system prompt")
        .user("Cordel title - Cordel content")
        .call()
        .content()).thenReturn("reviewed content");

    aiReviewService.review(1L, "review-1");

    verify(cache).put(eq("review-1"), eq(new AiReviewResponse(false, null)));
  }
}
