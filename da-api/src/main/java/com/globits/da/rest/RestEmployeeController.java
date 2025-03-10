package com.globits.da.rest;

import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.CodeConfig;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestEmployeeController {

    EmployeeService empService;


    // Câu 19 : Tạo api lấy tất cả employee
    @GetMapping("/get-all-employee")
    public ApiResponse<List<EmployeeResponse>> getAll() {

        return apiResponse(empService.getAllEmployee());
    }

    // Câu 20: Tạo api lấy tất cả employee theo điều kiện tìm kiếm EmployeeSearchDTO gửi lên
    @PostMapping("/search-employees")
    public ApiResponse<List<EmployeeResponse>> searchEmployees(@Valid @RequestBody EmployeeSearchDto employeeSearchDto) {
        List<EmployeeResponse> employees = empService.searchEmployees(employeeSearchDto);
        if (employees.isEmpty() || employees == null) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEES_NOT_FOUND);
        }
        return apiResponse(employees);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<ApiResponse<?>> createEmployee(@Valid @RequestBody EmployeeDTO request, BindingResult result) {


        ApiResponse<EmployeeResponse> response = new ApiResponse<>();
        response.setCode(CodeConfig.SUCCESS_CODE.getCode());
        response.setMessage(CodeConfig.SUCCESS_CODE.getMessage());
        response.setResult(empService.addEmployee(request));
        return ResponseEntity.ok(response);
    }

    // Cấu 21: Tạo api xóa 1 Employee
    @DeleteMapping("/delete-employee/{id}")
    public ApiResponse<String> deleteEmployee(@PathVariable("id") Integer id) {

        return apiResponse(empService.deleteEmployee(id));

    }

    @PutMapping("/update-employee/{empId}")
    public EmployeeResponse updateEmployee(@PathVariable("empId") Integer id, @Valid @RequestBody EmployeeDTO request) {
        return empService.updateEmployee(id, request);
    }


    // Câu 22: Tạo API xuất file excel:
    @GetMapping("/export-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        empService.exportExcel(response);
    }

    // Câu 32: Import Excel
    @PostMapping("/import-excel")
    public ApiResponse<String> importEmployees(@RequestParam("file") MultipartFile file) {
        ApiResponse apiResponse = new ApiResponse<>();
        try {
            empService.importExcelEmployee(file);
            CodeConfig codeConfig = CodeConfig.SUCCESS_CODE;
            apiResponse.setCode(codeConfig.getCode());
            apiResponse.setMessage(codeConfig.getMessage());
            apiResponse.setResult("Import File Excel Successfully!");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return apiResponse;

    }


    private <T> ApiResponse<T> apiResponse(T result) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        CodeConfig codeConfig = CodeConfig.SUCCESS_CODE;
        apiResponse.setCode(codeConfig.getCode());
        apiResponse.setMessage(codeConfig.getMessage());
        apiResponse.setResult(result);
        return apiResponse;
    }


}
