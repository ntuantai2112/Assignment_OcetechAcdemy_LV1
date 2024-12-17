package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.request.EmployeeDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestEmployeeController {

    @Autowired
    private EmployeeService empService;


    @GetMapping("/get-all-employee")
    public List<EmployeeResponse> getAll() {

        return empService.getAllEmployee();
    }

    @PostMapping("/search-employees")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(@RequestBody EmployeeSearchDto employeeSearchDto) {
        List<EmployeeResponse> employees = empService.searchEmployees(employeeSearchDto);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<ApiResponse<?>> createEmployee(@RequestBody EmployeeDto request) {

        ApiResponse<EmployeeResponse> response = new ApiResponse<>();
        response.setResult(empService.addEmployee(request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {

        return empService.deleteEmployee(id);

    }

    @PutMapping("/update-employee/{empId}")
    public EmployeeResponse updateEmployee(@PathVariable("empId") Integer id, @RequestBody EmployeeDto request) {
        return empService.updateEmployee(id, request);
    }


    // Câu 22: Tạo API xuất file excel:
    @GetMapping("/export-excel")
    public void exportExcelEmployee(HttpServletResponse response) throws IOException {
        // Thiết lập header cho response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        // Gọi service để lấy dữ liệu Excel
        ByteArrayInputStream inputStream = empService.getDataDowloadedExcel();

        // Ghi dữ liệu từ ByteArrayInputStream vào response output stream
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi export file Excel", e);
        }
    }


}
