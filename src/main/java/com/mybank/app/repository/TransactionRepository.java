package com.mybank.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mybank.app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	//@Query(value = "select * from transactions where trans_acc_id=:accountId and created_on>=:startDate and created_on<=:endDate", nativeQuery = true)
	@Query(value = "select * from transactions where trans_acc_id=:accountId and created_on>=:startDate order by created_on desc", nativeQuery = true)
	public List<Transaction> findInDateRange(@Param("accountId") Long accountId,
			@Param("startDate") LocalDateTime startDate);
}
