package com.ironhack.demo.service;

import com.ironhack.demo.enums.Department;
import com.ironhack.demo.enums.Status;
import com.ironhack.demo.model.Employee;
import com.ironhack.demo.repository.EmployeeRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllDoctors(){
        return employeeRepository.findAll();
    }

    public Employee getDoctorById(Long employeeId){
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with employee ID: " + employeeId));
    }

    public List<Employee> getDoctorsByStatus(Status status){
        return employeeRepository.findAllByStatus(status);
    }

    public List<Employee> getDoctorsByDepartment(Department department){
        return employeeRepository.findAllByDepartment(department);
    }

    public void addDoctor(Employee employee){
        employeeRepository.save(employee);
    }

    public void updateDoctorStatus(Long employeeId, Status status){
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            employee.get().setStatus(status);
            employeeRepository.save(employee.get());
        }
    }

    public void updateDoctorDepartment(Long employeeId, Department department) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            employee.get().setDepartment(department);
            employeeRepository.save(employee.get());
        }
    }
}
