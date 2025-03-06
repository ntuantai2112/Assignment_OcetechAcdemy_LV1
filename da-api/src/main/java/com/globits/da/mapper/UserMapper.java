package com.globits.da.mapper;

import com.globits.da.domain.UserEntity;
import com.globits.da.dto.request.UserDto;
import com.globits.da.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserEntity toUser(UserDto request);


    UserResponse toUserResponse(UserEntity user);


}
