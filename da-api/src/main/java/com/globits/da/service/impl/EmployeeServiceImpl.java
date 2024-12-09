package com.globits.da.service.impl;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.exception.ValidationException;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ExcelUtil;
import com.globits.da.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepo.findAll();
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<Employee> getEmpolyeeById(Integer id) {
        return employeeRepo.searchEmployeeById(id);
    }

    @Override
    public List<Employee> getEmpolyeeByCode(String code) {
        return employeeRepo.searchEmployeeByCode(code);
    }

    @Override
    public List<Employee> getEmpolyeeByName(String name) {
        return employeeRepo.searchEmployeeByCode(name);
    }

    @Override
    public Employee addEmployee(EmployeeDto request) {

        validateEmployee(request);


        Employee employee = employeeMapper.toEmployee(request);

        return employeeRepo.save(employee);
    }

    @Override
    public String deleteEmployee(Integer id) {
        Employee emp = employeeRepo.findById(id).get();
        if (emp != null) {
            employeeRepo.delete(emp);
            return "Delete employee successfully";
        }

        return "Employee not exit";
    }

    @Override
    public Employee updateEmployee(Integer id, EmployeeDto request) {

        Employee employee = employeeRepo.findById(id).get();
        employeeMapper.updateEmployee(employee,request);

        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> searchEmployees(EmployeeSearchDto employeeSearchDto) {
        return employeeRepo.searchEmployees(employeeSearchDto.getCode(), employeeSearchDto.getName(), employeeSearchDto.getName(), employeeSearchDto.getPhone());
    }

    @Override
    public ByteArrayInputStream getDataDowloadedExcel() throws IOException {
        List<Employee> employees = employeeRepo.findAll(); // Hoặc dữ liệu cần export
        if (employees == null || employees.isEmpty()) {
            throw new RuntimeException("No employees found for export");
        }
        return ExcelUtil.exportToExcel(employees);
    }

    @Override
    public void validateEmployee(EmployeeDto request) {
        List<ValidationException> errors = new ArrayList<>();

        // Validate Code employee
        if(request.getCode().trim() == null || request.getCode().trim().isEmpty()){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_NULL);
        }

        if(employeeRepo.findByCode(request.getCode().trim()).isPresent()){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_ALREADY_EXISTS);
        }

        String code = request.getCode().trim();

        if(code.length() < 6 ){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MIN_LENGTH);
        }

        if(code.length() > 10 ){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MAX_LENGTH);
        }

        // Validate Name employee
        if(request.getName().trim() == null || request.getName().trim().isEmpty()){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NAME_NOT_NULL);
        }

        // Validate email employee
        if(request.getEmail().trim() == null ||  request.getEmail().trim().isEmpty()){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_NOT_NULL);
        }

        if(!ValidationUtil.isValidEmail(request.getEmail())){
            throw  new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_FORMAT);
        }

        // Validate phone employee
        if(request.getPhone().trim() == null ||  request.getPhone().trim().isEmpty()){
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_NOT_NULL);
        }

        if(!ValidationUtil.isPhone(request.getPhone())){
            throw  new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_FORMAT);

        }

        // Validate age employee
        if(request.getAge() <= 0 || Integer.valueOf(request.getAge()) == null){
            throw  new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_VALUE);
        }



    }



    private EmployeeDto convertToDto(Employee employee) {

        EmployeeDto empDto = new EmployeeDto();
        empDto.setCode(employee.getCode());
        empDto.setName(employee.getName());
        empDto.setEmail(employee.getEmail());
        empDto.setPhone(employee.getPhone());
        empDto.setAge(employee.getAge());


        return empDto;

    }


}
