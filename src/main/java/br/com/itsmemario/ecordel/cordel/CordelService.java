/*
 * Copyright 2020-2021 Projeto e-cordel (http://ecordel.com.br)
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

import br.com.itsmemario.ecordel.exception.BadRequestException;
import br.com.itsmemario.ecordel.exception.FormError;
import br.com.itsmemario.ecordel.xilogravura.Xilogravura;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.StringWriter;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class CordelService {

  private static final int MINIMUM_TITLE_SIZE = 3;
  private static final MustacheFactory mf = new DefaultMustacheFactory();
  private static final Mustache cordelTemplate = mf.compile("mustache/cordel.mustache");

  private final CordelRepository repository;
  private final XilogravuraService xilogravuraService;

  @Transactional
  public Cordel save(Cordel cordel) {
    // this handles the case when a new xilogravura is set to an existing cordel during review and hibernate is not able to merge it properly
    if (xilogravuraIsNew(cordel.getXilogravura()) && Objects.nonNull(cordel.getId())) {
      var persistedXilo = xilogravuraService.save(cordel.getXilogravura());
      cordel.setXilogravura(persistedXilo);
    }
    return repository.save(cordel);
  }

  private boolean xilogravuraIsNew(Xilogravura xilogravura) {
    return Objects.nonNull(xilogravura) && (xilogravura.getId() == null || xilogravura.getId() == 0);
  }

  @Cacheable(value = "cordels", key = "#id")
  public Optional<Cordel> findById(Long id) {
    log.info("finding cordel by id: {}", id);
    return repository.findById(id);
  }

  @Cacheable(value = "publishedCordels", key = "#request.toString() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<CordelSummary> findPublishedByTitle(CordelSummaryRequest request, Pageable pageable) {
    log.info("finding published cordels by title: {}, page: {}, size: {}", request.getTitle(), pageable.getPageNumber(), pageable.getPageSize());
    if (titleIsValid(request.getTitle())) {
      return repository.findAllByPublishedAndTitleLike(request, pageable);
    }
    return repository.findAllByPublished(request, pageable);
  }

  /**
   * @deprecated new xilogravura model was created. This method will be removed in future versions.
   * @param cordelId
   * @param file
   * @return
   */
  public Cordel updateXilogravura(Long cordelId, MultipartFile file) {
    Optional<Cordel> byId = findById(cordelId);

    if (byId.isPresent()) {
      Cordel cordel = byId.get();
      String xilogravuraUrl = xilogravuraService.createXilogravuraWithFile(file);
      cordel.setXilogravuraUrl(xilogravuraUrl);
      return repository.save(cordel);
    } else {
      throw new CordelNotFoundException();
    }
  }

  private boolean titleIsValid(String title) {
    return title != null && title.length() >= MINIMUM_TITLE_SIZE;
  }

  public void updateEbookUrl(Long id, String ebookUrl) {
    try {
      new URL(ebookUrl).toURI();
    } catch (Exception e) {
      throw new BadRequestException(new FormError("ebookUrl", "Invalid value"));
    }
    Cordel cordel = repository.findById(id).orElseThrow(CordelNotFoundException::new);
    cordel.setEbookUrl(ebookUrl);
    repository.save(cordel);
  }

  /**
   * Generate a txt content for download using a mustache template.
   * Only the title and content are set in the DTO.
   * @param id cordel id
   * @return content for download
   */
  @Cacheable(value = "cordelContentForDownload", key = "#id")
  public Cordel getContentForDownload(Long id) {
    log.info("generating txt for cordel: {}", id);
    Cordel cordel = repository.findById(id).orElseThrow(CordelNotFoundException::new);

    StringWriter contentWriter = new StringWriter(cordel.getContent().length());
    cordelTemplate.execute(contentWriter, cordel);

    var cordelWithContent = new Cordel();
    cordelWithContent.setTitle(cordel.getTitle());
    cordelWithContent.setContent(contentWriter.toString());
    return cordelWithContent;
  }
}
