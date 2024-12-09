package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class AppException extends  RuntimeException{

    private ErrorCodeException errorCode;

    public AppException(ErrorCodeException errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
