package com.globits.da.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {

    private int id;
    private String name;
    Date validForm;
    Date validUntil;
    private int status;

}
