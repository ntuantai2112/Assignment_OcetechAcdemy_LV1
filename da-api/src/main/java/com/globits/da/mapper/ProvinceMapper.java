package com.globits.da.mapper;

import com.globits.da.domain.Province;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ProvinceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProvinceMapper {


    Province toProvince(ProvinceDto provinceDto);

    // Ánh xạ Entity sang response
    @Mapping(target = "districts",source = "districts")// Map danh sách district
    ProvinceResponse toProvinceResponse(Province province);

    void updateProvince(@MappingTarget Province province, ProvinceDto provinceDto);





}
