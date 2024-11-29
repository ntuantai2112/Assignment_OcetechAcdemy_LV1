package com.globits.da.mapper;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    Employee toEmployee(EmployeeDto employeeDto);

    EmployeeDto toEmployeeDto(Employee employee);

    void updateEmployee(@MappingTarget Employee employee, EmployeeDto empDto);


}
