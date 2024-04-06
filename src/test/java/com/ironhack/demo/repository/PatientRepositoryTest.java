package com.ironhack.demo.repository;

import com.ironhack.demo.enums.Department;
import com.ironhack.demo.model.Patient;
import com.ironhack.demo.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    private List<Patient> patients;

    @BeforeEach
    void setUp() {
        patients = List.of(
                new Patient(1L, "Anna", LocalDate.of(1990, 5, 12), null),
                new Patient(2L, "Mlemlo", LocalDate.of(1985, 3, 20), null)
        );

        patientRepository.saveAll(patients);
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        patients = null;
    }

    @Test
    void testFindPatientsByDateOfBirthRange() {
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 12, 31);
        List<Patient> result = patientRepository.findPatientsByDateOfBirthRange(startDate, endDate);
        assertEquals(2, result.size());
    }

    @Test
    void testFindPatientsByAdmittingDoctorDepartment() {
        List<Patient> result = patientRepository.findPatientsByAdmittingDoctorDepartment(Department.CARDIOLOGY);
        assertEquals(0, result.size());
    }

    @Test
    void testFindAllPatientsWithDoctorStatusOff() {
        List<Patient> result = patientRepository.findAllPatientsWithDoctorStatusOff();
        assertEquals(0, result.size());
    }
}
