package com.globits.da.repository;

import com.globits.da.domain.Employee;
import com.globits.da.dto.search.EmployeeSearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {


    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    List<Employee> searchEmployeeById(@Param("id") Integer id);

    @Query("SELECT e FROM Employee e WHERE e.code = :code")
    List<Employee> searchEmployeeByCode(@Param("code") String code);

    @Query("SELECT e FROM Employee e WHERE e.name = :name")
    List<Employee> searchEmployeeByName(@Param("name") String name);


    @Query(" SELECT  e FROM Employee e " +
            "WHERE(:code IS NULL OR e.code LIKE %:code% ) " +
            "AND (:name  IS NULL OR e.name LIKE %:name% ) " +
            "AND (:email IS NULL OR e.email LIKE %:email% )" +
            "AND (:phone IS NULL OR e.phone LIKE %:phone% )")
    List<Employee> searchEmployees(@Param("code") String code,
                                   @Param("name") String name,
                                   @Param("email") String email,
                                   @Param("phone") String phone);


    Optional<Employee> findByCode(String code);


    Optional<Employee> findByEmail(String email);
}
