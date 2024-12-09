package com.globits.da.repository;

import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Integer> {

    @Query("SELECT P FROM Province P WHERE lower(P.name) LIKE lower(concat('%', :name , '%'))")
    List<Province> findByNameQuery(@Param("name") String name);

//    @Query(value = "SELECT * FROM tbl_province WHERE LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
//    List<Province> findByNameQuery(@Param("keyword") String keyword);

    @Query("SELECT P FROM Province P WHERE lower(P.name) LIKE lower(concat('%', :provinceName , '%'))")
    Province findProvinceByNameQuery(@Param("provinceName") String provinceName);

    boolean existsByName(String proviceName);

}
