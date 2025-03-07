//package com.globits.da.mapper;
//
//import com.globits.da.domain.Commune;
//import com.globits.da.domain.District;
//import com.globits.da.domain.Employee;
//import com.globits.da.domain.Province;
//import com.globits.da.dto.request.EmployeeDTO;
//import com.globits.da.dto.request.EmployeeImportDTO;
//import com.globits.da.dto.response.EmployeeResponse;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//
//@Mapper(componentModel = "spring")
//public interface EmployeeImportMapper {
//
//    @Mapping(target = "id", ignore = true) // Không ánh xạ ID vì là dữ liệu import
//    @Mapping(target = "commune", source = "commune", qualifiedByName = "mapCommune")
//    @Mapping(target = "district", source = "district", qualifiedByName = "mapDistrict")
//    @Mapping(target = "province", source = "province", qualifiedByName = "mapProvince")
//    Employee toEmployee(EmployeeImportDTO employeeDto);
//
//    // Hàm ánh xạ từ String sang Commune
//    default Commune mapCommune(String communeName) {
//        if (communeName == null || communeName.isEmpty()) {
//            return null;
//        }
//        Commune commune = new Commune();
//        commune.setName(communeName);
//        return commune;
//    }
//
//    // Hàm ánh xạ từ String sang District
//    default District mapDistrict(String districtName) {
//        if (districtName == null || districtName.isEmpty()) {
//            return null;
//        }
//        District district = new District();
//        district.setName(districtName);
//        return district;
//    }
//
//    // Hàm ánh xạ từ String sang Province
//    default Province mapProvince(String provinceName) {
//        if (provinceName == null || provinceName.isEmpty()) {
//            return null;
//        }
//        Province province = new Province();
//        province.setName(provinceName);
//        return province;
//    }
//
//
//}
