package com.globits.da.repository;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.response.CommuneResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Integer> {

    @Query("SELECT C FROM Commune C WHERE lower(C.name) LIKE lower(concat('%', :name , '%'))")
    List<Commune> findByNameQuery(@Param("name") String name);

    @Query("SELECT C FROM Commune C WHERE lower(C.name) LIKE lower(concat('%', :name , '%'))")
    Optional<Commune> findCommuneByName(@Param("name") String name);


    Optional<Commune> findByName(String name);

    @Query("SELECT C FROM Commune C WHERE C.district.id = :districtId")
    List<Commune> findCommunesByDistrictId(@Param("districtId") Integer districtId);

    @Query("SELECT C FROM Commune C WHERE lower(C.name) LIKE lower(concat('%', :communeName , '%')) AND C.district.id =:districtId")
    Optional<Commune> findByNameAndDistrictId(@Param("communeName") String communeName, @Param("districtId") Integer districtId);

    Optional<Commune> findByIdAndDistrictId(Integer id, Integer districtId);
}
