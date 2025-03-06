package com.globits.da.utils;

import com.globits.da.domain.Employee;
import com.globits.da.dto.request.EmployeeDto;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public static String[] headers = {"STT", "Name", "Code", "Email", "Phone", "Age"};
    public static String SHEET_NAME = "employeeData";

    public static ByteArrayInputStream exportToExcel(List<Employee> employees) throws IOException {


        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row rowHeader = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = rowHeader.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderStyle(workbook));

            }

            // Thêm dữ liệu
            int rowIdex = 1;
            for (Employee emp : employees) {
                if (emp == null) continue; // Kiểm tra null tránh lỗi
                Row row = sheet.createRow(rowIdex);
                row.createCell(0).setCellValue(rowIdex);
                row.createCell(1).setCellValue(emp.getCode() != null ? emp.getCode() : ""); // Tránh null
                row.createCell(2).setCellValue(emp.getName() != null ? emp.getName() : "");
                row.createCell(3).setCellValue(emp.getEmail() != null ? emp.getEmail() : "");
                row.createCell(4).setCellValue(emp.getPhone() != null ? emp.getPhone() : "");
                row.createCell(5).setCellValue(Integer.valueOf(emp.getAge()) != null ? emp.getAge() : 0);
                rowIdex++;
            }

            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } finally {
            workbook.close();
            outputStream.close();
        }

    }

    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;

    }


//    public static List<EmployeeDto> parseExcel(MultipartFile multipartFile){
//        List<EmployeeDto> employeeDtos = new ArrayList<>();
//        try{
//
//        }catch (Exception e){
//            throw  new EmployeeAppException(EmployeeCodeException.INVALID_FILE_EXCEL);
//        }
//
//
//
//    }

}
