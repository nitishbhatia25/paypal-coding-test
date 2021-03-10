package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Employee> addEmployee(Employee e) {
        // Transform pojo employee received to employee entity
        EmployeeEntity employeeEntity = new EmployeeEntity(e);

        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

        return new ResponseEntity<>(savedEmployeeEntity.toPOJOEmployee(), HttpStatus.OK);
    }
}
