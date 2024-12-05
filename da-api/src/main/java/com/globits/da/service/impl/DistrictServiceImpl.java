package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CrudService;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DistrictServiceImpl implements DistrictService {


    @Autowired
    private ProvinceRepository provinceRepo;


    @Autowired
    private DistrictRepository repository;

    @Autowired
    private DistrictMapper mapper;

    @Autowired
    private CommuneMapper communeMapper;


    @Override
    public List<DistrictResponse> getAllDistrict() {
        List<District> districts = repository.findAll();
        List<DistrictResponse> districtResponses = new ArrayList<>();
        for (District district : districts) {
            districtResponses.add(mapper.toDistrictReponse(district));
        }
        return districtResponses;
    }

    @Override
    public List<DistrictResponse> getDistrictByName(String name) {
        List<District> districts = repository.findByNameQuery(name);
        List<DistrictResponse> districtResponses = new ArrayList<>();
        for (District district : districts) {
            districtResponses.add(mapper.toDistrictReponse(district));
        }
        return districtResponses;
    }

    @Override
    public DistrictResponse getDistrictById(Integer id) {
        District district = repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!"));
        return mapper.toDistrictReponse(district);
    }

    // Câu 24: Thêm huyện xác định luôn huyện đó thuộc tỉnh nào, Cách 1 sử dụng tên huyện lấy ra đối tượng province
    @Override
    public DistrictResponse addDistrict(DistrictDto request) {
        Province province = provinceRepo.findProvinceByNameQuery(request.getProvinceName());
        District district = mapper.toDistrict(request);
        district.setProvince(province);
        return mapper.toDistrictReponse(repository.save(district));
    }

    // Câu 24: Thêm huyện xác định luôn huyện đó thuộc tỉnh nào, sử dụng Id truyền vào từ đường dẫn
    @Override
    public DistrictResponse addDistrict(Integer provinceId, DistrictDto request) {

        Province province = provinceRepo.findById(provinceId).
                orElseThrow(() -> new RuntimeException("Province not found!"));
        District district = mapper.toDistrict(request);
        district.setProvince(province);
        return mapper.toDistrictReponse(repository.save(district));
    }

    @Override
    public String deleteDistrict(Integer id) {
        District district = repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!"));
        repository.delete(district);
        return "Delete Successfully!";
    }

    @Override
    public DistrictResponse updateDistrict(Integer id, DistrictDto request) {
        District district = repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!"));
        mapper.updateDistrict(district, request);
        return mapper.toDistrictReponse(repository.save(district));
    }

    @Override
    public List<DistrictResponse> findDistrictsByProvinceId(Integer provinceId) {
        List<District> districts = repository.findByProvinceId(provinceId);
        List<DistrictResponse> districtResponses = new ArrayList<>();
        for (District district : districts) {
            districtResponses.add(mapper.toDistrictReponse(district));
        }
        return districtResponses;
    }

    @Override
//    public DistrictResponse createDistrictAndCommune(Integer provinceId,DistrictDto districtRequest) {
//        Province province = provinceRepo.findById(provinceId)
//                .orElseThrow(() -> new RuntimeException("Province Not Found!"));
//
//        District district = mapper.toDistrict(districtRequest);
//        district.setProvince(province);
//        List<Commune> communes = districtRequest.getCommunes().stream()
//                .map(communeRequest -> {
//                    Commune commune = communeMapper.toCommune(communeRequest);
//                    commune.setDistrict(district);
//                    return commune;
//                })
//                .collect(Collectors.toList());
//
//        district.setCommunes(communes);
//
//        return mapper.toDistrictReponse(repository.save(district));
//    }

    public DistrictResponse createDistrictAndCommune(DistrictDto districtRequest) {
        Province province = provinceRepo.findProvinceByNameQuery(districtRequest.getProvinceName());
        if(province == null){
            new RuntimeException("Province Not Found!");
        }

        District district = mapper.toDistrict(districtRequest);
        district.setProvince(province);
        List<Commune> communes = districtRequest.getCommunes().stream()
                .map(communeRequest -> {
                    Commune commune = communeMapper.toCommune(communeRequest);
                    commune.setDistrict(district);
                    return commune;
                })
                .collect(Collectors.toList());

        district.setCommunes(communes);

        return mapper.toDistrictReponse(repository.save(district));
    }
}
