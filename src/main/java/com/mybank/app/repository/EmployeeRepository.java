package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mybank.app.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	@Query(value = "SELECT e FROM employee e WHERE e.employee_name=:employeeName" , nativeQuery=true)
	public Employee getEmployeeByEmployeeNameId(@Param("employeeName") String employeeName);
}
