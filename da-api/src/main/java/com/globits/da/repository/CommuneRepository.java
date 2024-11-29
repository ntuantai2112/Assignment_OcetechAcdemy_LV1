package com.globits.da.repository;

import com.globits.da.domain.Commune;
import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepository extends JpaRepository<Commune,Integer> {

    @Query("SELECT C FROM Commune C WHERE lower(C.name) LIKE lower(concat('%', :name , '%'))")
    List<Commune> findByNameQuery(@Param("name") String name);

}
