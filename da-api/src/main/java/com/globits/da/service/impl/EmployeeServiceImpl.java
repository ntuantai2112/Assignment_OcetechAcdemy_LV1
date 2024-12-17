package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.Employee;
import com.globits.da.dto.request.EmployeeDto;
import com.globits.da.dto.response.CommuneResponse;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.exception.ValidationException;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ExcelUtil;
import com.globits.da.utils.ValidationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepo;
    EmployeeMapper employeeMapper;
    CommuneRepository communeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployee() {
        List<EmployeeResponse> employees = employeeRepo.findAll().stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }


    @Override
    public List<EmployeeResponse> getEmpolyeeById(Integer id) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeById(id).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public List<EmployeeResponse> getEmpolyeeByCode(String code) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeByCode(code).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public List<EmployeeResponse> getEmpolyeeByName(String name) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeByCode(name).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public EmployeeResponse addEmployee(EmployeeDto request) {

        validateEmployee(request);


        Commune commune = communeRepository.findByName(request.getName()).
                orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_FOUND));

        if (commune.getDistrict() == null || commune.getDistrict().getProvince() == null) {
            throw new EmployeeAppException(EmployeeCodeException.DISTRICT_OR_PROVINCE_NOT_FOUND);
        }

        Employee employee = employeeMapper.toEmployee(request);
        employee.setCommune(commune);

        return employeeMapper.toEmployeeResponse(employeeRepo.save(employee));
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
    public EmployeeResponse updateEmployee(Integer id, EmployeeDto request) {

        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NOT_FOUND));
        employeeMapper.updateEmployee(employee, request);

        if(Integer.valueOf(request.getCommuneId()) != null){
            Commune commune = communeRepository.findByName(request.getName()).
                    orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_FOUND));

            if (commune.getDistrict() == null || commune.getDistrict().getProvince() == null) {
                throw new EmployeeAppException(EmployeeCodeException.DISTRICT_OR_PROVINCE_NOT_FOUND);
            }

            employee.setCommune(commune);
        }


        return employeeMapper.toEmployeeResponse(employeeRepo.save(employee));
    }

    @Override
    public List<EmployeeResponse> searchEmployees(EmployeeSearchDto employeeSearchDto) {

        List<EmployeeResponse> employees = employeeRepo.searchEmployees(employeeSearchDto.getCode(), employeeSearchDto.getName(), employeeSearchDto.getName(), employeeSearchDto.getPhone()).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;

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
        if (request.getCode().trim() == null || request.getCode().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_NULL);
        }

        if (employeeRepo.findByCode(request.getCode().trim()).isPresent()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_ALREADY_EXISTS);
        }

        String code = request.getCode().trim();

        if (code.length() < 6) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MIN_LENGTH);
        }

        if (code.length() > 10) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MAX_LENGTH);
        }

        // Validate Name employee
        if (request.getName().trim() == null || request.getName().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NAME_NOT_NULL);
        }

        // Validate email employee
        if (request.getEmail().trim() == null || request.getEmail().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_NOT_NULL);
        }

        if (!ValidationUtil.isValidEmail(request.getEmail())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_FORMAT);
        }

        // Validate phone employee
        if (request.getPhone().trim() == null || request.getPhone().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_NOT_NULL);
        }

        if (!ValidationUtil.isPhone(request.getPhone())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_FORMAT);

        }

        // Validate age employee
        if (request.getAge() <= 0 || Integer.valueOf(request.getAge()) == null) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_VALUE);
        }


    }


}
