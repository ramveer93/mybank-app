package com.mybank.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybank.app.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
