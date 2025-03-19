package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.EmployeeDTO;
import com.globits.da.dto.response.EmployeeResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.exception.ProvinceCodeException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
        validateEmployeeAdd(request);


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
    public EmployeeResponse addEmployeeAndAddressByName(EmployeeDTO request) {
        validateEmployeeAdd(request);

        Province province = provinceRepo.findByName(request.getProvinceName())
                .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_FOUND));

        District district = districtRepository.findByNameAndProvinceId(request.getDistrictName(), province.getId())
                .orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_FOUND));

        Commune commune = communeRepository.findByNameAndDistrictId(request.getCommuneName(), district.getId())
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
        validateEmployeeUpdate(request);
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
    public void validateEmployeeAdd(EmployeeDTO request) {

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

        // Validate địa chỉ
        if (Objects.isNull(request.getProvinceId()) || request.getProvinceId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_NULL);
        }
        if (Objects.isNull(request.getDistrictId()) || request.getDistrictId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_NULL);
        }
        if (Objects.isNull(request.getCommuneId()) || request.getCommuneId() == 0) {
            throw new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_NULL);
        }

        if (Objects.isNull(request.getProvinceName())) {
            throw new EmployeeAppException(EmployeeCodeException.PROVINCE_NOT_NULL);
        }
        if (Objects.isNull(request.getDistrictName())) {
            throw new EmployeeAppException(EmployeeCodeException.DISTRICT_NOT_NULL);
        }
        if (Objects.isNull(request.getCommuneName())) {
            throw new EmployeeAppException(EmployeeCodeException.COMMUNE_NOT_NULL);
        }


    }

    public void validateEmployeeUpdate(EmployeeDTO request) {

        // Validate Code employee
        if (request.getCode().trim() == null || request.getCode().trim().isEmpty()) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_NULL);
        }

        if (!ValidationUtil.isValidCode(request.getCode().trim())) {
            throw new EmployeeAppException(EmployeeCodeException.EMPLOYEE_CODE_NOT_SPACE);
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


    }

    @Override
    public Map<String, Object> importExcelEmployee(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try (InputStream is = file.getInputStream()) {
            List<EmployeeDTO> employeeList = ExcelUtil.excelToEmployeeDtoList(is);

            // Validate dữ liệu
            List<Map<String, Object>> errors = validateEmployees(employeeList);
            if (!errors.isEmpty()) {
                response.put("success", false);
                response.put("errors", errors);
                return response;
            }

            // Convert từ DTO sang Entity và lưu vào DB
            List<Employee> employeesToSave = employeeList.stream()
                    .map(dto -> {
                        Employee employee = employeeMapper.toEmployee(dto);
                        employee.setProvince(provinceRepo.findById(dto.getProvinceId()).orElse(null));
                        employee.setDistrict(districtRepository.findById(dto.getDistrictId()).orElse(null));
                        employee.setCommune(communeRepository.findById(dto.getCommuneId()).orElse(null));
                        return employee;
                    })
                    .collect(Collectors.toList());

            employeeRepo.saveAll(employeesToSave);

            response.put("success", true);
        } catch (Exception e) {
            log.error("Error processing the file: {}", e.getMessage());
        }

        return response;
    }


    public List<Map<String, Object>> validateEmployees(List<EmployeeDTO> employeeList) {
        List<Map<String, Object>> errors = new ArrayList<>();

        for (EmployeeDTO dto : employeeList) {
            List<Map<String, String>> fieldErrors = new ArrayList<>();

            // Validate mã nhân viên
            if (dto.getCode() == null || dto.getCode().isEmpty()) {
                fieldErrors.add(createError("code", EmployeeCodeException.EMPLOYEE_CODE_NOT_NULL.getMessage()));
            } else {
                String code = dto.getCode().trim();
                if (!ValidationUtil.isValidCode(code)) {
                    fieldErrors.add(createError("code", EmployeeCodeException.EMPLOYEE_CODE_NOT_SPACE.getMessage()));
                }
                if (employeeRepo.findByCode(code).isPresent()) {
                    fieldErrors.add(createError("code", EmployeeCodeException.EMPLOYEE_CODE_ALREADY_EXISTS.getMessage()));
                }
                if (code.length() < 6) {
                    fieldErrors.add(createError("code", EmployeeCodeException.EMPLOYEE_CODE_MIN_LENGTH.getMessage()));
                }
                if (code.length() > 10) {
                    fieldErrors.add(createError("code", EmployeeCodeException.EMPLOYEE_CODE_MAX_LENGTH.getMessage()));
                }
            }

            // Validate tên nhân viên
            if (dto.getName() == null || dto.getName().isEmpty()) {
                fieldErrors.add(createError("name", EmployeeCodeException.EMPLOYEE_NAME_NOT_NULL.getMessage()));
            }

            // Validate email
            if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
                fieldErrors.add(createError("email", EmployeeCodeException.EMPLOYEE_EMAIL_NOT_NULL.getMessage()));
            } else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
                fieldErrors.add(createError("email", EmployeeCodeException.EMPLOYEE_EMAIL_FORMAT.getMessage()));
            }

            // Validate số điện thoại
            if (dto.getPhone() == null || dto.getPhone().isEmpty()) {
                fieldErrors.add(createError("phone", EmployeeCodeException.EMPLOYEE_PHONE_NOT_NULL.getMessage()));
            } else if (!ValidationUtil.isPhone(dto.getPhone())) {
                fieldErrors.add(createError("phone", EmployeeCodeException.EMPLOYEE_PHONE_FORMAT.getMessage()));
            }

            // Validate tuổi
            if (dto.getAge() < 18) {
                fieldErrors.add(createError("age", EmployeeCodeException.EMPLOYEE_AGE_MIN.getMessage()));
            } else if (dto.getAge() > 60) {
                fieldErrors.add(createError("age", EmployeeCodeException.EMPLOYEE_AGE_MAX.getMessage()));
            }

            // Validate tỉnh/thành phố
            if (dto.getProvinceName() == null || dto.getProvinceName().trim().isEmpty()) {
                fieldErrors.add(createError("province", "The province name cannot be null or empty"));
            } else {
                Optional<Province> provinceOpt = provinceRepo.findByName(dto.getProvinceName());
                if (!provinceOpt.isPresent()) {
                    fieldErrors.add(createError("province", "No province found: " + dto.getProvinceName()));
                } else {
                    dto.setProvinceId(provinceOpt.get().getId());

                    // Validate quận/huyện
                    if (dto.getDistrictName() == null || dto.getDistrictName().trim().isEmpty()) {
                        fieldErrors.add(createError("district", "The district name cannot be null or empty"));
                    } else {
                        Optional<District> districtOpt = districtRepository.findByNameAndProvinceId(dto.getDistrictName(), dto.getProvinceId());
                        if (!districtOpt.isPresent()) {
                            fieldErrors.add(createError("district", "No district found: " + dto.getDistrictName()));
                        } else {
                            dto.setDistrictId(districtOpt.get().getId());

                            // Validate phường/xã
                            if (dto.getCommuneName() == null || dto.getCommuneName().trim().isEmpty()) {
                                fieldErrors.add(createError("commune", "The commune name cannot be null or empty"));
                            } else {
                                Optional<Commune> communeOpt = communeRepository.findByNameAndDistrictId(dto.getCommuneName(), dto.getDistrictId());
                                if (!communeOpt.isPresent()) {
                                    fieldErrors.add(createError("commune", "No commune found: " + dto.getCommuneName()));
                                } else {
                                    dto.setCommuneId(communeOpt.get().getId());
                                }
                            }
                        }
                    }
                }
            }
            // Nếu có lỗi thì thêm vào danh sách lỗi
            if (!fieldErrors.isEmpty()) {
                Map<String, Object> errorMap = new LinkedHashMap<>();
                errorMap.put("row", dto.getRowIndex());
                errorMap.put("errors", fieldErrors);
                errors.add(errorMap);
            }
        }

        return errors;
    }

    private static Map<String, String> createError(String column, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("column", column);
        error.put("error", message);
        return error;
    }


}
