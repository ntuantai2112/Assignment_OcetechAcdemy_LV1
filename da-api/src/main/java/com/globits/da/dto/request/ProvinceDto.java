package com.globits.da.dto.request;

import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto {

    private String name;
    private int status;


}
