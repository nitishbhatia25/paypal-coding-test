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

  public ResponseEntity<Employee> getEmployee(String id) {
    Optional<EmployeeEntity> employeeData = employeeRepository.findById(Long.valueOf(id));
    if (employeeData.isPresent()) {
      // Transform Employee Entity to employee
      return new ResponseEntity<>(employeeData.get().toPOJOEmployee(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  public ResponseEntity<Employee> addEmployee(Employee e, String idempotencyKey) {
    // Idempotency support in resource creation
    EmployeeEntity duplicateRequestCheck = employeeRepository.findByIdempotencyKey(idempotencyKey);
    if (duplicateRequestCheck != null)
      return new ResponseEntity<>(duplicateRequestCheck.toPOJOEmployee(), HttpStatus.OK);

    // Transform pojo employee received to employee entity
    EmployeeEntity employeeEntity = new EmployeeEntity(e, idempotencyKey);

    EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

    return new ResponseEntity<>(savedEmployeeEntity.toPOJOEmployee(), HttpStatus.OK);
  }

  public ResponseEntity<List<Employee>> getAllEmployees() {
    List<Employee> allEmployees = new ArrayList<>();
    employeeRepository.findAll().forEach( (employeeEntity) -> {
      allEmployees.add(employeeEntity.toPOJOEmployee());
    });
    return new ResponseEntity<>(allEmployees, HttpStatus.OK);
  }

  public ResponseEntity<Employee> deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
