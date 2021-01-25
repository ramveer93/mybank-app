package com.mybank.app.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mybank.app.security.AuthRequest;
import com.mybank.app.security.JwtUtil;
import com.mybank.app.service.AuthorizedService;
import com.mybank.app.util.ResponseParser;

@Configuration
@Component
@RestController
@RequestMapping(value = "/v1/authentication")
public class AuthenticationController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private ResponseParser responseParser;

	@Autowired
	private AuthorizedService authService;

	@RequestMapping(value = "/token", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> getJWTToken(@RequestBody AuthRequest authRequest) {
		this.LOGGER.info("get jwt token req came with authReq {} ", authRequest);
		try {
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			this.LOGGER.info("user successfully authenticated , generating jwt ");
			String token = this.jwtUtil.generateToken(authRequest.getUsername());
			JSONObject result = new JSONObject();
			result.put("token", token);
			result.put("status", "success");
			result.put("expiredOn", this.jwtUtil.extractExpiration(token));
			result.put("userName", this.jwtUtil.extractUsername(token));
			this.LOGGER.info(" successfully generated jwt");
			return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
		} catch (Exception ex) {
			this.LOGGER.error(" Error generating jwt {} ", ex.getMessage());
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> logout() {
		try {
			this.authService.logoutUser();
			return new ResponseEntity<Object>(this.responseParser.build(HttpStatus.OK.value(),
					"Successfully logged out", "Successfully logged out"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(
					this.responseParser.build(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
