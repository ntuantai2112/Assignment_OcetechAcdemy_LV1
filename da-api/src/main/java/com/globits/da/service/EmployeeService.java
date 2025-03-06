package com.globits.da.service;

import com.globits.da.dto.request.EmployeeDto;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployee();


    List<EmployeeResponse> getEmpolyeeById(Integer id);

    List<EmployeeResponse> getEmpolyeeByCode(String code);

    List<EmployeeResponse> getEmpolyeeByName(String name);

    EmployeeResponse addEmployee(EmployeeDto employeeDto);

    String deleteEmployee(Integer id);

    EmployeeResponse updateEmployee(Integer id,EmployeeDto employeeDto);


    List<EmployeeResponse> searchEmployees(EmployeeSearchDto employeeSearchDto);

    ByteArrayInputStream getDataDowloadedExcel() throws IOException;


    void validateEmployee(EmployeeDto request);


    void importEmployee(MultipartFile file);




}
