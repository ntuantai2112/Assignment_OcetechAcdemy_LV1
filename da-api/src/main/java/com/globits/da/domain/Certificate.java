package com.globits.da.domain;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_certificate")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "valid_form")
    Date validForm;
    @Column(name = "valid_until")
    Date validUntil;
    @Column(name = "status")
    int status;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime  updatedAt = LocalDateTime.now();


}
