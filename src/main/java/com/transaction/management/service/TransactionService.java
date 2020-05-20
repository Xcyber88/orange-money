package com.transaction.management.service;

import com.transaction.management.domain.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final PersistenceService persistenceService;

    @Autowired
    public TransactionService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public List<Transaction> getAll() {
        return persistenceService.getAll();
    }

    public String validateMultipleTransaction(List<Transaction> transactionsList){
        try {
            transactionsList.forEach(this::validateTransaction);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage();
        }

        return "Transactions Validated and Saved";
    }

    public String validateSingleTransaction(Transaction transaction, Boolean showReport){
        try {
            validateTransaction(transaction);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage();
        }

        if (showReport) {
            return printReportMethod(transaction.getCnp());
        }

        return "Transaction Validated and Saved. \nNo report asked!";
    }

    public void validateTransaction(Transaction transaction){
        //CNP is MANDATORY and should have 13 numbers in length
        if (transaction.getCnp().isEmpty() || transaction.getCnp().isBlank() || transaction.getCnp().length() != 13) {
            throw  new IllegalArgumentException("CNP is mandatory or provided cnp is malformed");
        }

        //Name, Description, Amount are mandatory
        if (transaction.getName().isBlank() || transaction.getName().isEmpty() ||
                transaction.getDescription().isEmpty() || transaction.getDescription().isBlank() ||
                transaction.getAmount() <= 0 ) {
            throw new IllegalArgumentException("Name and/or Description are mandatory; Amount must be greater than 0");
        }

        switch (transaction.getTransactionType()) {
            case "IBAN_TO_IBAN":
                if (transaction.getIbanPayer().isBlank() || transaction.getIbanPayer().isEmpty() ||
                        transaction.getIbanPayee().isEmpty() || transaction.getIbanPayee().isEmpty()) {
                    throw new IllegalArgumentException("Iban Payer and Iban Payee are mandatory fields!");
                }
                break;
            case "IBAN_TO_WALLET":
                if (transaction.getIbanPayer().isBlank() || transaction.getIbanPayer().isEmpty() ||
                        transaction.getWalletPayee().isEmpty() || transaction.getWalletPayee().isBlank()) {
                    throw  new IllegalArgumentException("Iban Payer and Wallet Payee are mandatory fields!");
                }
                break;
            case "WALLET_TO_IBAN":
                if (transaction.getIbanPayee().isBlank() || transaction.getIbanPayee().isEmpty() ||
                        transaction.getWalletPayer().isEmpty() || transaction.getWalletPayer().isBlank()) {
                    throw  new IllegalArgumentException("Wallet Payer and Iban Payee are mandatory fields!");
                }
                break;
            case "WALLET_TO_WALLET":
                if (transaction.getWalletPayer().isBlank() || transaction.getWalletPayer().isEmpty() ||
                        transaction.getWalletPayee().isEmpty() || transaction.getWalletPayee().isBlank()) {
                    throw  new IllegalArgumentException("Wallet Payer and Wallet Payee are mandatory fields!");
                }
                break;
            default:
                throw new IllegalArgumentException("Something was wrong with your request!");
        }

        persistenceService.saveTransaction(transaction);
    }

    private String printReportMethod(String cnp) {
        List<Transaction> transactionsListForProvidedCNP = persistenceService.findAllByCnp(cnp);
        StringBuilder sb = new StringBuilder();

        List<Transaction> transactionsITOI = new ArrayList<>();
        List<Transaction> transactionsITOW = new ArrayList<>();
        List<Transaction> transactionsWTOI = new ArrayList<>();
        List<Transaction> transactionsWTOW = new ArrayList<>();

        transactionsListForProvidedCNP.forEach(transaction -> {
            switch (transaction.getTransactionType()){
                case "IBAN_TO_IBAN":
                    transactionsITOI.add(transaction);
                    break;
                case "IBAN_TO_WALLET":
                    transactionsITOW.add(transaction);
                    break;
                case "WALLET_TO_IBAN":
                    transactionsWTOI.add(transaction);
                    break;
                case "WALLET_TO_WALLET":
                    transactionsWTOW.add(transaction);
                    break;
            }
        });

        int sumITOI = transactionsITOI.stream()
                                      .map(Transaction::getAmount)
                                      .reduce(0,Integer::sum);
        int sumITOW = transactionsITOW.stream()
                                      .map(Transaction::getAmount)
                                      .reduce(0,Integer::sum);
        int sumWTOI = transactionsWTOI.stream()
                                      .map(Transaction::getAmount)
                                      .reduce(0,Integer::sum);
        int sumWTOW = transactionsWTOW.stream()
                                      .map(Transaction::getAmount)
                                      .reduce(0,Integer::sum);

        int countITOI = transactionsITOI.size();
        int countITOW = transactionsITOW.size();
        int countWTOI = transactionsWTOI.size();
        int countWTOW = transactionsWTOW.size();

        sb.append("TRANSACTIONS: " + "\r\n");

        if (countITOI == 0) {
            sb.append("1. IBAN_TO_IBAN | No Transactions\n");
        } else {
            sb.append("1. IBAN_TO_IBAN | " + countITOI+ " transactions | " + sumITOI + " RON\n");
            AtomicInteger number = new AtomicInteger(1);
            transactionsITOI.forEach(t -> {
                sb.append("\t" + number).append(": Name: " + t.getName().toUpperCase() + " transferred from IBAN: " +
                        t.getIbanPayer() + " to the destination IBAN: " + t.getIbanPayee() + " the amount of "
                + t.getAmount() + " with the description " + t.getDescription().toUpperCase() + ".\n");
                number.getAndIncrement();
            });
        }

        if (countITOW == 0){
            sb.append("2. IBAN_TO_WALLET | No Transactions\n");
        } else {
            sb.append("2. IBAN_TO_WALLET | ").append(countITOW).append(" transactions | ").append(sumITOW).append(" RON\n");
            AtomicInteger number = new AtomicInteger(1);
            transactionsITOW.forEach(t -> {
                sb.append("\t" + number).append(": Name: " + t.getName().toUpperCase() + " transferred from IBAN: " +
                        t.getIbanPayer() + " to the destination WALLET: " + t.getWalletPayee() + " the amount of "
                        + t.getAmount() + " with the description " + t.getDescription().toUpperCase() + ".\n");
                number.getAndIncrement();
            });
        }

        if(countWTOI == 0){
            sb.append("3. WALLET_TO_IBAN | No Transactions\n");
        } else {
            sb.append("3. WALLET_TO_IBAN | " + countWTOI+ " transactions | " + sumWTOI + " RON\n");
            AtomicInteger number = new AtomicInteger(1);
            transactionsWTOI.forEach(t -> {
                sb.append("\t" + number).append(": Name: " + t.getName().toUpperCase() + " transferred from WALLET: " +
                        t.getWalletPayer() + " to the destination IBAN: " + t.getIbanPayee() + " the amount of "
                        + t.getAmount() + " with the description " + t.getDescription().toUpperCase() + ".\n");
                number.getAndIncrement();
            });
        }

        if (countWTOW == 0) {
            sb.append("4. WALLET_TO_WALLET | No Transactions\n");
        } else {
            sb.append("4. WALLET_TO_WALLET | " + countWTOW+ " transactions | " + sumWTOW + " RON\n");
            AtomicInteger number = new AtomicInteger(1);
            transactionsWTOW.forEach(t -> {
                sb.append("\t" + number).append(": Name: " + t.getName().toUpperCase() + " transferred from WALLET: " +
                        t.getWalletPayer() + " to the destination WALLET: " + t.getWalletPayee() + " the amount of "
                        + t.getAmount() + " with the description " + t.getDescription().toUpperCase() + ".\n");
                number.getAndIncrement();
            });
        }

        sb.append("END OF REPORT!");
        return sb.toString();
    }
}
