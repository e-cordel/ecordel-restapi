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

import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CordelService {

	private CordelRepository repository;
	private XilogravuraService xilogravuraService;

	@Autowired
	public CordelService(CordelRepository repository, XilogravuraService xilogravuraService) {
		super();
		this.repository = repository;
		this.xilogravuraService = xilogravuraService;
	}

	public Cordel save(Cordel cordel) {
		return repository.save(cordel);
	}

	public Page<CordelView> findByTags(List<String> tags, Pageable pageable){
		return repository.findByTags(tags, pageable);
	}

	public Optional<Cordel> findById(Long id) {
		return repository.findById(id);
	}

	public Page<CordelSummary> findPublishedByTitle(boolean published, String title, Pageable pageable) {
		return repository.findPublishedByTitleLike(published, title, pageable);
	}

	public Cordel updateXilogravura(Long cordelId, MultipartFile file) {
		Optional<Cordel> byId = findById(cordelId);

		if(byId.isPresent()) {
			Cordel cordel = byId.get();
			String xilogravuraUrl = xilogravuraService.createXilogravuraWithFile(file);
			cordel.setXilogravuraUrl( xilogravuraUrl );
			return save(cordel);
		}else{
			throw new CordelNotFoundException();
		}
	}
}
