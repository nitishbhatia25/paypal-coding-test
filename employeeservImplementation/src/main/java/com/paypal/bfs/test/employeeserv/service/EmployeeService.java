package com.paypal.bfs.test.employeeserv.service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
  @Autowired
  EmployeeRepository employeeRepository;

  public EmployeeEntity getEmployee(String id) {
    Optional<EmployeeEntity> employeeData = employeeRepository.findById(Long.valueOf(id));
    return employeeData.get();
  }

  public EmployeeEntity addEmployee(Employee e, String idempotencyKey) {
    // Transform pojo employee received to employee entity
    EmployeeEntity employeeEntity = new EmployeeEntity(e, idempotencyKey);

    EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

    return savedEmployeeEntity;
  }

  public List<EmployeeEntity> getAllEmployees() {
    List<EmployeeEntity> allEmployees = new ArrayList<>();
    employeeRepository.findAll().forEach( (employeeEntity) -> {
      allEmployees.add(employeeEntity);
    });
    return allEmployees;
  }

  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }

  public EmployeeEntity checkDuplicateRequest(String idempotencyKey) {
    // Idempotency support in resource creation
    EmployeeEntity employee = employeeRepository.findByIdempotencyKey(idempotencyKey);
    return employee;
  }
}
