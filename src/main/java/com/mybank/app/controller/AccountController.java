package com.mybank.app.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.mybank.app.entity.Account;
import com.mybank.app.service.AccountService;
import com.mybank.app.service.AuthorizedService;
import com.mybank.app.util.DateUtil;
import com.mybank.app.util.JSONUtil;
import com.mybank.app.util.ResponseParser;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JSONUtil jsonUtil;

	@Autowired
	private ResponseParser responseParser;

	@Autowired
	private AuthorizedService authorizedService;
    
	@Autowired
	private AccountService accService;
	

	@RequestMapping(value = "/createAccount", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> createNewAccount(@RequestBody Account account) {
		this.LOGGER.info("request came to add new account with details {} ", account.toString());
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			account.validateInput();
			account = this.accService.createNewAccount(account);
			JSONObject obj = this.jsonUtil.getJsonObjectFromObject(account);
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
			this.LOGGER.error("Error occured in add customer {} ", e.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
}
