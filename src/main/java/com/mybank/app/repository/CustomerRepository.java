package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
}
