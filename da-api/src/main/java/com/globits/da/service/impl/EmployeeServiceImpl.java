package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.dto.request.EmployeeImportDTO;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.exception.ValidationException;
import com.globits.da.mapper.EmployeeMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.ExcelUtil;
import com.globits.da.utils.ValidationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepo;
    EmployeeMapper employeeMapper;
    CommuneRepository communeRepository;
    ProvinceRepository provinceRepo;
    DistrictRepository districtRepository;

    @Override
    public List<EmployeeResponse> getAllEmployee() {
        return employeeRepo.findAll().stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
    }


    @Override
    public List<EmployeeResponse> getEmpolyeeById(Integer id) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeById(id).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public List<EmployeeResponse> getEmpolyeeByCode(String code) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeByCode(code).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public List<EmployeeResponse> getEmpolyeeByName(String name) {
        List<EmployeeResponse> employees = employeeRepo.searchEmployeeByCode(name).stream().
                map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());
        return employees;
    }

    @Override
    public EmployeeResponse addEmployee(EmployeeDTO request) {
        validateEmployee(request);

        Province province = provinceRepo.findById(request.getProvinceId())
                .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_FOUND));

        District district = districtRepository.findByIdAndProvinceId(request.getDistrictId(), request.getProvinceId())
                .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_FOUND));

        Commune commune = communeRepository.findByIdAndDistrictId(request.getCommuneId(), request.getDistrictId())
                .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_FOUND));

        Employee employee = employeeMapper.toEmployee(request);
        employee.setProvince(province);
        employee.setDistrict(district);
        employee.setCommune(commune);
        return employeeMapper.toEmployeeResponse(employeeRepo.save(employee));
    }

    @Override
    public String deleteEmployee(Integer id) {
        Employee emp = employeeRepo.findById(id).get();
        if (emp != null) {
            employeeRepo.delete(emp);
            return "Delete employee successfully";
        }

        return "Employee not exit";
    }

    @Override
    public EmployeeResponse updateEmployee(Integer id, EmployeeDTO request) {
        validateEmployee(request);
        Employee employee = employeeRepo.findById(id).orElseThrow(()
                -> new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NOT_FOUND));
        employeeMapper.updateEmployee(employee, request);
        if (request.getProvinceId() != 0 && request.getDistrictId()
                != 0 && request.getCommuneId() != 0) {

            Province province = provinceRepo.findById(request.getProvinceId())
                    .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_FOUND));

            District district = districtRepository.findByIdAndProvinceId(request.getDistrictId(), request.getProvinceId())
                    .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_FOUND));

            Commune commune = communeRepository.findByIdAndDistrictId(request.getCommuneId(), request.getDistrictId())
                    .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_FOUND));

            employee.setProvince(province);
            employee.setDistrict(district);
            employee.setCommune(commune);
        }

        return employeeMapper.toEmployeeResponse(employeeRepo.save(employee));
    }

    @Override
    public List<EmployeeResponse> searchEmployees(EmployeeSearchDto employeeSearchDto) {

        List<EmployeeResponse> employees = employeeRepo.searchEmployees(
                employeeSearchDto.getCode(),
                employeeSearchDto.getName(),
                employeeSearchDto.getEmail(),
                employeeSearchDto.getPhone(),
                employeeSearchDto.getMinAge(),
                employeeSearchDto.getMaxAge())
                .stream().map(employeeMapper::toEmployeeResponse).collect(Collectors.toList());

        if (employees.isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEES_NOT_FOUND);
        }

        return employees;

    }


    @Override
    public void exportExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<Employee> employees = employeeRepo.findAll();
        if (employees == null || employees.isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEES_NOT_IMPLEMENT_EXPORT);
        }
        ExcelUtil.exportToExcel(employees, httpServletResponse);
    }

    @Override
    public void validateEmployee(EmployeeDTO request) {

        // Validate Code employee
        if (request.getCode().trim() == null || request.getCode().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_NULL);
        }

        if (!ValidationUtil.isValidCode(request.getCode().trim())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_SPACE);
        }

        if (employeeRepo.findByCode(request.getCode().trim()).isPresent()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_ALREADY_EXISTS);
        }

        String code = request.getCode().trim();

        if (code.length() < 6) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MIN_LENGTH);
        }

        if (code.length() > 10) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_MAX_LENGTH);
        }

        // Validate Name employee
        if (request.getName().trim() == null || request.getName().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NAME_NOT_NULL);
        }

        // Validate email employee
        if (request.getEmail().trim() == null || request.getEmail().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_NOT_NULL);
        }

        if (!ValidationUtil.isValidEmail(request.getEmail().trim())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_EMAIL_FORMAT);
        }

        // Validate phone employee
        if (request.getPhone().trim() == null || request.getPhone().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_NOT_NULL);
        } else if (!ValidationUtil.isPhone(request.getPhone().trim())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_PHONE_FORMAT);

        }

        // Validate Age Empolyee
        if (Objects.isNull(request.getAge())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_NOT_NULL);
        } else {
            // Validate age employee
            if (request.getAge() <= 0 || Integer.valueOf(request.getAge()) == null) {
                throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_VALUE);
            } else if (request.getAge() < 18) {
                throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_MIN);
            } else if (request.getAge() > 60) {
                throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_AGE_MAX);
            }
        }


        if (Objects.isNull(request.getProvinceId()) || request.getProvinceId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_NULL);
        }
        if (Objects.isNull(request.getDistrictId()) || request.getDistrictId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_NULL);
        }
        if (Objects.isNull(request.getCommuneId()) || request.getCommuneId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_NULL);
        }


    }

    @Override
    public void importExcelEmployee(MultipartFile file) throws IOException {


    }

