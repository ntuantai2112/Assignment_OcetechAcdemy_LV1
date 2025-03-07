package com.globits.da.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globits.da.dto.request.MyApiDTO;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.exception.CodeConfig;
import com.globits.da.service.MyApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Transactional
@Slf4j
//@Consumes(MediaType.APPLICATION_JSON)
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
            log.error("Error processing request body: {}", e.getMessage(), e);
        }
        // Thực hiện đọc dữ liệu trong form-data

        return null;
    }

    @Override
    public ApiResponse<String> processFile(MultipartFile file) {

        ApiResponse<String> apiResponse = new ApiResponse();
        CodeConfig code = CodeConfig.SUCCESS_CODE;
        apiResponse.setCode(code.getCode());
        apiResponse.setMessage(code.getMessage());
        try {
            readFile(file);
            apiResponse.setResult("Read file successfully: " + file.getOriginalFilename());
            return apiResponse;
        } catch (Exception ex) {
            apiResponse.setResult("Error processing file:" + ex.getMessage());
            return apiResponse;


        }
    }

    @Override
    public MyApiDTO createMyApiNotRequestBody(MyApiDTO myApiDto) {
        return myApiDto;
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
                System.out.println(line);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error " + e.getMessage());
        }

    }


    private void readFileExcel(MultipartFile file) {

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                StringBuilder rowContent = new StringBuilder();
                boolean hasData = false;

                for (Cell cell : row) {
                    String cellValue = getCellValue(cell);
                    if (!cellValue.trim().isEmpty()) {
                        hasData = true; //  Kiểm tra xem có ô nào chứa dữ liệu không
                    }
                    rowContent.append(cellValue).append(" | ");
                }

                if (hasData) {
                    log.info("Row: {}", rowContent); // ✅ Chỉ in nếu có dữ liệu
                }
            }
        } catch (Exception e) {
            log.error("Error reading Excel file: {}", e.getMessage(), e);
        }

    }


    private String getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum(); // Sử dụng getCellTypeEnum() cho các phiên bản cũ hơn.
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Nếu là kiểu ngày tháng, định dạng lại
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    // Nếu là số, chuyển về chuỗi, loại bỏ số khoa học
                    return String.valueOf((long) cell.getNumericCellValue());
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
