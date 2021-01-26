package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long>{

}
