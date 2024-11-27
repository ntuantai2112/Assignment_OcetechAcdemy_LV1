package com.globits.da.exception;


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
    public Map<String,Object> notFoundException(NoHandlerFoundException ex){
        Map<String, Object> response = new HashMap<>();
            response.put("Error","Not Found");
            response.put("Message","The requested URL was not found on the server.");
            response.put("Path",ex.getRequestURL());
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
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequestHandler(Exception ex) {
        logger.info(ex.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("code", "400");
        map.put("error", "Params are wrong types");

        return map;
    }


}
