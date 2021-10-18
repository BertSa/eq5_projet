package com.gestionnaire_de_stage.repository;

import com.gestionnaire_de_stage.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {

    List<Curriculum> findAllByIsValid(boolean isValid);
    List<Curriculum> findAllByIsValidIsNull();

}
