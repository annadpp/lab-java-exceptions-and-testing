package com.ironhack.demo.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.demo.enums.Department;
import com.ironhack.demo.enums.Status;
import com.ironhack.demo.model.Employee;
import com.ironhack.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Employee employee1 = new Employee(1L, Department.CARDIOLOGY, "Talvi", Status.ON);
        Employee employee2 = new Employee(2L, Department.IMMUNOLOGY, "Jaume", Status.OFF);
        employeeRepository.saveAll(List.of(employee1, employee2));
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void getAllDoctors() throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        String expectedJson = objectMapper.writeValueAsString(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getDoctorById() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(employeeRepository.findById(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getDoctorsByDepartment() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(employeeRepository.findAllByDepartment(Department.CARDIOLOGY));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/department/{department}", Department.CARDIOLOGY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getDoctorsByStatus() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(employeeRepository.findAllByStatus(Status.ON));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/status/{status}", Status.ON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void addEmployee() throws Exception {
        Employee employee = new Employee(3L, Department.ORTHOPAEDIC, "Anna", Status.ON);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateDoctorStatus() throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        Long doctorId = employees.get(0).getEmployeeId();
        Status status = Status.OFF;

        mockMvc.perform(MockMvcRequestBuilders.patch("/employees/{doctorId}/status", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDoctorDepartment() throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        Long doctorId = employees.get(0).getEmployeeId();
        Department department = Department.PULMONARY;

        mockMvc.perform(MockMvcRequestBuilders.patch("/employees/{doctorId}/department", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk());
    }
}
