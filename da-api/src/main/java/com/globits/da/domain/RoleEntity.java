package com.globits.da.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_role", schema = "lv2")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modify_date")
    private Timestamp modifyDate;

    @Column(name = "role_description", length = 512)
    private String roleDescription;

    @Column(name = "role_name", nullable = false, length = 150)
    private String roleName;


    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

}