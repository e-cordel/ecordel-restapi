package br.com.itsmemario.ecordel.cordel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface CordelRepository extends CustomCordelRepository, JpaRepository<Cordel, Long>{

    Page<CordelView> findAllProjectedBy(Pageable pageable);
//    @Query("SELECT DISTINCT(c.id) as id, c.xilogravura as xilogravura, c.title as title, c.author as author, " +
//            "c.description as description, Collection as tags " +
//            "from Cordel c JOIN c.tags t WHERE t IN ?1 ")
//    Page<CordelView> findByTags(List<String> tags, Pageable pageable);

}
