package br.com.itsmemario.ecordel.cordel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCordelRepository {
    Page<CordelView> findByTags(List<String> tags, Pageable pageable);
}
