package com.globits.da.dto.request;

import com.globits.da.domain.Province;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {

    private Integer id;
    private String name;
    private int status;
    private Integer provinceId;
    private String provinceName;
    private List<CommuneDto> communes;




}
