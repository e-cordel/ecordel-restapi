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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

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
        logger.info("request received get cordel by id: {}", id);

        Optional<Cordel> cordel = service.findById(id);
        if (cordel.isPresent()) {
            CordelDto body = CordelDto.of(cordel.get());
            return ResponseEntity.ok(body);
        } else {
            logger.info("cordel with id {} not fond", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("summaries")
    public Page<CordelSummary> getCordels(@RequestParam(required = false) String title,
                                          @RequestParam(defaultValue = "true") boolean published,
                                          Pageable pageable) {
        logger.info("request received get cordels");
        return service.findPublishedByTitle(published, title, pageable);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CordelDto dto, UriComponentsBuilder uriBuilder) {
        logger.info("request received for create cordel: {}", dto);

        var newCordel = service.save(dto.toEntity());
        var uri = uriBuilder.path("/cordels/{id}").buildAndExpand(newCordel.getId()).toUri();
        logger.info("new cordel Location header: {}", uri.getPath());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("{id}/xilogravura")
    public ResponseEntity<CordelDto> putXilogravura(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        logger.info("request received, update xilogravura for cordel: {}", id);
        Cordel cordel = service.updateXilogravura(id, file);
        return ResponseEntity.ok(CordelDto.of(cordel));
    }

    @PutMapping("{id}")
    public ResponseEntity<CordelDto> update(@RequestBody @Valid CordelDto dto, @PathVariable Long id) {
        logger.info("request received update cordel with id: {}", id);

        Optional<Cordel> existingCordel = service.findById(id);
        if (existingCordel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var cordel = dto.toEntity();
        cordel.setId(id);
        Cordel updatedCordel = service.save(cordel);
        return ResponseEntity.ok(CordelDto.of(updatedCordel));
    }

}
