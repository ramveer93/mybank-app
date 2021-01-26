package com.mybank.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mybank.app.entity.Account;
import com.mybank.app.entity.Transaction;
import com.mybank.app.service.AccountService;
import com.mybank.app.service.AuthorizedService;
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
			this.LOGGER.error("Error occured in create account {} ", e.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/getBalance", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getAccountBalance(@RequestParam("accountId") Long accountId) {
		this.LOGGER.info("Request came for getAccountBalance with acId {} ",accountId);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			JSONObject result = this.accService.getAccountBalance(accountId);
			this.LOGGER.info("Successfully returned account balance with acId {} ",accountId);
			return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			this.LOGGER.info("Error occured in get balance  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/transferMoney", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> transferMoneyToAccount(@RequestParam("sourceAccountId") Long sourceAccountId,
			@RequestParam("targetAccountId") Long targetAccountId, @RequestParam("money") double money) {
			this.LOGGER.info("Request came for transfer money with source ac {}, targtet ac {} and money {}",sourceAccountId,targetAccountId,money);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			JSONObject result = this.accService.transferMoneyToAccount(sourceAccountId, targetAccountId, money);
			this.LOGGER.info("Successfully transfered money!!");
			return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			this.LOGGER.info("Error occured in transfering money  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/accountStatement", method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<Object> printAccountStatement(@RequestParam("accountId") Long accountId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		this.LOGGER.info("Req came to get account statement for start date {},endDate {} ",startDate,endDate);
		try {
			this.authorizedService.authorizeUser("EMPLOYEE");
			List<Transaction> list = this.accService.printAccountStatement(accountId, startDate, endDate);
			Gson gson = new Gson();
			String json = gson.toJson(list);
			return new ResponseEntity<Object>(json, HttpStatus.OK);
		} catch (InsufficientAuthenticationException in) {
			this.LOGGER.error(in.getMessage());
			return new ResponseEntity<>(
					this.responseParser.build(HttpStatus.UNAUTHORIZED.value(), in.getMessage(), in.getMessage()),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			this.LOGGER.info("Error occured in account statement  {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping("/generateReport")
	public void generatePDFReport(HttpServletResponse response, @RequestParam("accountId") Long accountId,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		this.LOGGER.info("Req came to generate pdf report for account statement ");
		response = this.accService.printReport(accountId, startDate, endDate, response);
		this.LOGGER.info("Successfully generated pdf report  ");
	}

}
