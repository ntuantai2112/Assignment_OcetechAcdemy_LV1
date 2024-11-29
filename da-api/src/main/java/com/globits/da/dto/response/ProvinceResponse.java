package com.globits.da.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {

    private int id;
    private String name;
    private int status;
}
