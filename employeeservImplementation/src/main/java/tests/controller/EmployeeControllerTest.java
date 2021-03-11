package tests.controller;

import com.paypal.bfs.test.employeeserv.EmployeeservApplication;
import com.paypal.bfs.test.employeeserv.model.AddressEntity;
import com.paypal.bfs.test.employeeserv.model.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= EmployeeservApplication.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  EmployeeService employeeService;

  EmployeeEntity mockEmployeeEntity = new EmployeeEntity("firstName", "lastName", Date.valueOf("2021-02-02"), new AddressEntity(), "idempotency-key-1");

  @Test
  public void employeeGetById() throws Exception {
    Mockito.when(employeeService.getEmployee(Mockito.anyString())).thenReturn(mockEmployeeEntity);
    mockMvc.perform(get("/v1/bfs/employees/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("{\"id\":0,\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"date_of_birth\":\"2021-02-02\",\"address\":{\"zip_code\":0}}"));
  }

  @Test
  public void employeeGetByIdNotFound() throws Exception {
    Mockito.when(employeeService.getEmployee(Mockito.anyString())).thenReturn(null);
    mockMvc.perform(get("/v1/bfs/employees/1"))
            .andDo(print())
            .andExpect(status().isNotFound());
  }

  @Test
  public void addEmployee() throws Exception {
    String employeeJson = "{\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"date_of_birth\":\"2021-02-02\",\"address\":{\"zip_code\":0}}";
    Mockito.when(employeeService.addEmployee(Mockito.any(), Mockito.anyString())).thenReturn(mockEmployeeEntity);
    Mockito.when(employeeService.checkDuplicateRequest(Mockito.anyString())).thenReturn(null);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/bfs/employees/create")
            .accept(MediaType.APPLICATION_JSON)
            .content(employeeJson)
            .contentType(MediaType.APPLICATION_JSON)
            .header("idempotency-key", "123");
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void addEmployeeWithoutIdempotencyKey() throws Exception {
    String employeeJson = "{\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"date_of_birth\":\"2021-02-02\",\"address\":{\"zip_code\":0}}";
    Mockito.when(employeeService.addEmployee(Mockito.any(), Mockito.anyString())).thenReturn(mockEmployeeEntity);
    Mockito.when(employeeService.checkDuplicateRequest(Mockito.anyString())).thenReturn(null);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/bfs/employees/create")
            .accept(MediaType.APPLICATION_JSON)
            .content(employeeJson)
            .contentType(MediaType.APPLICATION_JSON);
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void addEmployeeIdempotencyKeyMatches() throws Exception {
    String employeeJson = "{\"first_name\":\"firstName\",\"last_name\":\"lastName\",\"date_of_birth\":\"2021-02-02\",\"address\":{\"zip_code\":0}}";
    Mockito.when(employeeService.addEmployee(Mockito.any(), Mockito.anyString())).thenReturn(mockEmployeeEntity);
    Mockito.when(employeeService.checkDuplicateRequest(Mockito.anyString())).thenReturn(mockEmployeeEntity);
    RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/bfs/employees/create")
            .accept(MediaType.APPLICATION_JSON)
            .content(employeeJson)
            .contentType(MediaType.APPLICATION_JSON)
            .header("idempotency-key", "123");
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    MockHttpServletResponse response = result.getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }
}
