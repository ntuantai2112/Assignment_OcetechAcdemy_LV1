package com.globits.da.mapper;

import com.globits.da.domain.Employee;
import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.dto.response.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    Employee toEmployee(EmployeeDTO employeeDto);

    @Mapping(source = "commune.name", target = "communeName")
    @Mapping(source = "district.name", target = "districtName")
    @Mapping(source = "province.name", target = "provinceName")
    EmployeeResponse toEmployeeResponse(Employee employee);

    void updateEmployee(@MappingTarget Employee employee, EmployeeDTO empDto);


}
