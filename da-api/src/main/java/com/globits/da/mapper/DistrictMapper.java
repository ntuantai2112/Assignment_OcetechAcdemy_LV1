package com.globits.da.mapper;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.response.DistrictResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DistrictMapper {

    @Mapping(target = "province", ignore = true)
    District toDistrict(DistrictDto districtDto);

    DistrictResponse toDistrictReponse(District district);

    void updateDistrict(@MappingTarget District district, DistrictDto districtDto);


}
