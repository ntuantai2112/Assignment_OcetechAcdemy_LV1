package com.globits.da.mapper;

import com.globits.da.domain.Commune;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.response.CommuneResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommuneMapper {


    Commune toCommune(CommuneDto communeDto);

    CommuneResponse toCommuneResponse(Commune commune);

    void updateCommune(@MappingTarget Commune commune, CommuneDto communeDto);


}