//    public void importEmployees(MultipartFile file) throws IOException {
//        try {
//            List<EmployeeImportDTO> employees = readExcelFile(file);
//
//            // Kiểm tra lỗi trước khi lưu
//            List<String> errors = validateEmployees(employees);
//            if (!errors.isEmpty()) {
//                throw new EmployeeAppException(EmployeeCodeException.INVALID_EMPLOYEE_IMPORT);
//            }
//
//            // Chuyển DTO thành Entity và lưu vào DB
//            List<Employee> employeeEntities = employees.stream()
//                    .map(employeeMapper::toEmployeeImport)
//                    .collect(Collectors.toList());
//
//            employeeRepo.saveAll(employeeEntities);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }

    private List<EmployeeImportDTO> readExcelFile(MultipartFile file) throws IOException {
        List<EmployeeImportDTO> employees = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua hàng tiêu đề (index 0)
            Row row = sheet.getRow(i);
            if (row == null) continue;

            EmployeeImportDTO dto = new EmployeeImportDTO();
            dto.setRowIndex(i + 1);
            dto.setName(row.getCell(1).getStringCellValue());
            dto.setCode(row.getCell(2).getStringCellValue());
            dto.setEmail(row.getCell(3).getStringCellValue());
            dto.setPhone(row.getCell(4).getStringCellValue());
            dto.setAge((int) row.getCell(5).getNumericCellValue());
            dto.setProvince(row.getCell(6).getStringCellValue());
            dto.setDistrict(row.getCell(7).getStringCellValue());
            dto.setCommune(row.getCell(8).getStringCellValue());

            employees.add(dto);
        }
        workbook.close();
        return employees;
    }

    private List<String> validateEmployees(List<EmployeeImportDTO> employees) {
        List<String> errors = new ArrayList<>();
        for (EmployeeImportDTO dto : employees) {
            if (dto.getName().isEmpty()) {
                errors.add("Lỗi dòng " + dto.getRowIndex() + ": Tên không được để trống.");
            }
            if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("Lỗi dòng " + dto.getRowIndex() + ": Email không hợp lệ.");
            }
            if (dto.getAge() <= 0) {
                errors.add("Lỗi dòng " + dto.getRowIndex() + ": Tuổi phải là số dương.");
            }
        }
        return errors;
    }


}
