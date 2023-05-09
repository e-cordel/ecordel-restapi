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

import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.times;

class CordelServiceTest {

    @Mock
    private XilogravuraService xilogravuraService;
    @Mock
    private CordelRepository cordelRepository;

    private final Pageable page = Pageable.unpaged();
    private CordelService cordelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cordelService = new CordelService(cordelRepository, xilogravuraService);
    }

    @Test
    void testIfTitleIsInvalid_QueryOnlyByPublished() {

        //arrange
        CordelSummaryRequest invalidRequest0 = CordelSummaryRequest.builder().published(true).title("").build();
        CordelSummaryRequest invalidRequest1 = CordelSummaryRequest.builder().published(true).title(null).build();

        //act
        cordelService.findPublishedByTitle(invalidRequest0, page);
        cordelService.findPublishedByTitle(invalidRequest1, page);

        //test
        Mockito.verify(cordelRepository).findAllByPublished(invalidRequest0, page);
        Mockito.verify(cordelRepository).findAllByPublished(invalidRequest1, page);
        Mockito.verify(cordelRepository, times(0))
                .findAllByPublishedAndTitleLike(invalidRequest0, page);


    }

    @Test
    void testIfTitleIsValid_QueryOnlyByPublishedAndTitleLike() {

        //arrange
        CordelSummaryRequest validRequest0 = CordelSummaryRequest.builder().published(true).title("title").build();

        //act
        cordelService.findPublishedByTitle(validRequest0, page);

        //test
        Mockito.verify(cordelRepository).findAllByPublishedAndTitleLike(validRequest0, page);
        Mockito.verify(cordelRepository, times(0)).findAllByPublished(validRequest0, page);
    }

}