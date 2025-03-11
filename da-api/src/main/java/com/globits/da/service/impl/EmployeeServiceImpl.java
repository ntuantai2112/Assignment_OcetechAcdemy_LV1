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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public List<String> importExcelEmployee(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        try (InputStream is = file.getInputStream()) {
            List<EmployeeDTO> employeeList = ExcelUtil.excelToEmployeeDtoList(is);

            // Validate dữ liệu
            errors = validateEmployees(employeeList);
            if (!errors.isEmpty()) {
                return errors; // Nếu có lỗi, trả về danh sách lỗi
            }
            // Convert từ DTO sang Entity
            List<Employee> employeesToSave = new ArrayList<>();
            for (EmployeeDTO dto : employeeList) {
                Employee employee = employeeMapper.toEmployee(dto);
                employee.setProvince(provinceRepo.findById(dto.getProvinceId()).get());
                employee.setDistrict(districtRepository.findById(dto.getDistrictId()).get());
                employee.setCommune(communeRepository.findById(dto.getCommuneId()).get());
                employeesToSave.add(employee);
            }
            employeeRepo.saveAll(employeesToSave);
        } catch (Exception e) {
            log.error(e.getMessage());
            List<String> errorList = new ArrayList<>();
            errorList.add("Lỗi khi xử lý file!");
            return errorList;
        }
        return errors;
    }

    private List<String> validateEmployees(List<EmployeeDTO> employeeList) {
        List<String> errors = new ArrayList<>();

        for (EmployeeDTO dto : employeeList) {
            StringBuilder errorMsg = new StringBuilder("Dòng " + dto.getRowIndex() + ": ");

            // Kiểm tra các giá trị bắt buộc
            if (dto.getCode() == null || dto.getCode().isEmpty()) {
                errorMsg.append("Mã nhân viên không được để trống. ");
            }
            if (dto.getName() == null || dto.getName().isEmpty()) {
                errorMsg.append("Tên không được để trống. ");
            }
            if (dto.getPhone() == null || dto.getPhone().isEmpty()) {
                errorMsg.append("Số điện thoại không được để trống. ");
            }

            if (dto.getPhone() != null && !ValidationUtil.isPhone(dto.getPhone())) {
                errorMsg.append("Số điện thoại không được để trống. ");
            }

            if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
                errorMsg.append("Email không được để trống. ");
            }

            if (dto.getEmail() != null && !ValidationUtil.isValidEmail(dto.getEmail())) {
                errorMsg.append("Email không hợp lệ. ");
            }

            if (dto.getAge() < 18) {
                errorMsg.append("Tuổi phải lớn hơn hoặc bằng 18. ");
            }


            // Kiểm tra tỉnh
            Optional<Province> province = provinceRepo.findByName(dto.getProvinceName());
            if (province == null || Objects.isNull(province)) {
                errorMsg.append("Không tìm thấy tỉnh: ").append(dto.getProvinceName()).append(". ");
                continue;
            } else {
                dto.setProvinceId(province.get().getId());
            }

            Optional<District> district = districtRepository.findByNameAndProvinceId(dto.getDistrictName(), dto.getProvinceId());
            if (district == null || Objects.isNull(district)) {
                errorMsg.append("Không tìm thấy huyện: ").append(dto.getDistrictName()).append(". ");
            } else {
                dto.setDistrictId(district.get().getId());
            }

            Optional<Commune> commune = communeRepository.findByNameAndDistrictId(dto.getCommuneName(), dto.getDistrictId());
            if (commune == null || Objects.isNull(commune)) {
                errorMsg.append("Không tìm thấy xã: ").append(dto.getCommuneName()).append(". ");
                continue;
            } else {
                dto.setCommuneId(commune.get().getId());
            }


            // Nếu có lỗi, thêm vào danh sách lỗi
            if (!errorMsg.toString().equals("Dòng " + dto.getRowIndex() + ": ")) {
                errors.add(errorMsg.toString());
            }
        }
        return errors;
    }


}
