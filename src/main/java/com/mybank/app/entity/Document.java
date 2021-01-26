package com.mybank.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.jsonwebtoken.lang.Assert;

@Entity
@Table(name = "document")
public class Document {

	@Id
	@Column(name = "doc_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docId;

	@Column(name = "doc_name")
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
	
	public Document() {
		super();
	}

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
			if (this.getDocId() == null || this.getDocId() == 0) {
				Assert.hasLength(this.getDocFormat(), "Doc format can't be null or empty");
				Assert.hasLength(this.getDocName(), "Doc name can't be null or empty");
				Assert.hasLength(this.getDocNumber(), "Doc number can't be null or empty");
				Assert.hasLength(this.getVerificationStatus(), "Doc verification status can't be null or empty");
				Assert.hasLength(this.getDocType(), "Doc type can't be null or empty");
			}
			

		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	@Override
	public String toString() {
		return "Document [docId=" + docId + ", docName=" + docName + ", docNumber=" + docNumber + ", docType=" + docType
				+ ", verificationStatus=" + verificationStatus + ", docFormat=" + docFormat + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", deleted=" + deleted + "]";
	}

}
