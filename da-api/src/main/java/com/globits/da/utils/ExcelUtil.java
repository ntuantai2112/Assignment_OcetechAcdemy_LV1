package com.globits.da.utils;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelUtil {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private ProvinceRepository provinceRepo;

    @Autowired
    private DistrictRepository districtRepo;

    @Autowired
    private CommuneRepository communeRepo;

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

    public static List<EmployeeDTO> excelToEmployeeDtoList(InputStream is) {
        List<EmployeeDTO> employees = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                EmployeeDTO employee = new EmployeeDTO();
                employee.setRowIndex(i + 1);
                employee.setCode(row.getCell(1).getStringCellValue());
                employee.setName(row.getCell(2).getStringCellValue());
                employee.setEmail(row.getCell(3).getStringCellValue());
                employee.setPhone(row.getCell(4).getStringCellValue());
                employee.setAge((int) row.getCell(5).getNumericCellValue());
                employee.setProvinceName(row.getCell(6).getStringCellValue());
                employee.setDistrictName(row.getCell(7).getStringCellValue());
                employee.setCommuneName(row.getCell(8).getStringCellValue());

                employees.add(employee);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc file Excel!");
        }

        return employees;
    }

    //// Lấy tên tỉnh, huyện, xã từ file Excel
    //                String provinceName = row.getCell(6).getStringCellValue();
    //                String districtName = row.getCell(7).getStringCellValue();
    //                String communeName = row.getCell(8).getStringCellValue();
    //
    //                // Tìm ID của tỉnh từ database
    //                Optional<Province> province = provinceRepo.findByName(provinceName);
    //                if (province.isPresent()) {
    //                    employee.setProvinceId(province.get().getId());
    //                } else {
    //                    throw new RuntimeException("Không tìm thấy tỉnh: " + provinceName + " ở dòng " + (i + 1));
    //                }
    //
    //                // Tìm ID của huyện từ database
    //                Optional<District> districtOpt = districtRepo.findByNameAndProvinceId(districtName, employee.getProvinceId());
    //                if (districtOpt.isPresent()) {
    //                    employee.setDistrictId(districtOpt.get().getId());
    //                } else {
    //                    throw new RuntimeException("Không tìm thấy huyện: " + districtName + " thuộc tỉnh " + provinceName + " ở dòng " + (i + 1));
    //                }
    //
    //                // Tìm ID của xã từ database
    //                Optional<Commune> communeOpt = communeRepo.findByNameAndDistrictId(communeName, employee.getDistrictId());
    //                if (communeOpt.isPresent()) {
    //                    employee.setCommuneId(communeOpt.get().getId());
    //                } else {
    //                    throw new RuntimeException("Không tìm thấy xã: " + communeName + " thuộc huyện " + districtName + " ở dòng " + (i + 1));
    //                }
}
