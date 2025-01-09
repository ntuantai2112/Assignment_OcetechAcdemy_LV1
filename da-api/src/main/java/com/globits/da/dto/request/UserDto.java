package com.globits.da.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;
    private boolean active;
    private String email;
    private String password;
    private String username;
}
