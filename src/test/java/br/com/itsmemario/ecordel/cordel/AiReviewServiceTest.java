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

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiReviewServiceTest {

  @Mock
  private CordelService cordelService;

  private ChatClient.Builder chatClientBuilder;
  private AiReviewService aiReviewService;

  @BeforeEach
  void setUp() {
    chatClientBuilder = mock(ChatClient.Builder.class, RETURNS_DEEP_STUBS);
    aiReviewService = new AiReviewService(chatClientBuilder, cordelService);
  }

  @Test
  void shouldThrowBadRequestWhenCordelDoesNotExist() {
    when(cordelService.findById(999L)).thenReturn(Optional.empty());

    Assertions.assertThatThrownBy(() -> aiReviewService.review(999L))
    .isInstanceOf(CordelNotFoundException.class);
  }
}
