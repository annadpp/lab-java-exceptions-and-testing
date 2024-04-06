package com.ironhack.demo.repository;
import com.ironhack.demo.enums.Department;
import com.ironhack.demo.enums.Status;
import com.ironhack.demo.model.Employee;
import com.ironhack.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

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
    void testFindAllByStatus() {
                List<Employee> employeesWithStatusON = employeeRepository.findAllByStatus(Status.ON);
        assertEquals(1, employeesWithStatusON.size());

        List<Employee> employeesWithStatusOFF = employeeRepository.findAllByStatus(Status.OFF);
        assertEquals(1, employeesWithStatusOFF.size());
    }

    @Test
    void testFindAllByDepartment() {
       List<Employee> employeesInCardiologyDepartment = employeeRepository.findAllByDepartment(Department.CARDIOLOGY);
        assertEquals(2, employeesInCardiologyDepartment.size());

        List<Employee> employeesInImmunologyDepartment = employeeRepository.findAllByDepartment(Department.IMMUNOLOGY);
        assertEquals(0, employeesInImmunologyDepartment.size());
    }
}

