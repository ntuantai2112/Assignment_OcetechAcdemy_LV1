package com.globits.da.exception;


import com.globits.da.dto.response.ApiResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class RestExceptionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // Bắt lỗi 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public Map<String, Object> notFoundException(NoHandlerFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("Error", "Not Found");
        response.put("Message", "The requested URL was not found on the server.");
        response.put("Path", ex.getRequestURL());
        return response;
    }


    //    Bat loi Exception Data, khi Insert trung du lieu
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflictData(Exception ex) {
        logger.info(ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "409");
        map.put("error", "Conflict Data");

        return map;
    }


    //    //    Bat loi Exception khi goi sai method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Map<String, String>> methodNotSupportedException(Exception ex) {
        logger.info(ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "405");
        map.put("error", "Method Not Allow");

        return ResponseEntity.badRequest().body(map);
    }


    //    //    Bat loi Exception khi goi submit du lieu khong dung
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequestHandler(Exception ex) {
        logger.info(ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "400");
        map.put("error", "Params are wrong types");

        return map;
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handleAppException(AppException e) {
        ApiResponse<String> response = new ApiResponse<>();
        ErrorCodeException errorCode = e.getErrorCode();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = EmployeeAppException.class)
    ResponseEntity<ApiResponse<String>> handleEmployeeAppException(EmployeeAppException e) {
        ApiResponse<String> response = new ApiResponse<>();
        EmployeeCodeException errorCode = e.getEmployeeCode();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<String>> handlingException(Exception e) {

        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.ERROR_CODE.getCode());
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidationException(MethodArgumentNotValidException e) {

        String enumKey = e.getBindingResult().getFieldError().getDefaultMessage();
        EmployeeCodeException errorCode = EmployeeCodeException.INVALID_KEY;

        try {
            errorCode = EmployeeCodeException.valueOf(enumKey);
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
        }

        ApiResponse response = new ApiResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(value = IllegalStateException.class)
    ResponseEntity<ApiResponse> handlingIllegalStateException(IllegalStateException e) {

        ApiResponse<String> response = new ApiResponse<>();
        EmployeeCodeException errorCode = EmployeeCodeException.IO_EXCEPTION;
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);

    }

}
