package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;
import com.sun.javafx.util.Logging;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        EmployeeEntity employee = employeeService.getEmployee(id);
        if (employee == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(employee.toPOJOEmployee(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> addEmployee(Employee e, @RequestHeader("idempotency-key") String idempotencyKey) {
        EmployeeEntity employee = employeeService.checkDuplicateRequest(idempotencyKey);
        if (employee == null)
            employee = employeeService.addEmployee(e, idempotencyKey);

        return new ResponseEntity<>(employee.toPOJOEmployee(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> allEmployees = new ArrayList<>();
        List<EmployeeEntity> allEmployeesEntity = employeeService.getAllEmployees();
        allEmployeesEntity.forEach((employeeEntity) -> {
            allEmployees.add(employeeEntity.toPOJOEmployee());
        });
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> deleteEmployeeById(Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
