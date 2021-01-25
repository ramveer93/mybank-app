package com.mybank.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.jsonwebtoken.lang.Assert;

@Entity
@Table(name = "bank")
public class Bank {

	@Id
	@Column(name = "bank_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankId;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "bank_address")
	private String bankAddress;

	@Column(name = "ifsc_code")
	private String ifscCode;

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

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
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

	@Override
	public String toString() {
		return "Bank [bankId=" + bankId + ", bankName=" + bankName + ", bankAddress=" + bankAddress + ", ifscCode="
				+ ifscCode + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", deleted=" + deleted + "]";
	}

	public void validateInput() {
		try {
			Assert.notNull(this, "Bank can't be null or empty");
			Assert.hasLength(this.getBankAddress(), "Bank address can't be null or empty");
			Assert.hasLength(this.getBankName(), "Bank name can't be null or empty");
			Assert.hasLength(this.getIfscCode(), "Bank ifsc code can't be null or empty");
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
