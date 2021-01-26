package com.mybank.app.controller;

import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.app.entity.Customer;
import com.mybank.app.entity.KYC;
import com.mybank.app.service.AuthorizedService;
import com.mybank.app.service.CustomerService;
import com.mybank.app.util.BankException;
import com.mybank.app.util.JSONUtil;
import com.mybank.app.util.ResponseParser;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthorizedService authorizedService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JSONUtil jsonUtil;

	@Autowired
	private ResponseParser responseParser;

	@RequestMapping(value = "/addCustomer", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
		try {
			this.LOGGER.info("request came to add new customer with details {} ", customer.toString());
			this.authorizedService.authorizeUser("EMPLOYEE");
			customer.validateInput();
			Customer customerFromDb = this.customerService.addNewCustomer(customer);
			JSONObject jsonObj = jsonUtil.getJsonObjectFromObject(customerFromDb);
			this.LOGGER.info("Successfully created new customer");
			return new ResponseEntity<>(jsonObj.toString(), HttpStatus.CREATED);
		} catch (IllegalArgumentException ie) {
			this.LOGGER.error(ie.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.BAD_REQUEST.value(), ie.getMessage(), ie.getMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			this.LOGGER.error("Error occured in add customer {} ", e.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/deleteCustomer", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteCustomer(@RequestParam("customerId") Long customerId) {
		this.LOGGER.info("req came for deleting customer with id {} ", customerId);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			this.customerService.deleteCustomer(customerId);
			this.LOGGER.info("Successfully delete customer with id {} ", customerId);
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.OK.value(),
					"Successfully deleted customer with id: " + customerId,
					"Successfully deleted customer with id: " + customerId), HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (BankException ex) {
			this.LOGGER.info("Error occured in delete customer  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getCustomer", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getCustomerDetails(@RequestParam("customerId") Long customerId) {
		this.LOGGER.info("req to get customer with id {} ", customerId);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			Customer customer = this.customerService.getCustomerDetails(customerId);
			JSONObject obj = this.jsonUtil.getJsonObjectFromObject(customer);
			return new ResponseEntity<Object>(obj.toString(), HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			this.LOGGER.info("Error occured in get customer  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/linkCustomerWithAccount", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> linkCustomerWithAccount(@RequestParam("customerId") Long customerId,
			@RequestParam("accountIds") Set<Long> accountIds) {
		this.LOGGER.info("req came for linking customer with account id {} ", customerId);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			Customer updatedCustomer = this.customerService.linkCustomerWithAccounts(customerId, accountIds);
			JSONObject obj = this.jsonUtil.getJsonObjectFromObject(updatedCustomer);
			return new ResponseEntity<Object>(obj.toString(), HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			this.LOGGER.info("Error occured in linkCustomerWithAccount  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/updateKyc", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateCustomerKYC(@RequestParam("customerId") Long customerId, @RequestBody KYC kyc) {
		this.LOGGER.info("Req to updateKyc for customer id {} ",customerId);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			kyc.validateInput();
			Customer updatedCustomer = this.customerService.updateKYC(customerId,kyc);
			JSONObject obj = this.jsonUtil.getJsonObjectFromObject(updatedCustomer);
			return new ResponseEntity<Object>(obj.toString(), HttpStatus.OK);
		} catch (IllegalArgumentException ie) {
			this.LOGGER.error(ie.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.BAD_REQUEST.value(), ie.getMessage(), ie.getMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			this.LOGGER.error("Error occured in update customer kyc {} ", e.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
