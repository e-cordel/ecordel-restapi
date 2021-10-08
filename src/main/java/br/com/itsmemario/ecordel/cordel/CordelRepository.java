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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface CordelRepository extends JpaRepository<Cordel, Long>{

    @Query("SELECT new br.com.itsmemario.ecordel.cordel.CordelSummary(c.id, c.title, c.xilogravuraUrl, a.name) " +
            "FROM Cordel c JOIN c.author a WHERE c.published = :published")
    Page<CordelSummary> findAllByPublished(boolean published, Pageable pageable);

    @Query("SELECT new br.com.itsmemario.ecordel.cordel.CordelSummary(c.id, c.title, c.xilogravuraUrl, a.name) " +
            "FROM Cordel c JOIN c.author a WHERE c.published = :published and lower(c.title) LIKE lower( :title ) ")
    Page<CordelSummary> findAllByPublishedAndTitleLike(boolean published, String title, Pageable pageable);

}
