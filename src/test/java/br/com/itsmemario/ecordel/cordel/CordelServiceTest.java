/*
 * Copyright 2021 Projeto e-cordel (http://ecordel.com.br)
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

import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CordelServiceTest {

  @Mock
  private XilogravuraService xilogravuraService;
  @Mock
  private CordelRepository cordelRepository;

  private final Pageable page = Pageable.unpaged();
  private CordelService cordelService;

  @BeforeEach
  void setUp() {
    cordelService = new CordelService(cordelRepository, xilogravuraService);
  }

  @Test
  void testIfTitleIsInvalid_QueryOnlyByPublished() {
    CordelSummaryRequest invalidRequest0 = CordelSummaryRequest.builder().published(true).title("").build();
    CordelSummaryRequest invalidRequest1 = CordelSummaryRequest.builder().published(true).title(null).build();

    cordelService.findPublishedByTitle(invalidRequest0, page);
    cordelService.findPublishedByTitle(invalidRequest1, page);

    Mockito.verify(cordelRepository).findAllByPublished(invalidRequest0, page);
    Mockito.verify(cordelRepository).findAllByPublished(invalidRequest1, page);
    Mockito.verify(cordelRepository, times(0))
            .findAllByPublishedAndTitleLike(invalidRequest0, page);
  }

  @Test
  void testIfTitleIsValid_QueryOnlyByPublishedAndTitleLike() {
    CordelSummaryRequest validRequest0 = CordelSummaryRequest.builder().published(true).title("title").build();

    cordelService.findPublishedByTitle(validRequest0, page);

    Mockito.verify(cordelRepository).findAllByPublishedAndTitleLike(validRequest0, page);
    Mockito.verify(cordelRepository, times(0)).findAllByPublished(validRequest0, page);
  }

  @Test
  void shouldGenerateFormattedTextForDownload() {
    Cordel cordel = CordelUtil.newCordel(true, new Author("Test"));
    cordel.setYear(1999);
    cordel.setSource("https://ler.ecordel.com.br");
    when(cordelRepository.findById(1L)).thenReturn(Optional.of(cordel));

    CordelDto contentForDownload = cordelService.getContentForDownload(1L);
    log.info("contentForDownload: {}", contentForDownload.getContent());

    assertThat(contentForDownload).isNotNull();
    assertThat(contentForDownload.getTitle()).isEqualTo(cordel.getTitle());
    assertThat(contentForDownload.getContent()).contains("Título: " + cordel.getTitle(), "Autor: Test", "Texto do cordel:", "ler.ecordel.com.br");
  }
}
