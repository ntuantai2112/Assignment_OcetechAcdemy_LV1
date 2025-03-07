package com.globits.da.utils;

import com.globits.da.domain.Employee;
import com.globits.da.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ExcelUtil {

    @Autowired
    private EmployeeMapper employeeMapper;

    public static String[] headers = {"STT", "Name", "Code", "Email", "Phone", "Age"};
    public static String SHEET_NAME = "employeeData";

    public static void exportToExcel(List<Employee> employees, HttpServletResponse response) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET_NAME);

        // Định dạng tiêu đề
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Tạo hàng tiêu đề
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Ghi dữ liệu nhân viên vào các dòng tiếp theo
        int rowIndex = 1;
        for (Employee emp : employees) {
            if (emp == null) continue; // Kiểm tra null tránh lỗi
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(rowIndex);
            row.createCell(1).setCellValue(emp.getCode() != null ? emp.getCode() : ""); // Tránh null
            row.createCell(2).setCellValue(emp.getName() != null ? emp.getName() : "");
            row.createCell(3).setCellValue(emp.getEmail() != null ? emp.getEmail() : "");
            row.createCell(4).setCellValue(emp.getPhone() != null ? emp.getPhone() : "");
            row.createCell(5).setCellValue(Integer.valueOf(emp.getAge()) != null ? emp.getAge() : 0);
            rowIndex++;
        }

        // Tự động điều chỉnh độ rộng của cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Xuất file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }



}
