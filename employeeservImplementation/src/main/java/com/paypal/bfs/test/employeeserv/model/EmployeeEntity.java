package com.paypal.bfs.test.employeeserv.model;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "EMPLOYEE", indexes = @Index(columnList = "idempotencyKey"))
public class EmployeeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;
  @Column(nullable = false)
  private Date dateOfBirth;
  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private AddressEntity address;
  @Column(nullable=false, unique=true)
  private String idempotencyKey;

  public EmployeeEntity() {
  }

  public EmployeeEntity(String firstName, String lastName, Date dateOfBirth, AddressEntity address, String idempotencyKey) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.idempotencyKey = idempotencyKey;
  }

  public EmployeeEntity(Employee employee, String idempotencyKey) {
    // Persisting below comments for change in id behaviour in future
//    if (employee.getId() != null)
//      this.id = employee.getId();
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
    this.dateOfBirth = Date.valueOf(employee.getDateOfBirth());
    this.address = new AddressEntity(employee.getAddress());
    this.address.setEmployee(this);
    this.idempotencyKey = idempotencyKey;
  }

  public Employee toPOJOEmployee() {
    Employee pojoEmployee = new Employee();
    pojoEmployee.setId((int)this.id);
    pojoEmployee.setFirstName(this.firstName);
    pojoEmployee.setLastName(this.lastName);
    pojoEmployee.setDateOfBirth(this.dateOfBirth.toString());
    pojoEmployee.setAddress(this.address.toPOJOAddress());
    return pojoEmployee;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
    address.setEmployee(this);
  }

  public String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public String toString() {
    return "EmployeeEntity{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", address=" + address.toString() +
            ", idempotencyKey='" + idempotencyKey + '\'' +
            '}';
  }
}
