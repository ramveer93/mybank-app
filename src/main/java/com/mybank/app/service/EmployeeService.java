package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.List;
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
		Employee fromDb = this.empRepo.save(employee);
		return fromDb;
	}

	public void deleteEmployee(Long employeeId) {
		Optional<Employee> employeeFromDb = this.empRepo.findById(employeeId);
		if (employeeFromDb.isPresent()) {
			Employee employee = employeeFromDb.get();
			employee.setDeleted(LocalDateTime.now());
			this.empRepo.save(employee);
		}
		else
			throw new BankException("Employee with given id doesn't exist");
	}

}
