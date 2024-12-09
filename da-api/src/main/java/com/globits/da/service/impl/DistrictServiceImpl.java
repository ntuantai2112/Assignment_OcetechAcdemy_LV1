package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.exception.AppException;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.repository.CommuneRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DistrictServiceImpl implements DistrictService {


    @Autowired
    private ProvinceRepository provinceRepo;


    @Autowired
    private DistrictRepository repository;


    @Autowired
    private CommuneRepository communeRepo;

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
        District district = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.DISTRICT_NOT_FOUND));
        return mapper.toDistrictReponse(district);
    }

    // Câu 24: Thêm huyện xác định luôn huyện đó thuộc tỉnh nào, Cách 1 sử dụng tên huyện lấy ra đối tượng province
    @Override
    public DistrictResponse addDistrict(DistrictDto request) {
        Province province = provinceRepo.findProvinceByNameQuery(request.getProvinceName());
        if(province == null){
            throw new IllegalArgumentException("Province with name '" + request.getProvinceName() + "' does not exist!");
        }

        Optional<District> existsDistrict = repository.findByNameAndProvinceId(request.getName(), province.getId());
        if(existsDistrict.isPresent()){
            throw new AppException(ErrorCodeException.DISTRICT_NAME_EXISTS);
        }

        District district = mapper.toDistrict(request);
        district.setProvince(province);

        return mapper.toDistrictReponse(repository.save(district));

    }

    // Câu 24: Thêm huyện xác định luôn huyện đó thuộc tỉnh nào, sử dụng Id truyền vào từ đường dẫn
    @Override
    public DistrictResponse addDistrict(Integer provinceId, DistrictDto request) {

        Province province = provinceRepo.findById(provinceId).
                orElseThrow(() -> new AppException(ErrorCodeException.PROVINCE_NOT_FOUND));
        District district = mapper.toDistrict(request);
        district.setProvince(province);
        return mapper.toDistrictReponse(repository.save(district));
    }

    @Override
    public String deleteDistrict(Integer id) {
        District district = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.DISTRICT_NOT_FOUND));
        repository.delete(district);
        return "Delete Successfully!";
    }

    // Cập Nhật Huyện và danh sách xã đồng thời cùng 1 lúc, với các giá trị đã có trong DB
    @Override
    public DistrictResponse updateDistrict(Integer id, DistrictDto request) {
        District district = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.DISTRICT_NOT_FOUND));
        mapper.updateDistrict(district, request);
        for (CommuneDto communeDto : request.getCommunes()){
            if(communeDto.getId() != null){
                Commune commune = district.getCommunes().stream()
                        .filter( c -> c.getId() == communeDto.getId())
                        .findFirst()
                        .orElseThrow( () -> new AppException(ErrorCodeException.COMMUNE_NOT_FOUND));
                commune.setDistrict(district);
                communeMapper.updateCommune(commune,communeDto);

            }
        }

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
    public DistrictResponse createDistrictAndCommune(DistrictDto districtRequest) {
        Province province = provinceRepo.findProvinceByNameQuery(districtRequest.getProvinceName());
        if(province == null){
            new AppException(ErrorCodeException.PROVINCE_NOT_FOUND);
        }
        Optional<District> existsDistrict = repository.findByNameAndProvinceId(districtRequest.getName(), province.getId());
        if(existsDistrict.isPresent()){
            throw new AppException(ErrorCodeException.DISTRICT_NAME_EXISTS);
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

    @Override
    public DistrictResponse createDistrictAndCRUDCommune(Integer districtId, DistrictDto districtDto) {
        Optional<District> existsDistrict = repository.findByNameAndProvinceId(districtDto.getName(), districtId);
        if(existsDistrict.isPresent()){
            throw new AppException(ErrorCodeException.DISTRICT_NOT_FOUND);
        }
        District district = repository.findById(districtId)
                .orElseThrow(() -> new AppException(ErrorCodeException.DISTRICT_NOT_FOUND));

        mapper.updateDistrict(district,districtDto);

        List<Integer> communeIds = districtDto.getCommunes().stream()
                .filter( c -> c.getId() != null)
                .map(CommuneDto::getId)
                .collect(Collectors.toList());


        List<Commune> communes = district.getCommunes().stream()
                .filter( c -> !communeIds.contains(c.getId()))
                .collect(Collectors.toList());

        district.getCommunes().removeAll(communes);


        for (CommuneDto communeRequest : districtDto.getCommunes() ){

            if(communeRequest.getId() != null){
                Commune commune = district.getCommunes().stream()
                        .filter( c -> c.getId() == communeRequest.getId())
                        .findFirst()
                        .orElseThrow( () -> new AppException(ErrorCodeException.COMMUNE_NOT_FOUND));
                commune.setDistrict(district);
                communeMapper.updateCommune(commune,communeRequest);

            }
            else {
                Commune newCommune = new Commune();
                communeMapper.updateCommune(newCommune,communeRequest);
                newCommune.setDistrict(district);
                district.getCommunes().add(newCommune);
            }

        }

        return mapper.toDistrictReponse(repository.save(district));
    }
}
