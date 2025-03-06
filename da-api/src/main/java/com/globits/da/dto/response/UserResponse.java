package com.globits.da.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long id;
    private boolean active;
    private String email;
    private String password;
    private String username;

}
