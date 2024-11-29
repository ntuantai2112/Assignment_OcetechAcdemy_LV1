package com.globits.da.mapper;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.request.CertificateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CertificateMapper {


    Certificate toCertificate(CertificateDto certificateDto);

    CertificateDto toCertificateDto(Certificate certificate);

    void updateCetificate(@MappingTarget Certificate certificate, CertificateDto certificateDto);


}
