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

package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.author.Author;

import java.util.Arrays;
import java.util.HashSet;

public class CordelUtil {

    private CordelUtil() {}

    public static Cordel newCordel(boolean published, Author author) {
        var cordel = new Cordel();
        cordel.setDescription("description");
        cordel.setAuthor(author);
        cordel.setTitle("title");
        cordel.setContent("""
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
        Proin eget scelerisque nulla, vel cursus nunc. Maecenas ac malesuada purus, vitae vulputate mauris.
        
        Praesent malesuada mi ac nunc dignissim, eu congue nunc facilisis. Aenean dictum ex quis convallis laoreet.
        Nullam efficitur massa ut nulla blandit congue. Etiam ornare ipsum nec nunc suscipit ultrices. Proin a turpis est.
        
        Mauris blandit sagittis risus a tincidunt. Nulla in purus purus.
        """);
        cordel.setPublished(published);
        cordel.setTags(new HashSet<>(Arrays.asList("tag1","tag2")));
        return cordel;
    }
}
