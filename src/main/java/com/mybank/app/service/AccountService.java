package com.mybank.app.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mybank.app.entity.Account;
import com.mybank.app.entity.AccountType;
import com.mybank.app.entity.Bank;
import com.mybank.app.entity.Transaction;
import com.mybank.app.entity.TransactionTypeCodes;
import com.mybank.app.reporting.ReportingUtil;
import com.mybank.app.repository.AccountRepository;
import com.mybank.app.repository.AccountTypeCodeRepository;
import com.mybank.app.repository.BankRepository;
import com.mybank.app.repository.TransactionRepository;
import com.mybank.app.util.BankException;
import com.mybank.app.util.Constants;
import com.mybank.app.util.JSONUtil;

@Service
public class AccountService {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private BankRepository bankRepo;

	@Autowired
	private AccountTypeCodeRepository accTypeCodeRepo;

	@Autowired
	private JSONUtil jsonUtil;

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private ReportingUtil reportUtil;

	/**
	 * This method will create a new account if account id is passed from input it
	 * will try to find the account in db and update it
	 * 
	 * @Account: input account obj
	 */
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
				account = this.accountRepo.save(account);
				this.LOGGER.info("Successfully updated existing account");
				return account;
			}
		}
		AccountType accType = account.getAccType();
		if (accType != null && accType.getCode() != null && accType.getCode() != 0) {
			LOGGER.info("Found AccountTypeCode in req so not creating new ac type");
			Optional<AccountType> accountFromDb = this.accTypeCodeRepo.findById(accType.getCode());
			if (accountFromDb.isPresent()) {
				accType = accountFromDb.get();
				account.setAccType(accType);
				LOGGER.info("Successfully attached account {} to type code {} ", account.toString(), accType.getCode());
			} else {
				LOGGER.error("Account with given accId {} not found , No record will be created ", account.getAccId());
				throw new BankException(
						"Account with given accId " + account.getAccId() + " not found, No record will be created ");
			}
		}
		Bank inputBank = account.getBank();
		if (inputBank != null && inputBank.getBankId() != null && inputBank.getBankId() != 0) {
			LOGGER.info("Found bankId in input so new bank won't be created");
			Optional<Bank> bankFromDb = this.bankRepo.findById(inputBank.getBankId());
			if (bankFromDb.isPresent()) {
				inputBank = bankFromDb.get();
				account.setBank(inputBank);
				LOGGER.info("Successfully attached the employee {} to bank {} ", account.getAccId(),
						inputBank.toString());
			} else {
				this.LOGGER.error("Bank with given bankId  {} not found ", inputBank.getBankId());
				throw new BankException(
						"Bank with given bankId " + inputBank.getBankId() + " not found, No record will be created ");
			}
		}
		account = this.accountRepo.save(account);
		this.LOGGER.info("Successfully updated existing account");
		return account;
	}

	/**
	 * This method will get account balance
	 */
	@Transactional(readOnly = true)
	public JSONObject getAccountBalance(Long accountId) {
		this.LOGGER.info("get account balance in service for {} ", accountId);
		Optional<Account> accountFromDb = this.accountRepo.findById(accountId);
		if (accountFromDb.isPresent()) {
			this.LOGGER.info("account with id {} found in db ", accountId);
			Account dbAccount = accountFromDb.get();
			JSONObject result = new JSONObject();
			double balance = dbAccount.getBalance();
			result.put("balance", balance);
			result.put("accountType", dbAccount.getAccType().getName());
			return result;
		} else {
			this.LOGGER.error("account with id {} not found in db ", accountId);
			throw new BankException("Account with given id " + accountId + " not found, No record will be created ");
		}
	}

	/**
	 * This method will transfer money from one account to another by transaction
	 * 
	 */
	@Transactional
	public JSONObject transferMoneyToAccount(Long sourceAccountId, Long targetAccountId, double money) {
		Optional<Account> sourceDbAcc = this.accountRepo.findById(sourceAccountId);
		Optional<Account> targetDbAcc = this.accountRepo.findById(targetAccountId);
		JSONObject result = new JSONObject();
		if (sourceDbAcc.isPresent() && targetDbAcc.isPresent() && money > 0) {
			Account sourceAccount = sourceDbAcc.get();
			Account targetAccount = targetDbAcc.get();
			if (money > sourceAccount.getBalance()) {
				this.LOGGER.error("Source account doesn't have sufficient fund to transfer");
				throw new BankException("Source account doesn't have sufficient fund to transfer");
			} else {
				this.LOGGER.info("Initiating transation to transfer money");
				Transaction debitTrans = new Transaction();
				debitTrans.setTransTypeCodes(new TransactionTypeCodes("Money Transfer Debited"));
				debitTrans.setAmount(money);
				debitTrans.setDebit(true);
				debitTrans.setReceipt("Receipt: Money of " + money + " got debited from your ac");
				debitTrans.setStatus("Success");
				debitTrans.setTransactionEndDate(LocalDateTime.now());
				debitTrans.setAccount(sourceAccount);
				this.LOGGER.info("commited source account transaction");

				debitTrans = this.transactionRepo.save(debitTrans);
				sourceAccount.setBalance(sourceAccount.getBalance() - money);
				sourceAccount.setUpdatedOn(LocalDateTime.now());
				sourceAccount = this.accountRepo.save(sourceAccount);
				this.LOGGER.info("successfully updated source ac details");
				// credit
				this.LOGGER.info("initiating transaction in target account");
				Transaction creditTrans = new Transaction();
				creditTrans.setTransTypeCodes(new TransactionTypeCodes("Money Transfer Credited"));
				creditTrans.setAmount(money);
				creditTrans.setCredit(true);
				creditTrans.setReceipt("Receipt: Money of " + money + " credited to your ac");
				creditTrans.setStatus("Success");
				creditTrans.setAccount(targetAccount);
				creditTrans.setTransactionEndDate(LocalDateTime.now());
				creditTrans = this.transactionRepo.save(creditTrans);
				this.LOGGER.info("successfully commited target account transaction");

				targetAccount.setBalance(targetAccount.getBalance() + money);
				targetAccount.setUpdatedOn(LocalDateTime.now());
				targetAccount = this.accountRepo.save(targetAccount);
				result.put("status", "Success");
				result.put("sourceTransactionId", debitTrans.getId());
				result.put("targetTransactionId", creditTrans.getId());
				this.LOGGER.info("successfully saved target account details");
				this.LOGGER.info(
						"transaction completed successfully with source transaction id {} and target transaction id {} ",
						debitTrans.getId(), creditTrans.getId());
				return result;
			}
		} else {
			this.LOGGER.error("Either source or target account doesn't exists,not able to make the transaction");
			throw new BankException("Either source or target account doesn't exists,not able to make the transaction");
		}
	}

	/**
	 * This method will run in a scheduled manner to update all the accounts with interest 
	 * rate they earned
	 * 
	 */
	@Scheduled(cron = "* */2 * * * *", zone = "Asia/Calcutta")
	public void calculateInterest() {
		LOGGER.info("Scheduler started at {} ", LocalDateTime.now());
		List<Account> accounts = this.accountRepo.findAll();
		LOGGER.info("total accounts found {} , schedular will update interest earned for every accont",
				accounts.size());
		for (Account account : accounts) {
			long daysDiff = Duration.between(account.getCreatedOn(), LocalDateTime.now()).toDays();
			double time = daysDiff / 365;
			double interestEarned = calculateInterest(account.getBalance(), time, Constants.INTEREST_RATE);
			LOGGER.info(" time {} , interest earned {}, balance before update {} ", time, interestEarned,
					account.getBalance());
			account.setBalance(account.getBalance() + interestEarned);
			account.setUpdatedOn(LocalDateTime.now());
			LOGGER.info("Balance after update {} ", account.getBalance());
			account = this.accountRepo.save(account);
		}
	}

	private double calculateInterest(double principal, double time, double rate) {
		return principal * time * rate / 100;
	}

	public List<Transaction> printAccountStatement(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
		List<Transaction> transactions = this.transactionRepo.findInDateRange(accountId, startDate);
		return transactions;
	}

	public HttpServletResponse printReport(Long accountId, LocalDateTime startDate, LocalDateTime endDate,
			HttpServletResponse response) {
		List<Transaction> transactions = this.transactionRepo.findInDateRange(accountId, startDate);
		return this.reportUtil.generateReport(transactions, response);
	}

}
