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

import br.com.itsmemario.ecordel.author.AuthorEntity;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity(name = "cordel")
public class CordelEntity implements CordelView{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "author_id")
	private AuthorEntity author;
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@OneToOne
	@JoinColumn(name = "xilogravura_id")
	private XilogravuraEntity xilogravura;
	private String description;
	@ElementCollection
	@CollectionTable(name = "cordel_tags")
	private Set<String> tags;

	CordelEntity() {}

	public static CordelEntity of(Long id) {
		CordelEntity cordelEntity = new CordelEntity();
		cordelEntity.id = id;
		return cordelEntity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuthorEntity getAuthor() {
		return author;
	}

	public void setAuthor(AuthorEntity authorEntity) {
		this.author = authorEntity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public XilogravuraEntity getXilogravura() {
		return xilogravura;
	}

	public void setXilogravura(XilogravuraEntity xilogravuraEntity) {
		this.xilogravura = xilogravuraEntity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CordelEntity cordelEntity = (CordelEntity) o;
		return Objects.equals(id, cordelEntity.id) &&
				Objects.equals(title, cordelEntity.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}


}
