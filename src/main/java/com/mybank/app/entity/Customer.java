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
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "c_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_ac_id", referencedColumnName = "c_id")
	private List<Account> accounts = new ArrayList<>();

	@Column(name = "c_first_name")
	private String customerFirstName;

	@Column(name = "c_middle_name")
	private String customerMiddleName;

	@Column(name = "c_last_name")
	private String customerLastName;

	@Column(name = "c_address")
	private String customerAddress;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "c_banks_id", referencedColumnName = "c_id")
	private List<Bank> banks;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "cust_kyc")
	private KYC kyc;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerMiddleName() {
		return customerMiddleName;
	}

	public void setCustomerMiddleName(String customerMiddleName) {
		this.customerMiddleName = customerMiddleName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
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

	public KYC getKyc() {
		return kyc;
	}

	public void setKyc(KYC kyc) {
		this.kyc = kyc;
	}

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", accounts=" + accounts + ", customerFirstName=" + customerFirstName
				+ ", customerMiddleName=" + customerMiddleName + ", customerLastName=" + customerLastName
				+ ", customerAddress=" + customerAddress + ", banks=" + banks + ", kyc=" + kyc + ", createdOn="
				+ createdOn + ", updatedOn=" + updatedOn + ", deleted=" + deleted + "]";
	}
    
	public void validateInput() {
		try {
			Assert.notNull(this, "Customer object can't be null");
			Assert.hasLength(this.getCustomerAddress(), "Customer address can't be null or empty ");
			Assert.hasLength(this.getCustomerFirstName(), "Customer first name can't be null or empty");
			Assert.hasLength(this.getCustomerLastName(), "Customer last name can't be null or empty");
			Assert.isTrue(!this.getBanks().isEmpty(), "Customer must have atleast one bank ");
			List<Bank> banks = this.getBanks();
			for (Bank bank : banks) {
				Assert.hasLength(bank.getBankAddress(), "Bank must have an address");
				Assert.hasLength(bank.getBankName(), "Bank must have a name");
				Assert.hasLength(bank.getIfscCode(), "Bank must have an IFSC code");
			}

		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

}
