package com.paypal.bfs.test.employeeserv.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.paypal.bfs.test.employeeserv.api.model.Employee;

import java.util.List;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @RequestMapping(value = "/v1/bfs/employees/{id}", method = RequestMethod.GET)
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id);

    // ----------------------------------------------------------
    // TODO - add a new operation for creating employee resource.
    // ----------------------------------------------------------
    @RequestMapping(value = "/v1/bfs/employees/create", method = RequestMethod.POST)
    ResponseEntity<Employee> addEmployee(@RequestBody Employee e, @RequestHeader("idempotency-key") String idempotencyKey);

    @RequestMapping(value = "/v1/bfs/employees/all", method = RequestMethod.GET)
    ResponseEntity<List<Employee>> getAllEmployees();

    @RequestMapping(value = "/v1/bfs/employees/{id}", method = RequestMethod.DELETE)
    ResponseEntity<Employee> deleteEmployeeById(@PathVariable("id") Long id);
}
