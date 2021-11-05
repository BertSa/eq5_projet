package com.gestionnaire_de_stage.service;

import com.gestionnaire_de_stage.exception.IdDoesNotExistException;
import com.gestionnaire_de_stage.model.*;
import com.gestionnaire_de_stage.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ManagerService managerService;

    @Mock
    private MonitorService monitorService;

    @Mock
    private StudentService studentService;

    @Test
    public void testGetAllByManagerSignatureNull() {
        when(contractRepository.getAllByManagerSignatureNull()).thenReturn(getDummyContractList());

        final List<Contract> actualContractList = contractService.getAllUnsignedContracts();

        assertThat(actualContractList.size()).isEqualTo(getDummyContractList().size());
    }

    @Test
    public void testAddManagerSignature_withValidEntries() throws Exception {
        String managerSignature = "Joe Janson";
        Contract dummyContract = getDummyContract();
        Manager dummyManager = getDummyManager();
        when(contractRepository.existsById(any())).thenReturn(true);
        when(contractRepository.getContractById(any())).thenReturn(dummyContract);
        when(contractRepository.save(any())).thenReturn(getDummyFilledContract());

        Contract actualContract = contractService.addManagerSignature(managerSignature, dummyContract.getId());
        assertThat(actualContract.getManager()).isEqualTo(dummyManager);
    }

    @Test
    public void testAddManagerSignature_withNullManagerSignature() {
        Contract dummyContract = getDummyContract();
        assertThrows(IllegalArgumentException.class,
                () -> contractService.addManagerSignature(null, dummyContract.getId()));
    }

    @Test
    public void testAddManagerSignature_withNullContractID() {
        String managerSignature = "Joe Janson";
        assertThrows(IllegalArgumentException.class,
                () -> contractService.addManagerSignature(managerSignature, null));
    }

    @Test
    public void testAddManagerSignature_withInvalidContractID() {
        String managerSignature = "Joe Janson";
        Contract dummyContract = getDummyContract();
        when(contractRepository.existsById(any())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> contractService.addManagerSignature(managerSignature, dummyContract.getId()));
    }

    @Test
    public void testFillPDF_withValidEntries() {
        Contract dummyContract = getDummyFilledContract();
        when(contractRepository.save(any())).thenReturn(dummyContract);

        Contract actualContract = contractService.fillPDF(new Contract(), new ByteArrayOutputStream());

        assertThat(actualContract.getContractPDF()).isEqualTo(dummyContract.getContractPDF());
        assertThat(actualContract.getManagerSignature()).isEqualTo(dummyContract.getManagerSignature());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testFillPDF_withNullContractId() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class,
                () -> contractService.fillPDF(null, baos));
    }

    @Test
    public void testGetAllUnsignedContractForMonitor_withValidEntries() throws Exception {
        List<Contract> dummyContractList = getDummyContractList();
        long monitor_id = 1L;
        when(monitorService.isIdInvalid(any())).thenReturn(false);
        when(contractRepository.getAllByOffer_CreatorIdAndAndMonitorSignatureNullAndManagerSignatureNotNull(any()))
                .thenReturn(dummyContractList);


        List<Contract> actualContractList = contractService.getAllUnsignedContractForMonitor(monitor_id);

        assertThat(actualContractList.get(1).getId()).isEqualTo(dummyContractList.get(1).getId());
    }

    @Test
    public void testGetAllUnsignedContractForMonitor_withNullMonitorID() {
        assertThrows(IllegalArgumentException.class,
                () -> contractService.getAllUnsignedContractForMonitor(null));
    }

    @Test
    public void testGetAllUnsignedContractForMonitor_withInvalidMonitorID() {
        long monitor_id = 1L;
        when(monitorService.isIdInvalid(any())).thenReturn(true);

        assertThrows(IdDoesNotExistException.class,
                () -> contractService.getAllUnsignedContractForMonitor(monitor_id));
    }

    @Test
    public void testAddMonitorSignature_validValidEntries() throws Exception {
        String monitorSignature = "Joe Janson";
        Contract dummyContract = getDummyContract();
        when(contractRepository.existsById(any())).thenReturn(true);
        when(contractRepository.getContractById(any())).thenReturn(dummyContract);
        when(contractRepository.save(any())).thenReturn(getDummyFilledContract());

        Contract actualContract = contractService.addMonitorSignature(monitorSignature, dummyContract.getId());
        assertThat(actualContract.getStudent().getFirstName()).isEqualTo(dummyContract.getStudent().getFirstName());
    }

    @Test
    public void testAddMonitorSignature_withNullMonitorSignature() {
        Contract dummyContract = getDummyContract();
        assertThrows(IllegalArgumentException.class,
                () -> contractService.addMonitorSignature(null, dummyContract.getId()));
    }

    @Test
    public void testAddMonitorSignature_withNullContractID() {
        String monitorSignature = "Joe Janson";
        assertThrows(IllegalArgumentException.class,
                () -> contractService.addMonitorSignature(monitorSignature, null));
    }

    @Test
    public void testAddMonitorSignature_withInvalidContractID() {
        String monitorSignature = "Joe Janson";
        long contract_id = 1L;
        when(contractRepository.existsById(any())).thenReturn(false);
        assertThrows(IdDoesNotExistException.class,
                () -> contractService.addMonitorSignature(monitorSignature, contract_id));

    }

    @Test
    public void testGetContractByStudentId_withValidEntries() throws Exception {
        Contract dummyContract = getDummyContract();
        when(studentService.isIDNotValid(any())).thenReturn(false);
        when(contractRepository.getContractByStudentId(any())).thenReturn(dummyContract);

        Contract actualContract = contractService.getContractByStudentId(dummyContract.getStudent().getId());

        assertThat(actualContract.getStudent().getFirstName()).isEqualTo(dummyContract.getStudent().getFirstName());

    }

    @Test
    public void testGetContractByStudentId_withNullStudentId() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> contractService.getContractByStudentId(null));
    }

    @Test
    public void testGetContractByStudentId_withInvalidStudentId() throws Exception {
        Contract dummyContract = getDummyContract();
        when(studentService.isIDNotValid(any())).thenReturn(true);

        assertThrows(IdDoesNotExistException.class,
                () -> contractService.getContractByStudentId(dummyContract.getStudent().getId()));
    }

    @Test
    public void testAddStudentSignature_withValidEntries() throws Exception {
        Contract dummyContract = getDummyContract();
        String studentSignature = "Dawn Soap";
        when(contractRepository.existsById(any())).thenReturn(true);
        when(contractRepository.getContractById(any())).thenReturn(dummyContract);
        when(contractRepository.save(any())).thenReturn(dummyContract);

        Contract actualContract = contractService.addStudentSignature(studentSignature, dummyContract.getId());

        assertThat(actualContract.getStudent().getFirstName()).isEqualTo(dummyContract.getStudent().getFirstName());
    }

    @Test
    public void testAddStudentSignature_withNullEntries() {
        assertThrows(IllegalArgumentException.class,
                () -> contractService.addStudentSignature(null, null));
    }

    @Test
    public void testAddStudentSignature_withInvalidContractId() {
        Contract dummyContract = getDummyContract();
        String studentSignature = "Dawn Soap";
        when(contractRepository.existsById(any())).thenReturn(false);

        assertThrows(IdDoesNotExistException.class,
                () -> contractService.addStudentSignature(studentSignature, dummyContract.getId()));
    }

    private List<Contract> getDummyContractList() {
        List<Contract> dummyContractList = new ArrayList<>();
        Contract contract1 = new Contract();
        contract1.setId(1L);
        contract1.setStudent(new Student());
        dummyContractList.add(contract1);
        Contract contract2 = new Contract();
        contract2.setId(2L);
        contract1.setStudent(new Student());
        dummyContractList.add(contract2);
        Contract contract3 = new Contract();
        contract3.setId(3L);
        contract1.setStudent(new Student());
        dummyContractList.add(contract3);

        return dummyContractList;
    }

    private Contract getDummyContract() {
        Contract dummyContract = new Contract();
        dummyContract.setId(1L);
        dummyContract.setStudent(getDummyStudent());
        return dummyContract;
    }

    private Contract getDummyFilledContract() {
        Contract dummyContract = new Contract();
        dummyContract.setManager(getDummyManager());
        dummyContract.setManagerSignature("Joe Janson");
        dummyContract.setId(1L);
        dummyContract.setStudent(getDummyStudent());
        dummyContract.setContractPDF(new byte[] {5, 1, 9, 2, 6});
        return dummyContract;
    }

    private Student getDummyStudent() {
        Student dummyStudent = new Student();
        dummyStudent.setId(1L);
        dummyStudent.setLastName("Candle");
        dummyStudent.setFirstName("Tea");
        dummyStudent.setEmail("cant@outlook.com");
        dummyStudent.setPassword("cantPass");
        dummyStudent.setDepartment("info");
        dummyStudent.setMatricule("4673943");
        return dummyStudent;
    }

    private Manager getDummyManager() {
        Manager dummyManager = new Manager();
        dummyManager.setPassword("Test1234");
        dummyManager.setEmail("oussamakably@gmail.com");
        dummyManager.setFirstName("Oussama");
        dummyManager.setLastName("Kably");
        dummyManager.setPhone("5143643320");
        return dummyManager;
    }

}