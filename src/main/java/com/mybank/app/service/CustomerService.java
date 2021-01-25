package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Customer;
import com.mybank.app.repository.CustomerRepository;
import com.mybank.app.util.BankException;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	public Customer addNewCustomer(Customer customer) {
		if (customer.getId() != null && customer.getId() != 0) {
			Optional<Customer> customerFromDb = this.customerRepo.findById(customer.getId());
			if (customerFromDb.isPresent()) {
				Customer dbCustomer = customerFromDb.get();
				customer.setId(dbCustomer.getId());
				customer.setUpdatedOn(LocalDateTime.now());
				customer.setCreatedOn(dbCustomer.getCreatedOn());
				customer.setAccounts(dbCustomer.getAccounts());
				customer.setBanks(dbCustomer.getBanks());
				customer.setKyc(dbCustomer.getKyc());
				customer = this.customerRepo.save(customer);
				return customer;
			}
		}
		customer = this.customerRepo.save(customer);
		return customer;

	}

	public void deleteCustomer(Long customerId) {
		Optional<Customer> customerFromDb = this.customerRepo.findById(customerId);
		if(customerFromDb.isPresent()) {
			Customer customer = customerFromDb.get();
			customer.setDeleted(LocalDateTime.now());
			customer.setUpdatedOn(LocalDateTime.now());
			this.customerRepo.save(customer);
		}else {
			throw new BankException("Customer with given id doesn't exist");
		}

	}
}
