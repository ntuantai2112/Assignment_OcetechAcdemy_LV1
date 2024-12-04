package com.globits.da.dto.response;


import com.globits.da.dto.request.DistrictDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {

    private int id;
    private String name;
    private int status;
    private List<DistrictResponse> districts;
}
