package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Integer> {

    @Query("SELECT D FROM District D WHERE lower(D.name) LIKE lower(concat('%', :name , '%'))")
    List<District> findByNameQuery(@Param("name") String name);

    List<District> findByProvinceId(Integer provinceId);

    @Query("SELECT D FROM District D WHERE lower(D.name) LIKE lower(concat('%', :districtName , '%'))")
    District findDistrictByNameQuery(@Param("districtName") String districtName);

    @Query("SELECT D FROM District D WHERE lower(D.name) LIKE lower(concat('%', :districtName , '%')) AND D.province.id =:provinceId")
    Optional<District> findByNameAndProvinceId(@Param("districtName") String districtName, @Param("provinceId") Integer provinceId);

    Optional<District> findByIdAndProvinceId(Integer id, Integer provinceId);

}
