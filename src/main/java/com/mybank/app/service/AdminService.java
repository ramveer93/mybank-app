package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Bank;
import com.mybank.app.entity.Employee;
import com.mybank.app.repository.AdminRepository;
import com.mybank.app.repository.BankRepository;
import com.mybank.app.util.BankException;

@Service
public class AdminService {

	@Autowired
	private AdminRepository empRepo;

	@Autowired
	private BankRepository bankRepo;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
	 * This method will add a new employee 
	 * if employee id is passed from input, this method will update the existing employee
	 * @param employee
	 * @return persisted employee
	 */
	public Employee addNewBankEmployee(Employee employee) {
		this.LOGGER.debug("add bank employee request came in employee service {} ", employee);
		if (employee.getId() != null && employee.getId() != 0) {
			this.LOGGER.info("employeeId {} found in request so considering it as update request", employee.getId());
			Optional<Employee> existingEmployee = this.empRepo.findById(employee.getId());
			if (existingEmployee.isPresent()) {
				Employee employeeFromDb = existingEmployee.get();
				employee.setId(employeeFromDb.getId());
				employee.setEmployeeUserName(employeeFromDb.getEmployeeUserName());
				employee.setUpdatedOn(LocalDateTime.now());
				employee.setCreatedOn(employeeFromDb.getCreatedOn());
				employee.setRoles(employeeFromDb.getRoles());
				employee.setBank(employeeFromDb.getBank());
				employee = this.empRepo.save(employee);
				this.LOGGER.info("Successfully update employee details for id {} ", employee.getId());
				return employee;
			}
		}
		Bank inputBank = employee.getBank();
		if (inputBank != null && inputBank.getBankId() != null && inputBank.getBankId() != 0) {
			LOGGER.info("Found bankId in input so new bank won't be created");
			Optional<Bank> bankFromDb = this.bankRepo.findById(inputBank.getBankId());
			if (bankFromDb.isPresent()) {
				inputBank = bankFromDb.get();
				employee.setBank(inputBank);
				LOGGER.info("Successfully attached the employee {} to bank {} ", employee.getEmployeeUserName(),
						inputBank.toString());
			} else {
				throw new BankException(
						"Bank with given bankId " + inputBank.getBankId() + " not found, No record will be created ");
			}
		}
		Employee fromDb = this.empRepo.save(employee);
		fromDb.setEmployeeUserName();
		fromDb.setUpdatedOn(LocalDateTime.now());
		fromDb = this.empRepo.save(fromDb);
		this.LOGGER.info("Successfully created employee details for id {} ", fromDb.getId());
		return fromDb;
	}

	/**
	 * This will delete existing employee
	 * if the employee doesn't exist exception will be thrown
	 * @param employeeId
	 */
	public void deleteEmployee(Long employeeId) {
		this.LOGGER.info("delete employee request came in emp service with empId {} ", employeeId);
		Optional<Employee> employeeFromDb = this.empRepo.findById(employeeId);
		if (employeeFromDb.isPresent()) {
			Employee employee = employeeFromDb.get();
			employee.setDeleted(LocalDateTime.now());
			employee.setUpdatedOn(LocalDateTime.now());
			this.empRepo.save(employee);
			this.LOGGER.info("successfully soft deleted emp with id {} ", employeeId);
		} else {
			this.LOGGER.error("Error deleting emp : emp with given id doesn't exist");
			throw new BankException("Employee with given id doesn't exist");
		}

	}

}
