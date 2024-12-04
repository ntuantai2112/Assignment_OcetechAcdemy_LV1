package com.globits.da.service;

import com.globits.da.domain.Province;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ProvinceResponse;

import java.util.List;

public interface ProvinceService {

    List<ProvinceResponse> getAllProvince();


    List<ProvinceResponse>  getProvinceByName(String name);

    ProvinceResponse  getProvinceById(Integer id);

    ProvinceResponse addProvince(ProvinceDto provinceDto);

    String deleteProvince(Integer id);

    ProvinceResponse updateProvince(Integer id,ProvinceDto provinceDto);


    ProvinceResponse createProvinceAndDistricts(ProvinceDto request);








}
