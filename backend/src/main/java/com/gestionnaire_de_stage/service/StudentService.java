package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.exception.IdDoesNotExistException;
import com.gestionnaire_de_stage.exception.StudentAlreadyExistsException;
import com.gestionnaire_de_stage.model.Student;
import com.gestionnaire_de_stage.repository.StudentRepository;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> create(Student student) throws StudentAlreadyExistsException {
        Assert.isTrue(student != null, "Etudiant est null");
        if (isNotValid(student)) {
            throw new StudentAlreadyExistsException();
        }
        return Optional.of(studentRepository.save(student));
    }

    public Optional<Student> getOneByID(Long aLong) throws IdDoesNotExistException {
        Assert.isTrue(aLong != null, "ID est null");
        if (!isIDValid(aLong)) {
            throw new IdDoesNotExistException();
        }
        return Optional.of(studentRepository.getById(aLong));
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> update(Student student, Long aLong) throws IdDoesNotExistException {
        Assert.isTrue(aLong != null, "ID est null");
        Assert.isTrue(student != null, "L'étudiant est null");
        if (!isIDValid(aLong)) {
            throw new IdDoesNotExistException();
        }
        student.setId(aLong);
        return Optional.of(studentRepository.save(student));
    }

    public boolean deleteByID(Long aLong) {
        if (aLong != null && studentRepository.existsById(aLong)) {
            studentRepository.deleteById(aLong);
            return true;
        }
        return false;
    }

    public Optional<Student> getOneByEmailAndPassword(String email, String password) {
        if (studentRepository.existsByEmailAndPassword(email, password)) {
            return Optional.of(studentRepository.findStudentByEmailAndPassword(email, password));
        }
        return Optional.empty();
    }

    private boolean isNotValid(Student student) {
        return student.getEmail() != null && studentRepository.existsByEmail(student.getEmail());
    }

    private boolean isIDValid(Long id) {
        return studentRepository.existsById(id);
    }
}
