package com.globits.da.domain;

import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user", schema = "lv2")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "just_created", nullable = false)
    private boolean justCreated;

    @Column(name = "last_login_failures")
    private Long lastLoginFailures;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "total_login_failures")
    private Long totalLoginFailures;

    @Column(name = "username", nullable = false, length = 100)
    private String username;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;
}