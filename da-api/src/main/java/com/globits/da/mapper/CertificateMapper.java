package com.globits.da.mapper;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.request.CertificateDto;
import com.globits.da.dto.response.CertificateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    Certificate toCertificate(CertificateDto certificateDto);

    @Mapping(target = "employeeName", source = "employee.name")
    @Mapping(target = "provinceName", source = "province.name")
    CertificateResponse toCertificateResponse(Certificate certificate);

    void updateCertificate(@MappingTarget Certificate certificate, CertificateDto certificateDto);


}
