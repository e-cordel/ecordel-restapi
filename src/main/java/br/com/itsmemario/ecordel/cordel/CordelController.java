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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("cordels")
public class CordelController {

  private final Logger logger = LoggerFactory.getLogger(CordelController.class);

  private CordelService service;

  @Autowired
  CordelController(CordelService service) {
    this.service = service;
  }

  @GetMapping("{id}")
  public ResponseEntity<CordelDto> getCordel(@PathVariable Long id) {

    Optional<Cordel> cordel = service.findById(id);
    if (cordel.isPresent()) {
      CordelDto body = CordelMapper.INSTANCE.toDto(cordel.get());
      return ResponseEntity.ok(body);
    } else {
      logger.info("cordel with id {} not fond", id);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("summaries")
  public Page<CordelSummary> getCordels(CordelSummaryRequest request, Pageable pageable) {
    return service.findPublishedByTitle(request, pageable);
  }

  @PostMapping
  public ResponseEntity<String> create(@RequestBody @Valid CordelDto dto, UriComponentsBuilder uriBuilder) {

    var newCordel = service.save(CordelMapper.INSTANCE.toEntity(dto));
    var uri = uriBuilder.path("/cordels/{id}").buildAndExpand(newCordel.getId()).toUri();
    logger.info("new cordel Location header: {}", uri.getPath());
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<CordelDto> update(@RequestBody @Valid CordelDto dto, @PathVariable Long id) {

    Optional<Cordel> existingCordel = service.findById(id);
    if (existingCordel.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    var cordel = CordelMapper.INSTANCE.toEntity(dto);
    cordel.setId(id);
    Cordel updatedCordel = service.save(cordel);
    return ResponseEntity.ok(CordelMapper.INSTANCE.toDto(updatedCordel));
  }

  @PutMapping("{id}/xilogravura")
  public ResponseEntity<CordelDto> putXilogravura(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    Cordel cordel = service.updateXilogravura(id, file);
    return ResponseEntity.ok(CordelMapper.INSTANCE.toDto(cordel));
  }

  /**
   * This endpoint is called by automatic routines to generate ebook files.
   * @param id cordel id.
   * @param ebookUrl the url where the ebook is served.
   * @return ok if the request completes successfully.
   */
  @PutMapping("{id}/ebook-url")
  public ResponseEntity<Void> updateEbookUrl(@PathVariable Long id, @RequestBody @NotBlank String ebookUrl) {
    service.updateEbookUrl(id, ebookUrl);
    return ResponseEntity.ok().build();
  }

}
