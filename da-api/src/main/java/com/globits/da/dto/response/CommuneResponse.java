package com.globits.da.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommuneResponse {

    private int id;
    private String name;
    private int status;
}
