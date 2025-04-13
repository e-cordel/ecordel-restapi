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
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cordel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "author_id")
  private Author author;
  @NotBlank
  private String title;
  @NotBlank
  private String content;
  /**
   * @deprecated This field is deprecated and will be removed in the future. Use {@link Xilogravura} instead.
   */
  @Column(name = "xilogravura_url")
  @Deprecated(forRemoval = true, since = "2025-01-01")
  private String xilogravuraUrl;
  private String description;
  @ElementCollection
  @CollectionTable(name = "cordel_tags")
  private Set<String> tags;
  private boolean published;
  private Integer year;
  @Column(name = "ebook_url")
  private String ebookUrl;
  private String source;

  Cordel() {
  }

  /**
   * Creates an empty cordel with only the id.
   *
   * @param id cordel identifier.
   * @return a new cordel.
   */
  public static Cordel of(Long id) {
    Cordel cordel = new Cordel();
    cordel.id = id;
    return cordel;
  }

  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cordel cordel = (Cordel) o;
    return Objects.equals(id, cordel.id) &&
        Objects.equals(title, cordel.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title);
  }
}
