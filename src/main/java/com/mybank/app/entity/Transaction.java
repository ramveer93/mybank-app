package com.mybank.app.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trans_id")
	private Long id;

	@Column(name = "amount")
	private double amount;

	@Column(name = "status")
	private String status;

	@Column(name = "receipt")
	private String receipt;

	@Column(name = "is_credit")
	private boolean isCredit;
	
	@Column(name = "is_debit")
	private boolean isDebit;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "trans_trans_type_codes")
	private TransactionTypeCodes transTypeCodes;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "trans_acc_id")
	private Account account;

	@Column(name = "created_on")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime createdOn = LocalDateTime.now();

	@Column(name = "updated_on")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime updatedOn;

	@Column(name = "deleted")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime deleted;

	@Column(name = "end_date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime transactionEndDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
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

	public TransactionTypeCodes getTransTypeCodes() {
		return transTypeCodes;
	}

	public void setTransTypeCodes(TransactionTypeCodes transTypeCodes) {
		this.transTypeCodes = transTypeCodes;
	}

	public LocalDateTime getTransactionEndDate() {
		return transactionEndDate;
	}

	public void setTransactionEndDate(LocalDateTime transactionEndDate) {
		this.transactionEndDate = transactionEndDate;
	}

	public boolean isCredit() {
		return isCredit;
	}

	public void setCredit(boolean isCredit) {
		this.isCredit = isCredit;
	}

	public boolean isDebit() {
		return isDebit;
	}

	public void setDebit(boolean isDebit) {
		this.isDebit = isDebit;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", amount=" + amount + ", status=" + status + ", receipt=" + receipt
				+ ", isCredit=" + isCredit + ", isDebit=" + isDebit + ", transTypeCodes=" + transTypeCodes
				+ ", account=" + account + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", deleted="
				+ deleted + ", transactionEndDate=" + transactionEndDate + "]";
	}


	

}
