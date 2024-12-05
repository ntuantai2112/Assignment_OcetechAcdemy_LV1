package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.mapper.ProvinceMapper;
import com.globits.da.repository.DistrictRepository;
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
    private DistrictRepository districtRepo;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private DistrictMapper districtMapper;


    @Override
    public List<ProvinceResponse> getAllProvince() {

        List<Province> provinces = provinceRepo.findAll();
        List<ProvinceResponse> provinceResponses = new ArrayList<>();
        for (Province province : provinces) {
            provinceResponses.add(provinceMapper.toProvinceResponse(province));
        }

        return provinceResponses;
    }


    @Override
    public List<ProvinceResponse> getProvinceByName(String name) {

        List<Province> provinces = provinceRepo.findByNameQuery(name);
        List<ProvinceResponse> provinceResponses = new ArrayList<>();
        for (Province province : provinces) {
            provinceResponses.add(provinceMapper.toProvinceResponse(province));
        }

        return provinceResponses;
    }

    @Override
    public ProvinceResponse getProvinceById(Integer id) {
        return provinceMapper.toProvinceResponse(provinceRepo.findById(id).orElseThrow(() -> new RuntimeException("Not Found!")));
    }

    @Override
    public ProvinceResponse addProvince(ProvinceDto provinceDto) {

        Province province = provinceMapper.toProvince(provinceDto);
        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }

    @Override
    public String deleteProvince(Integer id) {

        Province province = provinceRepo.findById(id).orElseThrow(() -> new RuntimeException("Provice not found"));
        if (province != null) {
            provinceRepo.delete(province);
            return "Delete Successfully!";
        }
        return "Not Found!";
    }

    @Override
    public ProvinceResponse updateProvince(Integer id, ProvinceDto provinceDto) {

        Province province = provinceRepo.findById(id).orElseThrow(() -> new RuntimeException("Provice not found"));
        provinceMapper.updateProvince(province, provinceDto);
        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }

    // Sử dụng Cascade trong Spring với All nó nó lưu tự động districts thông qua lớp cha là province.
    @Override
    @Transactional
    public ProvinceResponse createProvinceAndDistricts(ProvinceDto provinceRequest) {

        Province province = provinceMapper.toProvince(provinceRequest);

        List<District> newDistricts = provinceRequest.getDistricts().stream()
                .map(districtRequest -> {
                    District district = districtMapper.toDistrict(districtRequest);
                    district.setProvince(province);
                    return district;
                }).collect(Collectors.toList());

        province.setDistricts(newDistricts);

        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }

    @Override
    public ProvinceResponse updateProvinceAndDistrict(Integer provinceId, ProvinceDto request) {
        Province province = provinceRepo.findById(provinceId).orElseThrow(() -> new RuntimeException("Province not found"));

        provinceMapper.updateProvince(province, request);

        for (DistrictDto districtRequest : request.getDistricts()) {
            District district = districtRepo.findById(districtRequest.getId()).orElseThrow(() -> new RuntimeException("District not found"));
            districtMapper.updateDistrict(district, districtRequest);
        }
        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }

    @Override
    public ProvinceResponse updateProvinceAndCRUDDistrict(Integer provinceId, ProvinceDto provinceRequest){
        Province province = provinceRepo.findById(provinceId).orElseThrow(() -> new RuntimeException("Province not found!"));

        provinceMapper.updateProvince(province,provinceRequest);


        List<Integer> districtIds = province.getDistricts().stream()
                                    .filter( d -> Integer.valueOf(d.getId()) != null)
                                    .map(District::getId)
                                    .collect(Collectors.toList());

        List<District> districts = province.getDistricts().stream()
                                    .filter( d -> !districtIds.contains(d.getId()))
                                    .collect(Collectors.toList());

        province.getDistricts().removeAll(districts);


        for (DistrictDto districtRequest : provinceRequest.getDistricts() ){
            if(districtRequest.getId() != null){
                District district = province.getDistricts().stream()
                                    .filter(d -> d.getId() == districtRequest.getId())
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("District not found!"));
                district.setProvince(province);
                districtMapper.updateDistrict(district,districtRequest);

            }else {
                District newDistrict = new District();
                districtMapper.updateDistrict(newDistrict,districtRequest);
                newDistrict.setProvince(province);
                province.getDistricts().add(newDistrict);
            }
        }

        return provinceMapper.toProvinceResponse(provinceRepo.save(province));
    }
}
