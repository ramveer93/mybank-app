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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.jsonwebtoken.lang.Assert;

@Entity
@Table(name = "document")
public class Document {

	@Id
	@Column(name = "doc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docId;
	
	@Column(name  = "doc_name")
	private String docName;
	
	@Column(name = "doc_number")
	private String docNumber;
	
	@Column(name = "doc_type")
	private String docType;
	
	@Column(name = "verification_status")
	private String verificationStatus;
	
	@Column(name = "doc_format")
	private String docFormat;
	
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

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public String getDocFormat() {
		return docFormat;
	}

	public void setDocFormat(String docFormat) {
		this.docFormat = docFormat;
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


   public void validateInput() {
	   try {
		  Assert.notNull(this, "document can't be null or empty");
		  Assert.hasLength(this.getDocFormat(), "Doc format can't be null or empty");
		  Assert.hasLength(this.getDocName(),"Doc name can't be null or empty");
		  Assert.hasLength(this.getDocNumber(),"Doc number can't be null or empty");
		  Assert.hasLength(this.getVerificationStatus(),"Doc verification status can't be null or empty");
	   }catch(IllegalArgumentException ex) {
		   throw new IllegalArgumentException(ex.getMessage());
	   }
   }
	
}
