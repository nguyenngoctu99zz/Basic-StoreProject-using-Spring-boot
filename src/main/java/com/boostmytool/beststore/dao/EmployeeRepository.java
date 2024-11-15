package com.boostmytool.beststore.dao;


import com.boostmytool.beststore.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository <Employee, Integer> {


//    @Query("SELECT e FROM Employee e WHERE e.empname LIKE %:name%")
//    List<Employee> findByNameContaining(@Param("name") String name);

    @Query("SELECT e FROM Employee e WHERE e.empname LIKE %:name%")
    Page<Employee> findByNameContaining(String name, Pageable pageable);
//    Page<Employee> findAll(Pageable pageable);

}
