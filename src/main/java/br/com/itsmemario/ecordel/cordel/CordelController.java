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
import java.io.ByteArrayInputStream;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("cordels")
public class CordelController {

  private CordelService service;

  @Autowired
  CordelController(CordelService service) {
    this.service = service;
  }

  @GetMapping("{id}")
  public ResponseEntity<Cordel> getCordel(@PathVariable Long id) {
    Optional<Cordel> cordel = service.findById(id);
    if (cordel.isPresent()) {
      return ResponseEntity.ok(cordel.get());
    } else {
      log.info("cordel with id {} not fond", id);
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "{id}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<InputStreamResource> downloadTxt(@PathVariable Long id) {
    var cordel = service.getContentForDownload(id);
    var contentBytes = cordel.getContent().getBytes();
    var inputStreamResource = new InputStreamResource(new ByteArrayInputStream(contentBytes));

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cordel.getTitle() + ".txt\"")
            .contentType(MediaType.TEXT_PLAIN)
            .contentLength(contentBytes.length)
            .body(inputStreamResource);
  }

  @GetMapping("summaries")
  public Page<CordelSummary> getCordels(CordelSummaryRequest request, Pageable pageable) {
    return service.findPublishedByTitle(request, pageable);
  }

  @PostMapping
  public ResponseEntity<String> create(@RequestBody @Valid Cordel cordel, UriComponentsBuilder uriBuilder) {
    var newCordel = service.save(cordel);
    var uri = uriBuilder.path("/cordels/{id}").buildAndExpand(newCordel.getId()).toUri();
    log.info("new cordel Location header: {}", uri.getPath());
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("{id}")
  public ResponseEntity<Cordel> update(@RequestBody @Valid Cordel cordel, @PathVariable Long id) {
    Optional<Cordel> existingCordel = service.findById(id);
    if (existingCordel.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    cordel.setId(id);
    Cordel updatedCordel = service.save(cordel);
    return ResponseEntity.ok(updatedCordel);
  }

  @PutMapping("{id}/xilogravura")
  public ResponseEntity<Cordel> putXilogravura(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    Cordel cordel = service.updateXilogravura(id, file);
    return ResponseEntity.ok(cordel);
  }

  /**
   * This endpoint is called by automatic routines to set the ebook url for download.
   *
   * @param id       cordel id.
   * @param ebookUrl the url where the ebook is served.
   * @return ok if the request completes successfully.
   */
  @PutMapping("{id}/ebook-url")
  public ResponseEntity<Void> updateEbookUrl(@PathVariable Long id, @RequestBody @NotBlank String ebookUrl) {
    service.updateEbookUrl(id, ebookUrl);
    return ResponseEntity.ok().build();
  }

}
