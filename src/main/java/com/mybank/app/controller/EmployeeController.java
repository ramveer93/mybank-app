package com.mybank.app.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.app.entity.Employee;
import com.mybank.app.service.EmployeeService;
import com.mybank.app.util.BankException;
import com.mybank.app.util.JSONUtil;
import com.mybank.app.util.ResponseParser;

@Configuration
@Component
@RestController
@RequestMapping(value = "/v1/employee")
public class EmployeeController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private JSONUtil jsonUtil;

	@Autowired
	private ResponseParser responseParser;

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> addBankEmployee(@RequestBody Employee employee) {
		this.LOGGER.info("add employee called with input {} ", employee.toString());
		try {
			Employee dbEmployee = this.employeeService.addNewBankEmployee(employee);
			JSONObject result = jsonUtil.getJsonObjectFromObject(dbEmployee);
			this.LOGGER.debug("updated employee {} ", employee.toString());
			return new ResponseEntity<Object>(result.toString(), HttpStatus.CREATED);
		} catch (Exception e) {
			this.LOGGER.error("Error occured in add employee {} ", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteBankEmployee(@RequestParam("employeeId") Long employeeId) {
		this.LOGGER.info("delete employee request came for employee id {} ", employeeId);
		try {
			this.employeeService.deleteEmployee(employeeId);
			this.LOGGER.info("successfully soft deleted employee with id {} ", employeeId);
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.OK.value(),
					"Successfully soft Deleted Employee with id " + employeeId,
					"Successfully soft Deleted Employee with id " + employeeId), HttpStatus.OK);
		} catch (BankException ex) {
			this.LOGGER.info("Error occured in delete employee  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
