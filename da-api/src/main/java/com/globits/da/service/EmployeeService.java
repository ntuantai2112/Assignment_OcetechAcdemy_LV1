package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.springframework.data.jpa.repository.Query;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployee();

    List<EmployeeDto> getAllEmployees();

    List<Employee> getEmpolyeeById(Integer id);

    List<Employee> getEmpolyeeByCode(String code);

    List<Employee> getEmpolyeeByName(String name);

    Employee addEmployee(EmployeeDto employeeDto);

    String deleteEmployee(Integer id);

    Employee updateEmployee(Integer id,EmployeeDto employeeDto);


    List<Employee> searchEmployees(EmployeeSearchDto employeeSearchDto);

    ByteArrayInputStream getDataDowloadedExcel() throws IOException;





}
