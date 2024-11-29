package com.globits.da.mapper;

import com.globits.da.domain.District;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.response.DistrictResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DistrictMapper {


    District toDistrict(DistrictDto districtDto);

    DistrictResponse toDistrictReponse(District district);

    void updateDistrict(@MappingTarget District district, DistrictDto districtDto);


}
