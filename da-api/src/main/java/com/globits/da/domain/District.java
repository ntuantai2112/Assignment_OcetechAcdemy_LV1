package com.globits.da.domain;

import com.globits.da.dto.request.CommuneDto;
import lombok.*;
import org.joda.time.DateTime;
import org.mapstruct.Mapper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_district")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private int status;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime  updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id",nullable = false)
    private Province province;

    @OneToMany(mappedBy = "district",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Commune> communes = new ArrayList<>();


}
