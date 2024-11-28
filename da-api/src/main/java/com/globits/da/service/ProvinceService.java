package com.globits.da.service;

import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.search.EmployeeSearchDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface ProvinceService {

    List<Province> getAllProvince();


    List<Province> getProvinceByName(String name);

    Province addProvince(ProvinceDto provinceDto);

    String delelteProvince(Integer id);

    Province updateProvince(Integer id,ProvinceDto provinceDto);








}
