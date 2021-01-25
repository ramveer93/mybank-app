package com.mybank.app.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Employee;
import com.mybank.app.repository.EmployeeRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private EmployeeRepository empRepo;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = this.empRepo.findEmployeeByEmployeeUserName(username);
		if(employee == null) {
			this.LOGGER.error("There is no use with username {} found , forbidden ",username);
		}
		return new org.springframework.security.core.userdetails.User(employee.getEmployeeUserName(),
				employee.getPassword(), new ArrayList<>());
	}

}
