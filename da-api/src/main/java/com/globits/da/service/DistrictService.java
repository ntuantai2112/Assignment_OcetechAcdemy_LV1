package com.globits.da.service;

import com.globits.da.domain.District;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.DistrictResponse;

import java.util.List;

public interface DistrictService {

    List<DistrictResponse> getAllDistrict();


    List<DistrictResponse> getDistrictByName(String name);

    DistrictResponse getDistrictById(Integer id);

    DistrictResponse addDistrict(DistrictDto request);

    DistrictResponse addDistrict(Integer provinceId,DistrictDto request);

    String deleteDistrict(Integer id);

    DistrictResponse updateDistrict(Integer id,DistrictDto request);

    List<DistrictResponse> findDistrictsByProvinceId(Integer provinceId);

//    DistrictResponse createDistrictAndCommune(Integer provinceId,DistrictDto request);

    DistrictResponse createDistrictAndCommune(DistrictDto request);









}
