package com.globits.da.service;

import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployee();


    List<EmployeeResponse> getEmpolyeeById(Integer id);

    List<EmployeeResponse> getEmpolyeeByCode(String code);

    List<EmployeeResponse> getEmpolyeeByName(String name);

    EmployeeResponse addEmployee(EmployeeDTO employeeDto);

    String deleteEmployee(Integer id);

    EmployeeResponse updateEmployee(Integer id, EmployeeDTO employeeDto);


    List<EmployeeResponse> searchEmployees(EmployeeSearchDto employeeSearchDto);


    void exportExcel(HttpServletResponse httpServletResponse) throws IOException;

    void validateEmployee(EmployeeDTO request);


    void importExcelEmployee(MultipartFile file) throws IOException;


}
