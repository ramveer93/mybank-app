package com.mybank.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Employee;
import com.mybank.app.entity.EmployeeRole;
import com.mybank.app.repository.EmployeeRepository;

@Service
public class AuthorizedService {

	@Autowired
	private EmployeeRepository empRepo;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public void authorizeUser() {
		this.LOGGER.info("Request came to perform authorization of logged in user");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUser = authentication.getName();
		this.LOGGER.info("Current logged in user {} ", loggedInUser);
		Employee loggedInEmployee = this.empRepo.findEmployeeByEmployeeUserName(loggedInUser);
		Set<EmployeeRole> roles = loggedInEmployee.getRoles();
		List<String> list = new ArrayList<>();
		for (EmployeeRole role : roles) {
			list.add(role.getName());
		}
		if (!list.contains("ADMIN")) {
			this.LOGGER.error("logged in user {} doesn't have ADMIN role so not allowed to perform this operation ",
					loggedInUser);
			throw new InsufficientAuthenticationException(
					"Logged in user doesn't have role as ADMIN Hence not allowed to perform this request");
		}
		this.LOGGER.info("logged user {} is authorized successfully !!", loggedInUser);
	}
}
