package com.transaction.management.domain.repository;

import com.transaction.management.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByCnp(final String cnp);
}
