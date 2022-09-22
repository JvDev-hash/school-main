package br.com.alura.school.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<List<Section>> findByCode(String code);

    Section findByCourseCodeAndCode(String courseCode, String code) ;
}
