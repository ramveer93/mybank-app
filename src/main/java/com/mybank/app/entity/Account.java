package com.mybank.app.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@Column(name = "acc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long AccId;

	@Column(name = "balance")
	private double balance;

	@Column(name = "interest_rate")
	private float interestRate;

	@Column(name = "status")
	private String status;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "acc_bank_id")
	private Bank bank;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "acc_ac_code_id")
	private AccountType accType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ac_transaction_id", referencedColumnName = "acc_id")
	private List<Transaction> transactions = new ArrayList<>();

	@Column(name = "created_on")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn = LocalDateTime.now();

	@Column(name = "updated_on")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedOn;

	@Column(name = "deleted")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deleted;

	public Long getAccId() {
		return AccId;
	}

	public void setAccId(Long accId) {
		AccId = accId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public float getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AccountType getAccType() {
		return accType;
	}

	public void setAccType(AccountType accType) {
		this.accType = accType;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public LocalDateTime getDeleted() {
		return deleted;
	}

	public void setDeleted(LocalDateTime deleted) {
		this.deleted = deleted;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	@Override
	public String toString() {
		return "Account [AccId=" + AccId + ", balance=" + balance + ", interestRate=" + interestRate + ", status="
				+ status + ", bank=" + bank + ", accType=" + accType + ", transactions=" + transactions + ", createdOn="
				+ createdOn + ", updatedOn=" + updatedOn + ", deleted=" + deleted + "]";
	}

	public void validateInput() {
		try {
			Assert.notNull(this, "Account can't be null or empty");
			Assert.hasLength(this.getStatus(), "Account status can't be null or empty");
			Assert.notNull(this.getAccType(), "Account type can't be null or empty");
			Assert.hasLength(this.getAccType().getName(), "Account type name can't be null or empty");
			Assert.notNull(this.getBank(), "Account bank can't be null or empty");
			Assert.hasLength(String.valueOf(this.getInterestRate()), "Account interest rate can't be null or empty");
			Assert.hasLength(this.getBank().getBankAddress(), "Bank address can't be null or empty");
			Assert.hasLength(this.getBank().getBankName(), "Bank name can't be null or empty");
			Assert.hasLength(this.getBank().getIfscCode(), "Bank ifsc code can't be null or empty");
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
