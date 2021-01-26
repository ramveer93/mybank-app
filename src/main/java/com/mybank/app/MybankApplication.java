package com.mybank.app;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mybank.app.entity.Employee;
import com.mybank.app.entity.EmployeeRole;
import com.mybank.app.service.AdminService;

@SpringBootApplication
@EnableScheduling
public class MybankApplication {
    
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AdminService empService;

	@PostConstruct
	public void initEmployees() {
		this.LOGGER.info("Initializing admin user ");
		Employee adminEmployee = new Employee();
		adminEmployee.setDesignation("ADMIN");
		adminEmployee.setEmployeeFirstName("Admin");
		adminEmployee.setEmployeeMiddleName("Admin");
		adminEmployee.setEmployeeLastName("Admin");
		adminEmployee.setPassword("Admin");
		Set<EmployeeRole> roles = new HashSet<>();

		EmployeeRole adminRole = new EmployeeRole();
		adminRole.setName("ADMIN");
		roles.add(adminRole);

		adminEmployee.setRoles(roles);
		adminEmployee = this.empService.addNewBankEmployee(adminEmployee);
		this.LOGGER.info("Successfully initialized ADMIN user {} ",adminEmployee.toString());

		this.LOGGER.info("Initializing System  user ");
		Employee emplEmployee = new Employee();
		emplEmployee.setDesignation("System");
		emplEmployee.setEmployeeFirstName("System");
		emplEmployee.setEmployeeMiddleName("System");
		emplEmployee.setEmployeeLastName("Employee");
		emplEmployee.setPassword("System");

		Set<EmployeeRole> empRoles = new HashSet<>();

		EmployeeRole empRole = new EmployeeRole();
		empRole.setName("EMPLOYEE");

		empRoles.add(empRole);

		emplEmployee.setRoles(empRoles);
		emplEmployee = this.empService.addNewBankEmployee(emplEmployee);
		this.LOGGER.info("successfully created System  user {} ",emplEmployee);
	}

	public static void main(String[] args) {
		SpringApplication.run(MybankApplication.class, args);
	}

}
