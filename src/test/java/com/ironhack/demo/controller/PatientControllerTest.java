package com.ironhack.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demo.enums.Department;
import com.ironhack.demo.enums.Status;
import com.ironhack.demo.repository.EmployeeRepository;
import com.ironhack.demo.repository.PatientRepository;
import com.ironhack.demo.model.Employee;
import com.ironhack.demo.model.Patient;
import com.ironhack.demo.service.PatientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Employee employee1 = new Employee(2L, Department.CARDIOLOGY, "Talvi", Status.ON);
        employeeRepository.save(employee1);
        Patient patient1 = new Patient(1L, "Anna", LocalDate.of(1990, 5, 12), employee1);
        Patient patient2 = new Patient(2L, "Mlemlo", LocalDate.of(1985, 3, 20), employee1);
        patientRepository.saveAll(List.of(patient1, patient2));
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
    }

    @Test
    void getAllPatients() throws Exception {
        List<Patient> patients = patientRepository.findAll();
        String expectedJson = objectMapper.writeValueAsString(patients);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));  }

    @Test
    void getPatientById() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(patientRepository.findById(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getPatientsByDateOfBirthRange() throws Exception {
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(2000, 12, 31);
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/by-dob")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getPatientsByAdmittingDoctorDepartment() throws Exception {
        String department = "Cardiology";
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/by-department")
                        .param("department", department)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getPatientsWithDoctorStatusOff() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/by-status-off")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void addPatient() throws Exception {
        Employee admittedBy = employeeRepository.findById(2L).orElseThrow();

        Patient patient = new Patient(3L, "Jaume", LocalDate.of(1980, 10, 5), admittedBy);

        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patient)))
                .andExpect(status().isCreated());
    }

    @Test
    void updatePatientInfo() throws Exception {
        long patientId = 1L;

        // Obtener un empleado existente del repositorio
        Employee admittedBy = employeeRepository.findById(2L).orElseThrow();

        // Crear el paciente actualizado utilizando el empleado existente
        Patient updatedPatient = new Patient(patientId, "Anna Updated", LocalDate.of(1990, 5, 12), admittedBy);
        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPatient)))
                .andExpect(status().isOk());
    }
}

