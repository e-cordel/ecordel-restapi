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

import br.com.itsmemario.ecordel.author.Author;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cordel implements CordelView {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	private String xilogravuraUrl;
	private String description;
	@ElementCollection
	@CollectionTable(name = "cordel_tags")
	private Set<String> tags;
	private boolean published;

	Cordel() {}

	public static Cordel of(Long id) {
		Cordel cordel = new Cordel();
		cordel.id = id;
		return cordel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
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

	public String getXilogravuraUrl() {
		return xilogravuraUrl;
	}

	public void setXilogravuraUrl(String xilogravuraUrl) {
		this.xilogravuraUrl = xilogravuraUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags);
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cordel cordel = (Cordel) o;
		return Objects.equals(id, cordel.id) &&
				Objects.equals(title, cordel.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}
}
