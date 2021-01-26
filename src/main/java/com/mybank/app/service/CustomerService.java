package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Account;
import com.mybank.app.entity.Bank;
import com.mybank.app.entity.Customer;
import com.mybank.app.entity.Document;
import com.mybank.app.entity.KYC;
import com.mybank.app.repository.AccountRepository;
import com.mybank.app.repository.BankRepository;
import com.mybank.app.repository.CustomerRepository;
import com.mybank.app.repository.DocumentRepository;
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

	@Autowired
	private DocumentRepository docRepo;

	/**
	 * The method will add new customer to db if customerId passed from input
	 * existing customer will be updated
	 * 
	 * @param customer
	 * @return
	 */
	public Customer addNewCustomer(Customer customer) {
		this.LOGGER.debug("adding customer to db {} ", customer.toString());
		if (customer.getId() != null && customer.getId() != 0) {
			Optional<Customer> customerFromDb = this.customerRepo.findById(customer.getId());
			if (customerFromDb.isPresent()) {
				this.LOGGER.info("existing customer found so update will be performed instead of create");
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
			LOGGER.info("Found bankId in input so new bank won't be created");
			Optional<Bank> bankFromDb = this.bankRepo.findById(inputBank.getBankId());
			if (bankFromDb.isPresent()) {
				inputBank = bankFromDb.get();
				customer.setBank(inputBank);
				LOGGER.info("Successfully attached the customer {} to bank {} ", customer.getCustomerFirstName(),
						inputBank.toString());
			} else {
				this.LOGGER.error("Bank with given bankId {} not found ", inputBank.getBankId());
				throw new BankException(
						"Bank with given bankId " + inputBank.getBankId() + " not found, No record will be created ");
			}
		}
		customer = this.customerRepo.save(customer);
		this.LOGGER.info("Successfully updated customer new id is {} ", customer.getId());
		return customer;
	}

	/**
	 * The method will delete existing customer if no customer found it will throw
	 * exception Please not this is just soft delete and deleted column will be
	 * update with current date, no actual record will be delete
	 * 
	 * @param customerId
	 */
	public void deleteCustomer(Long customerId) {
		Optional<Customer> customerFromDb = this.customerRepo.findById(customerId);
		if (customerFromDb.isPresent()) {
			this.LOGGER.info("Existing customer found in db s deleting it ");
			Customer customer = customerFromDb.get();
			customer.setDeleted(LocalDateTime.now());
			customer.setUpdatedOn(LocalDateTime.now());
			this.customerRepo.save(customer);
			this.LOGGER.info("Successfully marked this customer as deleted ");
		} else {
			this.LOGGER.error("Customer with given id doesn't exist");
			throw new BankException("Customer with given id doesn't exist");
		}

	}

	/**
	 * The method will return customer from the db for given customer id
	 * 
	 * @param customerId
	 * @return
	 */
	public Customer getCustomerDetails(Long customerId) {
		Optional<Customer> customer = this.customerRepo.findById(customerId);
		if (customer.isPresent()) {
			return customer.get();
		} else {
			this.LOGGER.error("No customer found with given id");
			throw new BankException("No customer found with given id");
		}
	}

	/**
	 * The method will link a customer with accounts It will update the customer
	 * table with accounts
	 * 
	 * @param customerId
	 * @param accountIds
	 * @return
	 */
	public Customer linkCustomerWithAccounts(Long customerId, Set<Long> accountIds) {
		Optional<Customer> customer = this.customerRepo.findById(customerId);
		Customer customerFromDb = null;
		if (customer.isPresent()) {
			this.LOGGER.info("customer found for given id {} so linking to account", customerId);
			customerFromDb = customer.get();
			Set<Account> accounts = new HashSet<>();
			for (Long acId : accountIds) {
				Optional<Account> accounFromDb = this.accRepo.findById(acId);
				if (accounFromDb.isPresent()) {
					accounts.add(accounFromDb.get());
				} else {
					this.LOGGER.error("No account found with given id {}", acId);
					throw new BankException("No account found with given id  " + acId);
				}
				customerFromDb.getAccounts().addAll(accounts);
				customerFromDb = this.customerRepo.save(customerFromDb);
			}
		} else {
			this.LOGGER.error("No customer found with given id");
			throw new BankException("No customer found with given id");
		}
		return customerFromDb;
	}

	/**
	 * The method will update the kyc details of the customer it can also update kyc
	 * when the document is already existing in this case the caller has to pass the
	 * document id instead of document object
	 * 
	 * @param customerId
	 * @param kyc
	 * @return
	 */
	public Customer updateKYC(Long customerId, KYC kyc) {
		Optional<Customer> customer = this.customerRepo.findById(customerId);
		Customer customerFromDb = null;
		if (customer.isPresent()) {
			this.LOGGER.info("customer for given id {} found in db ", customerId);
			customerFromDb = customer.get();
			KYC kycFromDb = customerFromDb.getKyc();
			kyc.setKycId(kycFromDb.getKycId());
			kyc.setUpdatedOn(LocalDateTime.now());
			Set<Document> inputDocs = kyc.getDocuments();
			for (Document doc : inputDocs) {
				if (doc != null && doc.getDocId() != null && doc.getDocId() != 0) {
					Optional<Document> docFromDb = this.docRepo.findById(doc.getDocId());
					if (docFromDb.isPresent()) {
						Document dbDoc = docFromDb.get();
						kyc.getDocuments().remove(doc);
						kyc.getDocuments().add(dbDoc);
					} else {
						this.LOGGER.error("No Document found with given id {} ", doc.getDocId());
						throw new BankException("No Document found with given id " + doc.getDocId());
					}
				}
			}
			customerFromDb.setKyc(kyc);
			customerFromDb = this.customerRepo.save(customerFromDb);
		} else {
			this.LOGGER.info("No customer found with given id");
			throw new BankException("No customer found with given id");
		}
		return customerFromDb;
	}
}
