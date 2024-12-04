package com.globits.da.repository;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Integer> {

    @Query("SELECT C FROM Certificate C WHERE lower(C.name) LIKE lower(concat('%', :name , '%'))")
    List<Certificate> findByNameQuery(@Param("name") String name);

}
