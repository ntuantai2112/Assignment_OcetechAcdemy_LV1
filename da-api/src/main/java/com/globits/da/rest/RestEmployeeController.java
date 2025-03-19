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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
//    @RestSta
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

    // Câu 28-29-30:Employee sẽ có thêm 3 trường: Tỉnh, Huyện, Xã
    // - Thêm mới employee phải có cả 3 trường này
    @PostMapping("/add-employee")
    public ResponseEntity<ApiResponse<?>> createEmployee(@Valid @RequestBody EmployeeDTO request) {
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

    // Câu 30:Sửa: Nếu gửi thông tin sửa của cả 3 trường mới sửa, không thì bỏ qua
    @PutMapping("/update-employee/{empId}")
    public ApiResponse<EmployeeResponse> updateEmployee(@PathVariable("empId") Integer id, @Valid @RequestBody EmployeeDTO request) {
        return apiResponse(empService.updateEmployee(id, request));
    }


    // Câu 22: Tạo API xuất file excel:
    @GetMapping("/export-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        empService.exportExcel(response);
    }

    // Câu 32: Import Excel
    @PostMapping("/import-excel")
    public ApiResponse<Map<String, Object>> importEmployees(@RequestParam("file") MultipartFile file) {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();

        try {
            Map<String, Object> response = empService.importExcelEmployee(file);
            boolean success = (boolean) response.get("success");
            if (!success) {
                apiResponse.setCode(CodeConfig.ERROR_IMPORT_EXCEL.getCode());
                apiResponse.setMessage("There is an error in the imported data, please check again..");
                apiResponse.setResult(response);
                return apiResponse;
            }

            // Import thành công
            apiResponse.setCode(CodeConfig.SUCCESS_CODE.getCode());
            apiResponse.setMessage("Import File Excel Successfully!");
            apiResponse.setResult(response);

        } catch (Exception e) {
            log.error("Error when importing Excel file: {}", e.getMessage());
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
