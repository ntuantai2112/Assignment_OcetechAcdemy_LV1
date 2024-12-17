package com.globits.da.repository;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Integer> {

    @Query("SELECT C FROM Certificate C WHERE lower(C.name) LIKE lower(concat('%', :name , '%'))")
    List<Certificate> findByNameQuery(@Param("name") String name);

    // Lấy ra Danh sách Certificate gồm cả Employee và Province
    @Query("SELECT C FROM Certificate C " +
            "LEFT JOIN FETCH C.employee e " +
            "LEFT JOIN FETCH C.province p")
    List<Certificate> findAllWithEmployeeAndProvince();



    //Kiểm tra danh sách văn bằng theo tỉnh

    @Query("SELECT C FROM Certificate C WHERE C.employee.id = :employeeId AND lower(C.name) LIKE lower(concat('%', :certificateName , '%')) AND C.province.id = :provinceId AND C.validUntil > :currentDate")
    List<Certificate> findValidCertificatesByProvince(@Param("employeeId") Integer employeeId,
                                                      @Param("certificateName") String certificateName,
                                                      @Param("provinceId") Integer provinceId,
                                                      @Param("currentDate")LocalDate currentDate);


    @Query("SELECT C FROM Certificate C WHERE C.employee.id = :employeeId AND lower(C.name) LIKE lower(concat('%', :certificateName , '%')) AND C.validUntil > :currentDate")
    List<Certificate> findValidCertificatesByName(@Param("employeeId") Integer employeeId,
                                                      @Param("certificateName") String certificateName,
                                                      @Param("currentDate")LocalDate currentDate);


}
