package com.globits.da.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse {

    private int id;
    private String name;
    private int status;
    private List<CommuneResponse> communes;

}
