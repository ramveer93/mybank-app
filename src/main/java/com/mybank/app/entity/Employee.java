package com.mybank.app.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@Column(name = "employee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "empl_role", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<EmployeeRole> roles = new HashSet<>();

	@Column(name = "employee_user_name")
	private String employeeUserName;

	@Column(name = "bank_id")
	private Long bankId;

	@Column(name = "designation")
	private String designation;

	@Column(name = "first_name")
	private String employeeFirstName;

	@Column(name = "middle_name")
	private String employeeMiddleName;

	@Column(name = "last_name")
	private String employeeLastName;

	@Column(name = "password")
	private String password;

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

	public Set<EmployeeRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<EmployeeRole> roles) {
		this.roles = roles;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeMiddleName() {
		return employeeMiddleName;
	}

	public void setEmployeeMiddleName(String employeeMiddleName) {
		this.employeeMiddleName = employeeMiddleName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmployeeUserName() {
		return employeeUserName;
	}

	public void setEmployeeUserName(String employeeUserName) {
		this.employeeUserName = employeeUserName;
	}

	public void validateInput() {
		try {
			Assert.notNull(this, "Employee object can't be null");
			Assert.hasLength(this.getDesignation(), "Employee's designation can't be null or empty");
			Assert.hasLength(this.getEmployeeFirstName(), "Employee's first name can't be null or empty");
			Assert.hasLength(this.getEmployeeLastName(), "Employee's last name can't be null or empty");
			Assert.hasLength(this.getPassword(), "Employee's password can't be null");
			Assert.notNull(this.getRoles(), "Employee's role can't be null");
			Assert.isTrue(!this.getRoles().isEmpty(), "Employee's role array can't be empty");
			Assert.isTrue(this.getEmployeeFirstName().length() >= 3,
					"Employee's first name mush have length at least 3");
			Assert.isTrue(this.getEmployeeLastName().length() >= 3, "Employee's last name mush have length at least 3");
			Set<EmployeeRole> roles = this.getRoles();
			for (EmployeeRole role : roles) {
				Assert.hasLength(role.getName(), "Employee's role name can't be null or empty");
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());

		}
	}

	public void setEmployeeUserName() {
		String employeeUserName = this.getEmployeeFirstName().substring(0, 3)
				+ this.getEmployeeLastName().substring(0, 3) + this.getId();
		this.setEmployeeUserName(employeeUserName);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", roles=" + roles + ", employeeUserName=" + employeeUserName + ", bankId="
				+ bankId + ", designation=" + designation + ", employeeFirstName=" + employeeFirstName
				+ ", employeeMiddleName=" + employeeMiddleName + ", employeeLastName=" + employeeLastName
				+ ", password=" + password + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", deleted="
				+ deleted + "]";
	}

	

}
