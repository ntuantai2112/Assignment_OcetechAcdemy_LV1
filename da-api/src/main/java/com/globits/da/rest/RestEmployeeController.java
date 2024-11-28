package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ExcelUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
    public List<Employee> getAll(){
        return  empService.getAllEmployee();
    }

    @PostMapping("/search-employees")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestBody EmployeeSearchDto employeeSearchDto){
        List<Employee> employees =  empService.searchEmployees(employeeSearchDto);
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add-employee")
    public Employee createEmployee(@RequestBody EmployeeDto request){
      return  empService.addEmployee(request);
    }

    @DeleteMapping("/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id){

       return empService.delelteEmployee(id);

    }

    @PutMapping("/update-employee/{empId}")
    public Employee updateEmployee(@PathVariable("empId") Integer id ,@RequestBody EmployeeDto request){
        return  empService.updateEmployee(id,request);
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
