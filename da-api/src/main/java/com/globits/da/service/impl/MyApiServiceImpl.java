package com.globits.da.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globits.da.dto.request.MyApiDTO;
import com.globits.da.service.MyApiService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Transactional
public class MyApiServiceImpl implements MyApiService {


    @Override
    public String myFirstApiService() {
        return "My First Api Service";
    }

    @Override
    public MyApiDTO createMyApiDTO(MyApiDTO myApiDTO) {
        return myApiDTO;
    }

    @Override
    public MyApiDTO createMyApiFormData(MyApiDTO myApiDTO) {
        return myApiDTO;
    }

    @Override
    public Map<String, Object> myApiFormData(MyApiDTO myApiDto) {

//        String fileName = myApiDto.getFile().getOriginalFilename();
//        Long fileSize = myApiDto.getFile().getSize();
        Map<String, Object> response = new HashMap<>();
        response.put("name", myApiDto.getName());
        response.put("age", myApiDto.getAge());
//        response.put("fileName", fileName);
//        response.put("fileSize", fileSize);
        return response;
    }

    @Override
    public ResponseEntity<MyApiDTO> getMyApiFormData(MyApiDTO myApiDto) {
        return ResponseEntity.ok(myApiDto);
    }

    @Override
    public MyApiDTO createMyApiPathVariable(String code, String name, Integer age) {
        return new MyApiDTO(code, name, age);
    }

    @Override
    public MyApiDTO postMyFirstAPI(HttpServletRequest request) {
        //Thực hiện đọc dữ lieu trong body với Json
        try {
            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            // Chuyển đổi Json thành đối tượng DTO
            ObjectMapper objectMapper = new ObjectMapper();
            MyApiDTO myApiDTO = objectMapper.readValue(requestBody.toString(), MyApiDTO.class);
            return myApiDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Thực hiện đọc dữ liệu trong form-data

        return null;
    }

    @Override
    public ResponseEntity<String> processFile(MultipartFile file) {
        try {
            readFile(file);
            return ResponseEntity.ok("Read file success fully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error processing file:" + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<MyApiDTO> getMyApiNoRequestBody(MyApiDTO myApiDto) {
        return ResponseEntity.ok(myApiDto);
    }


    public void readFile(MultipartFile file) {
        String fileType = file.getContentType();
        try {
            if (fileType != null && fileType.equals("text/plain")) {
                readFileText(file);
            } else if (fileType != null && fileType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                readFileExcel(file);
            } else {
                throw new IllegalAccessException("Unsupported file type" + fileType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error read file type" + e.getMessage());
        }

    }


    private void readFileText(MultipartFile file) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Text read line:" + line);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error " + e.getMessage());
        }

    }


    private void readFileExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                StringBuilder rowContent = new StringBuilder();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    rowContent.append(getCellValue(cell)).append("\t");
                }
                System.out.println(rowContent);

            }
        } catch (Exception e) {
            throw new RuntimeException("Error " + e.getMessage());
        }

    }


    private String getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum(); // Sử dụng getCellTypeEnum() cho các phiên bản cũ hơn.
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Nếu ô là kiểu ngày, chuyển đổi sang định dạng ngày
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Nếu ô chứa công thức, xử lý công thức
                return cell.getCellFormula();
            case BLANK:
                return ""; // Ô trống
            default:
                return "Unknown Cell Type"; // Kiểu không xác định
        }
    }


}
