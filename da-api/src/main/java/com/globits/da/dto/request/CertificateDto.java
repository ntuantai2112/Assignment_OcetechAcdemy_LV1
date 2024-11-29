package com.globits.da.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTime;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateDto {
    int id;
    String name;
    Date validForm;
    Date validUntil;
    int status;
}
