package com.ironhack.demo.controller;

import com.ironhack.demo.enums.Department;
import com.ironhack.demo.enums.Status;
import com.ironhack.demo.model.Employee;
import com.ironhack.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getAllDoctors() {
        return employeeService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getDoctorById(@PathVariable Long id) {
        Employee doctor = employeeService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/department/{department}")
    public List<Employee> getDoctorsByDepartment(@PathVariable Department department) {
        return employeeService.getDoctorsByDepartment(department);
    }

    @GetMapping("/status/{status}")
    public List<Employee> getDoctorsByStatus(@PathVariable Status status) {
        return employeeService.getDoctorsByStatus(status);
    }

    @PostMapping("/employees")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addEmployee(@RequestBody Employee employee) {
        employeeService.addDoctor(employee);
    }

    @PatchMapping("/{employeeId}/status")
    public void updateDoctorStatus(@PathVariable Long employeeId, @RequestBody Status status) {
        employeeService.updateDoctorStatus(employeeId, status);
    }

    @PatchMapping("/{employeeId}/department")
    public void updateDoctorDepartment(@PathVariable Long employeeId, @RequestBody Department department) {
        employeeService.updateDoctorDepartment(employeeId, department);
    }
}

