/*
 * Copyright 2021 Projeto e-cordel (http://ecordel.com.br)
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

package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.author.AuthorTo;



public class XilogravuraTo implements XilogravuraView{

    private Long id;

    private String url;
    private String description;

    private AuthorTo xilografo;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AuthorTo getXilografo() {
        return xilografo;
    }

    public void setXilografo(AuthorTo xilografo) {
        this.xilografo = xilografo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public XilogravuraEntity toEntity(){
        XilogravuraEntity xilogravuraEntity = new XilogravuraEntity();
        xilogravuraEntity.setUrl(this.url);
        xilogravuraEntity.setDescription(this.description);
        xilogravuraEntity.setXilografo(xilografo.toEntity());
        return xilogravuraEntity;
    }
}