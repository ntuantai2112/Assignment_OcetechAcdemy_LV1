package com.globits.da.service.impl;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
