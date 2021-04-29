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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@Transactional(readOnly = true)
class CustomCordelRepositoryImpl implements CustomCordelRepository {

    private static final int MINIMUM_SIZE = 3;
    private static final String TAGS = "tags";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private static final String LIMIT_SQL = " limit :limit offset :offset ";

    private static final String FIND_BY_TAGS_SQL_FROM = " from cordel_tags ct \n" +
            "join cordel c on c.id = ct.cordel_id \n" +
            "join author a on a.id = c.author_id \n" +
            "where ct.tags in (:tags) \n";

    private static final String FIND_BY_TAGS_SQL = "select distinct(c.id) as id, c.title, c.description, a.name, a.email " + FIND_BY_TAGS_SQL_FROM + LIMIT_SQL;

    private static final String FIND_BY_TAGS_SQL_COUNT = "select COUNT(distinct(c.id)) " + FIND_BY_TAGS_SQL_FROM ;

    private static final String CORDEL_SUMMARY = "SELECT new br.com.itsmemario.ecordel.cordel.CordelSummary(c.id, c.title, x.url, a.name) FROM cordel c JOIN c.author a LEFT JOIN c.xilogravura x";
    private static final String COUNT_CORDEL_SUMMARY = "SELECT COUNT(c.id) FROM cordel c JOIN c.author a LEFT JOIN c.xilogravura x";
    public static final String TITLE = "title";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CordelView> findByTags(List<String> tags, Pageable pageable) {
        long count = countResults(tags);
        if(count == 0) return Page.empty(pageable);

        Query query = entityManager.createNativeQuery(this.FIND_BY_TAGS_SQL);
        query.setParameter(TAGS, tags);
        query.setParameter(LIMIT, pageable.getPageSize());
        query.setParameter(OFFSET, pageable.getOffset());
        List<Object[]> resultList = query.getResultList();
        List<CordelView> cordels = resultList.stream().map(this::buildCordel).collect(Collectors.toList());

        return new PageImpl<>(cordels, pageable, count);
    }

    private long countResults(List<String> tags) {
        BigInteger count = (BigInteger) entityManager.createNativeQuery(FIND_BY_TAGS_SQL_COUNT).setParameter(TAGS, tags).getSingleResult();
        return count.longValue();
    }

    private CordelEntity buildCordel(Object[] fields) {
        CordelEntity cordelEntity = new CordelEntity();
        cordelEntity.setId(((BigInteger)fields[0]).longValue());
        cordelEntity.setTitle(String.valueOf(fields[1]));
        cordelEntity.setDescription(String.valueOf(fields[2]));
        cordelEntity.setAuthor(buildAuthor(fields[3],fields[4]));
        return cordelEntity;
    }

    private AuthorEntity buildAuthor(Object name, Object email) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(String.valueOf(name));
        authorEntity.setEmail(String.valueOf(email));
        return authorEntity;
    }

    @Override
    public Page<CordelSummary> findByTitleLike(String title, Pageable pageable) {

        StringBuilder sql = new StringBuilder(CORDEL_SUMMARY);
        StringBuilder countSql = new StringBuilder(COUNT_CORDEL_SUMMARY);

        TypedQuery<Long> countQuery =  entityManager.createQuery(countSql.toString(), Long.class);
        TypedQuery<CordelSummary> query = entityManager.createQuery(sql.toString(), CordelSummary.class);

        if(isAValidString(title)){
            query = createQueryWithTitle(title, sql, CordelSummary.class);
            countQuery = createQueryWithTitle(title, countSql, Long.class);
        }

        Long count = countQuery.getSingleResult();
        if(count == 0) return Page.empty(pageable);

        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult((int)pageable.getOffset());

        List<CordelSummary> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, count);
    }

    private boolean isAValidString(String title) {
        return title != null && title.length() >= MINIMUM_SIZE;
    }

    private <T> TypedQuery<T> createQueryWithTitle(String title, StringBuilder sql, Class<T> clazz) {
        String where = " WHERE lower(c.title) LIKE lower( :title )";
        TypedQuery<T> query;
        sql.append(where);
        query = entityManager.createQuery(sql.toString(), clazz);
        query.setParameter(TITLE, String.format("%%%s%%", title));
        return query;
    }

}
