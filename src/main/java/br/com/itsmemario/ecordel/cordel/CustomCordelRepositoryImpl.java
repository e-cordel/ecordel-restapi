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
import br.com.itsmemario.ecordel.xilogravura.Xilogravura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@Transactional(readOnly = true)
class CustomCordelRepositoryImpl implements CustomCordelRepository {

    public static final String TAGS = "tags";

    private static final String FIND_BY_TAGS_SQL_FROM = " from cordel_tags ct \n" +
            "join cordel c on c.id = ct.cordel_id \n" +
            "join author a on a.id = c.author_id \n" +
            "where ct.tags in (:tags) \n";

    private static final String FIND_BY_TAGS_SQL = "select distinct(c.id) as id, c.title, c.description, a.name, a.email \n" +
            FIND_BY_TAGS_SQL_FROM + " limit :limit offset :offset";

    private static final String FIND_BY_TAGS_SQL_COUNT = "select COUNT(distinct(c.id)) " + FIND_BY_TAGS_SQL_FROM ;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CordelView> findByTags(List<String> tags, Pageable pageable) {
        long count = countResults(tags);

        if(count > 0) {
            Query query = entityManager.createNativeQuery(this.FIND_BY_TAGS_SQL);
            query.setParameter(TAGS, tags);
            query.setParameter("limit", pageable.getPageSize());
            query.setParameter("offset", pageable.getOffset());
            List<Object[]> resultList = query.getResultList();
            List<Cordel> cordels = resultList.stream().map(this::buildCordel).collect(Collectors.toList());

            return new PageImpl(cordels, pageable, count);
        }

        return Page.empty(pageable);
    }

    private long countResults(List<String> tags) {
        BigInteger count = (BigInteger) entityManager.createNativeQuery(FIND_BY_TAGS_SQL_COUNT).setParameter(TAGS, tags).getSingleResult();
        return count.longValue();
    }

    private Cordel buildCordel(Object[] fields) {
        Cordel cordel = new Cordel();
        cordel.setId(((BigInteger)fields[0]).longValue());
        cordel.setTitle(String.valueOf(fields[1]));
        cordel.setDescription(String.valueOf(fields[2]));
        cordel.setAuthor(buildAuthor(fields[3],fields[4]));
        return cordel;
    }

    private Author buildAuthor(Object name, Object email) {
        Author author = new Author();
        author.setName(String.valueOf(name));
        author.setEmail(String.valueOf(email));
        return author;
    }

}
