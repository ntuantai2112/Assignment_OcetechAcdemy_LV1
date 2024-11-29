package com.globits.da.service.impl;

import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.mapper.ProvinceMapper;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {


    @Autowired
    private ProvinceRepository provinceRepo;

    @Autowired
    private ProvinceMapper provinceMapper;


    @Override
    public List<ProvinceResponse> getAllProvince() {

        List<Province> provinces = provinceRepo.findAll();
        List<ProvinceResponse> provinceResponses = new ArrayList<>();
        for (Province province : provinces){
            provinceResponses.add( provinceMapper.toProvinceResponse(province));
        }

        return provinceResponses;
    }



    @Override
    public List<ProvinceResponse> getProvinceByName(String name) {

        List<Province> provinces = provinceRepo.findByNameQuery(name);
        List<ProvinceResponse> provinceResponses = new ArrayList<>();
        for (Province province : provinces){
            provinceResponses.add( provinceMapper.toProvinceResponse(province));
        }

        return provinceResponses;
    }

    @Override
    public ProvinceResponse getProvinceById(Integer id) {
        return  provinceMapper.toProvinceResponse(provinceRepo.findById(id).orElseThrow(() -> new RuntimeException("Not Found!")));
    }

    @Override
    public ProvinceResponse addProvince(ProvinceDto provinceDto) {

        Province province = provinceMapper.toProvince(provinceDto);
        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }

    @Override
    public String deleteProvince(Integer id) {

        Province province = provinceRepo.findById(id).orElseThrow( () -> new RuntimeException("Provice not found"));
        if (province != null){
            provinceRepo.delete(province);
            return "Delete Successfully!";
        }
        return "Not Found!";
    }

    @Override
    public ProvinceResponse updateProvince(Integer id, ProvinceDto provinceDto) {

        Province province = provinceRepo.findById(id).orElseThrow( () -> new RuntimeException("Provice not found"));
        provinceMapper.updateProvince(province,provinceDto);
        return  provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }
}
