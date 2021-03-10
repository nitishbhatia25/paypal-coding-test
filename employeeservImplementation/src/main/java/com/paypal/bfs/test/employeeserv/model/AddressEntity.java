package com.paypal.bfs.test.employeeserv.model;

import com.paypal.bfs.test.employeeserv.api.model.Address;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String line1;
  private String line2;
  private String city;
  private String state;
  private String country;
  private int zipCode;
  @OneToOne
  @MapsId
  private EmployeeEntity employee;

  public AddressEntity() {
  }

  public AddressEntity(Address address) {
    this.line1 = address.getLine1();
    this.line2 = address.getLine2();
    this.city = address.getCity();
    this.state = address.getState();
    this.country = address.getCountry();
    this.zipCode = address.getZipCode();
  }

  public Address toPOJOAddress() {
    Address pojoAddress = new Address();
    pojoAddress.setLine1(this.line1);
    pojoAddress.setLine2(this.line2);
    pojoAddress.setCity(this.city);
    pojoAddress.setState(this.state);
    pojoAddress.setCountry(this.country);
    pojoAddress.setZipCode(this.zipCode);
    return pojoAddress;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLine1() {
    return line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getLine2() {
    return line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public int getZipCode() {
    return zipCode;
  }

  public void setZipCode(int zipCode) {
    this.zipCode = zipCode;
  }

  public EmployeeEntity getEmployee() {
    return employee;
  }

  public void setEmployee(EmployeeEntity employee) {
    this.employee = employee;
  }

  @Override
  public String toString() {
    return "AddressEntity{" +
            "id=" + id +
            ", line1='" + line1 + '\'' +
            ", line2='" + line2 + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", country='" + country + '\'' +
            ", zipCode=" + zipCode +
            '}';
  }
}
