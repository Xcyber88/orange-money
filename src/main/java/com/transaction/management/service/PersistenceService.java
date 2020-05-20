package com.transaction.management.service;

import com.transaction.management.domain.entity.Transaction;
import com.transaction.management.domain.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersistenceService {

    private final TransactionsRepository transactionsRepository;

    @Autowired
    public PersistenceService(final TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public List<Transaction> getAll() {
        return transactionsRepository.findAll();
    }

    public void saveTransaction(final Transaction transaction){
        transactionsRepository.save(transaction);
    }

    public List<Transaction> findAllByCnp(final String cnp){
        return transactionsRepository.findAllByCnp(cnp);
    }
}
