package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Account;
import com.mybank.app.entity.Bank;
import com.mybank.app.entity.Customer;
import com.mybank.app.repository.AccountRepository;
import com.mybank.app.repository.BankRepository;
import com.mybank.app.repository.CustomerRepository;
import com.mybank.app.util.BankException;

@Service
public class CustomerService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private BankRepository bankRepo;

	@Autowired
	private AccountRepository accRepo;

	public Customer addNewCustomer(Customer customer) {
		if (customer.getId() != null && customer.getId() != 0) {
			Optional<Customer> customerFromDb = this.customerRepo.findById(customer.getId());
			if (customerFromDb.isPresent()) {
				Customer dbCustomer = customerFromDb.get();
				customer.setId(dbCustomer.getId());
				customer.setUpdatedOn(LocalDateTime.now());
				customer.setCreatedOn(dbCustomer.getCreatedOn());
				customer.setAccounts(dbCustomer.getAccounts());
				customer.setBank(dbCustomer.getBank());
				customer.setKyc(dbCustomer.getKyc());
				customer = this.customerRepo.save(customer);
				return customer;
			}
		}
		Bank inputBank = customer.getBank();
		if (inputBank != null && inputBank.getBankId() != null && inputBank.getBankId() != 0) {
			// update bank dont create new
			LOGGER.info("Found bankId in input so new bank won't be created");
			Optional<Bank> bankFromDb = this.bankRepo.findById(inputBank.getBankId());
			if (bankFromDb.isPresent()) {
				inputBank = bankFromDb.get();
				customer.setBank(inputBank);
				LOGGER.info("Successfully attached the customer {} to bank {} ", customer.getCustomerFirstName(),
						inputBank.toString());
			} else {
				throw new BankException(
						"Bank with given bankId " + inputBank.getBankId() + " not found, No record will be created ");
			}
		}
		customer = this.customerRepo.save(customer);
		return customer;

	}

	public void deleteCustomer(Long customerId) {
		Optional<Customer> customerFromDb = this.customerRepo.findById(customerId);
		if (customerFromDb.isPresent()) {
			Customer customer = customerFromDb.get();
			customer.setDeleted(LocalDateTime.now());
			customer.setUpdatedOn(LocalDateTime.now());
			this.customerRepo.save(customer);
		} else {
			throw new BankException("Customer with given id doesn't exist");
		}

	}

	public Customer getCustomerDetails(Long customerId) {
		Optional<Customer> customer = this.customerRepo.findById(customerId);
		if (customer.isPresent()) {
			return customer.get();
		} else {
			throw new BankException("No customer found with given id");
		}
	}

	public Customer linkCustomerWithAccounts(Long customerId, List<Long> accountIds) {
		Optional<Customer> customer = this.customerRepo.findById(customerId);
		Customer customerFromDb = null;
		if (customer.isPresent()) {
			customerFromDb = customer.get();
			List<Account> accounts = new ArrayList<>();
			for (Long acId : accountIds) {
				Optional<Account> accounFromDb = this.accRepo.findById(acId);
				if (accounFromDb.isPresent()) {
					accounts.add(accounFromDb.get());
				} else {
					throw new BankException("No account found with given id  " + acId);
				}
				customerFromDb.setAccounts(accounts);
				customerFromDb = this.customerRepo.save(customerFromDb);
			}
		} else {
			throw new BankException("No customer found with given id");
		}
		return customerFromDb;
	}
}
