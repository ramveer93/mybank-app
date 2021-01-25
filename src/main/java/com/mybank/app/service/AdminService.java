package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Employee;
import com.mybank.app.repository.AdminRepository;
import com.mybank.app.util.BankException;

@Service
public class AdminService {

	@Autowired
	private AdminRepository empRepo;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public Employee addNewBankEmployee(Employee employee) {
		this.LOGGER.debug("add bank employee request came in employee service {} ",employee);
		if (employee.getId() != null && employee.getId() != 0) {
			this.LOGGER.info("employeeId {} found in request so considering it as update request",employee.getId());
			Optional<Employee> existingEmployee = this.empRepo.findById(employee.getId());
			if (existingEmployee.isPresent()) {
				Employee employeeFromDb = existingEmployee.get();
				employee.setId(employeeFromDb.getId());
				employee.setEmployeeUserName(employeeFromDb.getEmployeeUserName());
				employee.setUpdatedOn(LocalDateTime.now());
				employee.setCreatedOn(employeeFromDb.getCreatedOn());
				employee.setRoles(employeeFromDb.getRoles());
				employee = this.empRepo.save(employee);
				this.LOGGER.info("Successfully update employee details for id {} ",employee.getId());
				return employee;
			}
		}
		Employee fromDb = this.empRepo.save(employee);
		fromDb.setEmployeeUserName();
		fromDb.setUpdatedOn(LocalDateTime.now());
		fromDb = this.empRepo.save(fromDb);
		this.LOGGER.info("Successfully created employee details for id {} ",fromDb.getId());

		return fromDb;
	}

	public void deleteEmployee(Long employeeId) {
		this.LOGGER.info("delete employee request came in emp service with empId {} ",employeeId);
		Optional<Employee> employeeFromDb = this.empRepo.findById(employeeId);
		if (employeeFromDb.isPresent()) {
			Employee employee = employeeFromDb.get();
			employee.setDeleted(LocalDateTime.now());
			employee.setUpdatedOn(LocalDateTime.now());
			this.empRepo.save(employee);
			this.LOGGER.info("successfully soft deleted emp with id {} ",employeeId);
		} else {
			this.LOGGER.error("Error deleting emp : emp with given id doesn't exist");
			throw new BankException("Employee with given id doesn't exist");
		}
			
			
	}

}
