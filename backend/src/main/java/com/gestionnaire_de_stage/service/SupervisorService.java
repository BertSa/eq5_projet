package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.exception.EmailAndPasswordDoesNotExistException;
import com.gestionnaire_de_stage.exception.IdDoesNotExistException;
import com.gestionnaire_de_stage.exception.SupervisorAlreadyExistsException;
import com.gestionnaire_de_stage.model.Student;
import com.gestionnaire_de_stage.model.Supervisor;
import com.gestionnaire_de_stage.repository.SupervisorRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupervisorService {

    private final SupervisorRepository supervisorRepository;

    public SupervisorService(SupervisorRepository supervisorRepository) {
        this.supervisorRepository = supervisorRepository;
    }

    public Supervisor create(Supervisor supervisor) throws SupervisorAlreadyExistsException {
        Assert.isTrue(supervisor != null, "Superviseur est null");
        if (isNotValid(supervisor)) {
            throw new SupervisorAlreadyExistsException();
        }
        return supervisorRepository.save(supervisor);
    }

    public Supervisor getOneByID(Long aLong) throws IdDoesNotExistException {
        Assert.isTrue(aLong != null, "ID est null");
        if (isIdNotValid(aLong)) {
            throw new IdDoesNotExistException();
        }
        return supervisorRepository.getById(aLong);
    }


    public List<Supervisor> getAll() {
        return supervisorRepository.findAll();
    }

    public boolean assign(Student student, Supervisor supervisor){
        if (supervisor.getStudentList() == null)
            supervisor.setStudentList(new ArrayList<>());
        supervisor.getStudentList().add(student);
        supervisorRepository.save(supervisor);
        return true;
    }

    public Supervisor update(Supervisor supervisor, Long aLong) throws IdDoesNotExistException {
        Assert.isTrue(aLong != null, "ID est null");
        Assert.isTrue(supervisor != null, "Le superviseur est null");
        if (isIdNotValid(aLong)) {
            throw new IdDoesNotExistException();
        }
        supervisor.setId(aLong);
        return supervisorRepository.save(supervisor);
    }

    public void deleteByID(Long aLong) throws IdDoesNotExistException {
        Assert.isTrue(aLong != null, "ID est null");
        if (isIdNotValid(aLong)) {
            throw new IdDoesNotExistException();
        }
        supervisorRepository.deleteById(aLong);
    }

    public Supervisor getOneByEmailAndPassword(String email, String password) throws EmailAndPasswordDoesNotExistException {
        Assert.isTrue(email != null, "Le courriel est null");
        Assert.isTrue(password != null, "Le mot de passe est null");
        if (!isEmailAndPasswordValid(email, password)) {
            throw new EmailAndPasswordDoesNotExistException();
        }
        return supervisorRepository.findSupervisorByEmailAndPassword(email, password);
    }

    private boolean isNotValid(Supervisor supervisor) {
        return supervisor.getEmail() != null &&
                supervisorRepository.existsByEmail(supervisor.getEmail());
    }

    private boolean isIdNotValid(Long id) {
        return !supervisorRepository.existsById(id);
    }

    private boolean isEmailAndPasswordValid(String email, String password) {
        return supervisorRepository.existsByEmailAndPassword(email, password);
    }
}
