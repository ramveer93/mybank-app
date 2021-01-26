package com.mybank.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybank.app.entity.Account;
import com.mybank.app.entity.AccountType;
import com.mybank.app.entity.Bank;
import com.mybank.app.repository.AccountRepository;
import com.mybank.app.repository.AccountTypeCodeRepository;
import com.mybank.app.repository.BankRepository;
import com.mybank.app.util.BankException;

@Service
public class AccountService {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private BankRepository bankRepo;

	@Autowired
	private AccountTypeCodeRepository accTypeCodeRepo;

	public Account createNewAccount(Account account) {
		this.LOGGER.info("Account service got request to create new account");
		if (account.getAccId() != null && account.getAccId() != 0) {
			this.LOGGER.info(
					"Account id {} is not null or zero so considering this request to update existing account ",
					account.getAccId());
			Optional<Account> acFromDb = this.accountRepo.findById(account.getAccId());
			if (acFromDb.isPresent()) {
				this.LOGGER.info("Existing account found with acc id so updating this");
				Account dbAccount = acFromDb.get();
				account.setAccId(dbAccount.getAccId());
				account.setBank(dbAccount.getBank());
				account.setUpdatedOn(LocalDateTime.now());
				account.setCreatedOn(dbAccount.getCreatedOn());
				account.setAccType(dbAccount.getAccType());
				account.setTransactions(dbAccount.getTransactions());
				account = this.accountRepo.save(account);
				this.LOGGER.info("Successfully updated existing account");
				return account;
			}
		}

		AccountType accType = account.getAccType();

		if (accType != null && accType.getCode() != null && accType.getCode() != 0) {
			// acc type code found so not creating new type
			LOGGER.info("Found AccountTypeCode in req so not creating new ac type");
			Optional<AccountType> accountFromDb = this.accTypeCodeRepo.findById(accType.getCode());
			if (accountFromDb.isPresent()) {
				accType = accountFromDb.get();
				account.setAccType(accType);
				LOGGER.info("Successfully attached account {} to type code {} ", account.toString(), accType.getCode());
			} else {
				throw new BankException(
						"Account with given accId " + account.getAccId() + " not found, No record will be created ");
			}

		}
		Bank inputBank = account.getBank();
		if (inputBank != null && inputBank.getBankId() != null && inputBank.getBankId() != 0) {
			// update bank dont create new
			LOGGER.info("Found bankId in input so new bank won't be created");
			Optional<Bank> bankFromDb = this.bankRepo.findById(inputBank.getBankId());
			if (bankFromDb.isPresent()) {
				inputBank = bankFromDb.get();
				account.setBank(inputBank);
				LOGGER.info("Successfully attached the employee {} to bank {} ", account.getAccId(),
						inputBank.toString());
			} else {
				throw new BankException(
						"Bank with given bankId " + inputBank.getBankId() + " not found, No record will be created ");
			}
		}

		account = this.accountRepo.save(account);
		this.LOGGER.info("Successfully updated existing account");
		return account;
	}

	public JSONObject getAccountBalance(Long accountId) {
		Optional<Account> accountFromDb = this.accountRepo.findById(accountId);
		if (accountFromDb.isPresent()) {
			Account dbAccount = accountFromDb.get();
			JSONObject result = new JSONObject();
			double balance = dbAccount.getBalance();
			result.put("balance", balance);
			result.put("accountType", dbAccount.getAccType().getName());
			return result;
		} else {
			throw new BankException("Account with given id " + accountId + " not found, No record will be created ");
		}
	}

}
