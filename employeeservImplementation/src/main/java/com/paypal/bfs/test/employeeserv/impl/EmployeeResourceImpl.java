package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public ResponseEntity<Employee> employeeGetById(String id) {

        Optional<EmployeeEntity> employeeData = employeeRepository.findById(Long.valueOf(id));
        if (employeeData.isPresent()) {
            // Transform Employee Entity to employee
            return new ResponseEntity<>(employeeData.get().toPOJOEmployee(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Employee> addEmployee(Employee e, @RequestHeader("idempotency-key") String idempotencyKey) {
        // Idempotency support in resource creation
        EmployeeEntity duplicateRequestCheck = employeeRepository.findByIdempotencyKey(idempotencyKey);
        if (duplicateRequestCheck != null)
            return new ResponseEntity<>(duplicateRequestCheck.toPOJOEmployee(), HttpStatus.OK);

        // Transform pojo employee received to employee entity
        EmployeeEntity employeeEntity = new EmployeeEntity(e, idempotencyKey);

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return new ResponseEntity<>(savedEmployeeEntity.toPOJOEmployee(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> allEmployees = new ArrayList<>();
        employeeRepository.findAll().forEach( (employeeEntity) -> {
            allEmployees.add(employeeEntity.toPOJOEmployee());
        });
        return new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
