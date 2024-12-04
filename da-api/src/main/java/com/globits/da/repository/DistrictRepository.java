package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Integer> {

    @Query("SELECT D FROM District D WHERE lower(D.name) LIKE lower(concat('%', :name , '%'))")
    List<District> findByNameQuery(@Param("name") String name);

    List<District> findByProvinceId(Integer provinceId);




}
