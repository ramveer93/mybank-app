package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

}
