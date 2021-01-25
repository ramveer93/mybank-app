package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Employee;
import com.mybank.app.repository.EmployeeRepository;
import com.mybank.app.util.BankException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository empRepo;

	public Employee addNewBankEmployee(Employee employee) {
		if (employee.getId() != null && employee.getId() != 0) {
			// this is update request
			Optional<Employee> existingEmployee = this.empRepo.findById(employee.getId());
			if (existingEmployee.isPresent()) {
				Employee employeeFromDb = existingEmployee.get();
				employeeFromDb.setBankId(employee.getBankId());
				employeeFromDb.setDesignation(employee.getDesignation());
				employeeFromDb.setEmployeeFirstName(employee.getEmployeeFirstName());
				employeeFromDb.setEmployeeLastName(employee.getEmployeeLastName());
				employeeFromDb.setEmployeeMiddleName(employee.getEmployeeMiddleName());
				employeeFromDb.setPassword(employee.getPassword());
				employeeFromDb.setRoles(employee.getRoles());
				employeeFromDb.setUpdatedOn(LocalDateTime.now());
				this.empRepo.save(employeeFromDb);
				return employeeFromDb;
			}
		}

		Employee fromDb = this.empRepo.save(employee);
		return fromDb;
	}

	public void deleteEmployee(Long employeeId) {
		Optional<Employee> employeeFromDb = this.empRepo.findById(employeeId);
		if (employeeFromDb.isPresent()) {
			Employee employee = employeeFromDb.get();
			employee.setDeleted(LocalDateTime.now());
			employee.setUpdatedOn(LocalDateTime.now());
			this.empRepo.save(employee);
		} else
			throw new BankException("Employee with given id doesn't exist");
	}

}
