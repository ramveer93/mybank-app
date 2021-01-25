package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mybank.app.entity.Employee;

public interface AdminRepository extends JpaRepository<Employee, Long> {

	@Query(value = "SELECT * FROM employee e WHERE e.employee_user_name=:employeeUserName", nativeQuery = true)
	public Employee findEmployeeByEmployeeUserName(@Param("employeeUserName") String employeeUserName);
}
