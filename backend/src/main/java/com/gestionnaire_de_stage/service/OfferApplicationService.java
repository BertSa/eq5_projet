package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.enums.Status;
import com.gestionnaire_de_stage.exception.IdDoesNotExistException;
import com.gestionnaire_de_stage.exception.StudentAlreadyAppliedToOfferException;
import com.gestionnaire_de_stage.exception.StudentHasNoCurriculumException;
import com.gestionnaire_de_stage.model.Curriculum;
import com.gestionnaire_de_stage.model.Offer;
import com.gestionnaire_de_stage.model.OfferApplication;
import com.gestionnaire_de_stage.model.Student;
import com.gestionnaire_de_stage.repository.OfferApplicationRepository;
import io.jsonwebtoken.lang.Assert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferApplicationService {

    private final OfferApplicationRepository offerApplicationRepository;
    private final OfferService offerService;
    private final ManagerService managerService;
    private final StudentService studentService;


    public OfferApplicationService(OfferApplicationRepository offerApplicationRepository, OfferService offerService, ManagerService managerService, StudentService studentService) {
        this.offerApplicationRepository = offerApplicationRepository;
        this.offerService = offerService;
        this.managerService = managerService;
        this.studentService = studentService;
    }


    public OfferApplication create(Long idOffer, Long idStudent) throws StudentAlreadyAppliedToOfferException, IdDoesNotExistException, IllegalArgumentException, StudentHasNoCurriculumException {
        Assert.isTrue(idOffer != null, "L'id de l'offre ne peut pas être null");
        Optional<Offer> offer = offerService.findOfferById(idOffer);
        Student student = studentService.getOneByID(idStudent);
        Curriculum curriculum = student.getPrincipalCurriculum();

        if (curriculum == null)
            throw new StudentHasNoCurriculumException();

        if (offer.isEmpty())
            throw new IdDoesNotExistException();

        if (offerApplicationRepository.existsByOfferAndCurriculum(offer.get(), curriculum))
            throw new StudentAlreadyAppliedToOfferException();

        OfferApplication offerApplication = new OfferApplication();
        offerApplication.setOffer(offer.get());
        offerApplication.setCurriculum(student.getPrincipalCurriculum());
        offerApplication.setStatus(Status.CV_ENVOYE);

        return offerApplicationRepository.save(offerApplication);
    }

    public List<OfferApplication> getAllByOfferCreatorEmail(String email) {
        Assert.isTrue(email != null, "Le courriel ne peut pas être null");
        return offerApplicationRepository.getAllByOffer_CreatorEmail(email);
    }

    public List<OfferApplication> getOffersApplicationsStageTrouver(Long id) throws IdDoesNotExistException {
        Assert.isTrue(id != null, "L'id du gestionnaire ne peut pas être null!");
        if (managerService.isIDNotValid(id))
            throw new IdDoesNotExistException();

        return offerApplicationRepository.getAllByStatus(Status.STAGE_TROUVE);
    }

    public OfferApplication getOneById(Long idOfferApplication) throws IdDoesNotExistException {
        Assert.isTrue(idOfferApplication != null, "L'id de l'application ne peut pas être null");
        if (offerApplicationRepository.existsById(idOfferApplication))
            throw new IdDoesNotExistException();
        return offerApplicationRepository.getById(idOfferApplication);
    }
}
