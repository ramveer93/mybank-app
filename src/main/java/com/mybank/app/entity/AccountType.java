package com.mybank.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.jsonwebtoken.lang.Assert;

@Entity
@Table(name = "acc_type")
public class AccountType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acc_code")
	private Long code;

	@Column(name = "name")
	private String name;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AccountType [code=" + code + ", name=" + name + "]";
	}

	public void validateInput() {
		try {
			Assert.notNull(this, "AccountType can't be null or empty");
			Assert.hasLength(this.getName(), "AccountType name can't be null or empty");
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
