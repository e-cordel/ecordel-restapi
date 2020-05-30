package br.com.itsmemario.ecordel.cordel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface CordelRepository extends JpaRepository<Cordel, Long>{

    Page<CordelSummary> findAllProjectedBy(Pageable pageable);

}
