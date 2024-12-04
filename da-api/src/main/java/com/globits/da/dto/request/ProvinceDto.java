package com.globits.da.dto.request;

import com.globits.da.domain.District;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto {

    private String name;
    private int status;
    private List<DistrictDto> districts;


}
