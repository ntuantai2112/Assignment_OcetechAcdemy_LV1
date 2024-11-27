package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestEmployeeController {

    @Autowired
    private EmployeeService empService;


    @GetMapping("/get-all-employee")
    public List<Employee> getAll(){
        return  empService.getAllEmployee();
    }

    // Test Lại phương thức này theo nhiều trường hợp
    @GetMapping("/search-employees")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestBody EmployeeSearchDto employeeSearchDto){
        List<Employee> employees =  empService.searchEmployees(employeeSearchDto);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add-employee")
    public Employee createEmployee(@RequestBody EmployeeDto request){
      return  empService.addEmployee(request);
    }

    @DeleteMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id){

       return empService.delelteEmployee(id);

    }

    @PutMapping("/update-employee/{empId}")
    public Employee updateEmployee(@PathVariable("empId") Integer id ,@RequestBody EmployeeDto request){
        return  empService.updateEmployee(id,request);
    }




}
