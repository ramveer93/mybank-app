package com.mybank.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_type_codes")
public class TransactionTypeCodes {

	@Id
	@Column(name = "transaction_code")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionCode;
	
	@Column(name = "transaction_code_name")
	private String transactionCodeName;

	public Long getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(Long transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionCodeName() {
		return transactionCodeName;
	}

	public void setTransactionCodeName(String transactionCodeName) {
		this.transactionCodeName = transactionCodeName;
	}

	@Override
	public String toString() {
		return "TransactionTypeCodes [transactionCode=" + transactionCode + ", transactionCodeName="
				+ transactionCodeName + "]";
	}
}
