package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.AccountType;

public interface AccountTypeCodeRepository extends JpaRepository<AccountType, Long> {

}
