package ca.bertsa.internos.repository;

import ca.bertsa.internos.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);

    boolean existsByMatricule(String matricule);

    boolean existsByIdAndSupervisorNull(Long id);

    Student findStudentByEmailAndPassword(String email, String password);

    List<Student> getAllByPrincipalCurriculum_IsValidAndSupervisorNull(Boolean isValid);

    Student getByEmail(String email);
}
