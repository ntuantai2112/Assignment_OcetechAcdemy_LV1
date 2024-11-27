package com.globits.da.service.impl;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepo.findAll();
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

        Employee employee = new Employee();
        employee.setCode(request.getCode());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAge(request.getAge());

        return  employeeRepo.save(employee);
    }

    @Override
    public String delelteEmployee(Integer id) {
            Employee emp = employeeRepo.findById(id).get();
            if(emp != null){
                employeeRepo.delete(emp);
                return "Delete employee successfully";
            }

            return "Employee not exit";
    }

    @Override
    public Employee updateEmployee(Integer id, EmployeeDto request) {

        Employee employee = employeeRepo.findById(id).get();
        employee.setCode(request.getCode());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAge(request.getAge());

        return employeeRepo.save(employee);
    }

    @Override
    public List<Employee> searchEmployees(EmployeeSearchDto employeeSearchDto) {
        return  employeeRepo.searchEmployees(employeeSearchDto.getCode(), employeeSearchDto.getName(), employeeSearchDto.getName(),employeeSearchDto.getPhone() );
    }
}
