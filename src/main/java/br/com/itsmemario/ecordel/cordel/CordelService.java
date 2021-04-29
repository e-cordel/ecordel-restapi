/*
 * Copyright 2020 Projeto e-cordel (http://ecordel.com.br)
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

import br.com.itsmemario.ecordel.xilogravura.XilogravuraEntity;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CordelService {

	private final Logger logger = LoggerFactory.getLogger(CordelService.class);

	private CordelRepository repository;
	private XilogravuraService xilogravuraService;

	@Autowired
	public CordelService(CordelRepository repository, XilogravuraService xilogravuraService) {
		super();
		this.repository = repository;
		this.xilogravuraService = xilogravuraService;
	}

	public Page<CordelView> getCordels(Pageable pageable) {
		return repository.findAllProjectedBy(pageable);
	}
	
	public CordelEntity save(CordelTo cordelTo) {
		return this.save(cordelTo.toEntity());
	}

	public CordelEntity save(CordelEntity cordelEntity) {
		return repository.save(cordelEntity);
	}

	public Page<CordelView> findByTags(List<String> tags, Pageable pageable){
		return repository.findByTags(tags, pageable);
	}

	public Optional<CordelEntity> findById(Long id) {
		return repository.findById(id);
	}

	public Page<CordelSummary> findByTitle(String title, Pageable pageable) {
		return repository.findByTitleLike(title, pageable);
	}

	public CordelEntity updateXilogravura(Long cordelId, XilogravuraTo xilogravuraTo, MultipartFile file) {
		Optional<CordelEntity> byId = findById(cordelId);

		if(byId.isPresent()) {
			CordelEntity cordelEntity = byId.get();
			XilogravuraEntity xilogravuraEntityWithFile = xilogravuraService.createXilogravuraWithFile(xilogravuraTo.toEntity(), file);
			cordelEntity.setXilogravura(xilogravuraEntityWithFile);
			return save(cordelEntity);
		}else{
			throw new CordelNotFoundException();
		}
	}
}
