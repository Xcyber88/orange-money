package com.transaction.management.controller;

import com.transaction.management.domain.entity.Transaction;
import com.transaction.management.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(
            path =  "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Transaction> getAllTransactions() {
        return transactionService.getAll();
    }

    @PostMapping(
            path =  "/validate-multiple",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String validateMultipleTransactions(@RequestBody List<Transaction> transactions) {
        return transactionService.validateMultipleTransaction(transactions);
    }

    @PostMapping(
            value = "/validate-single/{showReport}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String validateMultipleTransactions(@RequestBody Transaction transaction,
                                               @PathVariable("showReport") boolean showReport) {
        return transactionService.validateSingleTransaction(transaction, showReport);
    }
}
