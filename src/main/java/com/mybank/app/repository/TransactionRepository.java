package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
