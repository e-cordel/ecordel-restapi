/*
 * Copyright 2025 Projeto e-cordel (http://ecordel.com.br)
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface CordelRepository extends JpaRepository<Cordel, Long> {

  @Query(value = """
          SELECT
            c.id AS id, c.title AS title, c.description AS description, c.ebookUrl AS ebookUrl,
            a.name AS authorName, a.id AS authorId,
            x.url AS xilogravuraUrl, x.description AS xilogravuraDescription
          FROM Cordel c
          JOIN c.author a
          LEFT JOIN c.xilogravura x
          WHERE c.published = :#{#request.published}
            and (a.id = :#{#request.authorId} OR :#{#request.authorId} IS NULL)
          """)
  Page<CordelSummary> findAllByPublished(CordelSummaryRequest request, Pageable pageable);

  @Query(value = """
          SELECT
            c.id AS id, c.title AS title, c.description AS description, c.ebookUrl AS ebookUrl,
            a.name AS authorName, a.id AS authorId,
            x.url AS xilogravuraUrl, x.description AS xilogravuraDescription
          FROM Cordel c
          JOIN c.author a
          LEFT JOIN c.xilogravura x
          WHERE c.published = :#{#request.published}
            and LOWER(c.title) LIKE lower(:#{#request.formatedTitle})
            and (a.id = :#{#request.authorId} OR :#{#request.authorId} IS NULL)
          """)
  Page<CordelSummary> findAllByPublishedAndTitleLike(CordelSummaryRequest request, Pageable pageable);

}
