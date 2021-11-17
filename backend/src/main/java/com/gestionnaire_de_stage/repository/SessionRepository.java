package com.gestionnaire_de_stage.repository;

import com.gestionnaire_de_stage.enums.TypeSession;
import com.gestionnaire_de_stage.model.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    boolean existsByTypeSessionAndYear(TypeSession typeSession, Year year);

    List<Session> findAllByYearGreaterThanEqual(Year year);
}