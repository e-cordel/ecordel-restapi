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

import br.com.itsmemario.ecordel.author.AuthorTo;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraTo;

import java.util.Objects;
import java.util.Set;

public class CordelTo implements CordelView{

    private Long id;
    private AuthorTo authorTo;
    private String title;
    private String content;
    private XilogravuraTo xilogravuraTo;
    private String description;
    private Set<String> tags;

    CordelTo() {}

    public static CordelTo of(Long id) {
        CordelTo cordelEntity = new CordelTo();
        cordelEntity.id = id;
        return cordelEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuthorTo getAuthor() {
        return authorTo;
    }

    public void setAuthor(AuthorTo authorTo) {
        this.authorTo = authorTo;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public XilogravuraTo getXilogravura() {
        return xilogravuraTo;
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

    public void setXilogravura(XilogravuraTo xilogravuraTo) {
        this.xilogravuraTo = xilogravuraTo;
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
        CordelTo cordelEntity = (CordelTo) o;
        return Objects.equals(id, cordelEntity.id) &&
                Objects.equals(title, cordelEntity.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }


    public CordelEntity toEntity(){
        CordelEntity cordelEntity = new CordelEntity();
        cordelEntity.setId(this.id);
        cordelEntity.setAuthor(authorTo.toEntity());
        cordelEntity.setContent(this.content);
        cordelEntity.setXilogravura(xilogravuraTo.toEntity());
        cordelEntity.setDescription(this.description);
        cordelEntity.setTags(this.tags);
        return cordelEntity;
    }


}