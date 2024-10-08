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
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import java.net.URL;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CordelService {

  private static final int MINIMUM_SIZE = 3;

  private final CordelRepository repository;
  private final XilogravuraService xilogravuraService;

  public Cordel save(Cordel cordel) {
    return repository.save(cordel);
  }

  public Optional<Cordel> findById(Long id) {
    return repository.findById(id);
  }

  public Page<CordelSummary> findPublishedByTitle(CordelSummaryRequest request, Pageable pageable) {
    if (isAValidString(request.getTitle())) {
      return repository.findAllByPublishedAndTitleLike(request, pageable);
    }
    return repository.findAllByPublished(request, pageable);
  }

  public Cordel updateXilogravura(Long cordelId, MultipartFile file) {
    Optional<Cordel> byId = findById(cordelId);

    if (byId.isPresent()) {
      Cordel cordel = byId.get();
      String xilogravuraUrl = xilogravuraService.createXilogravuraWithFile(file);
      cordel.setXilogravuraUrl(xilogravuraUrl);
      return save(cordel);
    } else {
      throw new CordelNotFoundException();
    }
  }

  private boolean isAValidString(String title) {
    return title != null && title.length() >= MINIMUM_SIZE;
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
}
