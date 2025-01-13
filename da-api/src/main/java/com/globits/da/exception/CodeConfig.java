package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CodeConfig {
    SUCCESS_CODE(1000, "Success fully!");


    private int code;
    private String message;

}
