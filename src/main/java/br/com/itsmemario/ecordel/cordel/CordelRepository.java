package br.com.itsmemario.ecordel.cordel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface CordelRepository extends CustomCordelRepository, JpaRepository<Cordel, Long>{

    Page<CordelView> findAllProjectedBy(Pageable pageable);

    Page<CordelView> findByTitleLike(String title, Pageable pageable);
}
