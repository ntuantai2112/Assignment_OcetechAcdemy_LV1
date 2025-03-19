package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CodeConfig {
    SUCCESS_CODE(200, "Success fully!"),
    ERROR_IMPORT_EXCEL(400, "Import failed! Invalid data!");

    private int code;
    private String message;

}
