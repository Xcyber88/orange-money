package com.transaction.management.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "transaction_type", length = 250)
    private String transactionType;

    @Column(name = "iban_payer", length = 30)
    private String ibanPayer;

    @Column(name = "iban_payee", length = 30)
    private String ibanPayee;

    @Column(name = "wallet_payer", length = 30)
    private String walletPayer;

    @Column(name = "wallet_payee", length = 30)
    private String walletPayee;

    private String cnp;

    private String name;

    private String description;

    private int amount;

    public Transaction() {
    }

    public Transaction(String transactionType, String ibanPayer, String ibanPayee, String walletPayer,
                       String walletPayee, String cnp, String name, String description, int amount) {
        this.transactionType = transactionType;
        this.ibanPayer = ibanPayer;
        this.ibanPayee = ibanPayee;
        this.walletPayer = walletPayer;
        this.walletPayee = walletPayee;
        this.cnp = cnp;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transaction_type) {
        this.transactionType = transaction_type;
    }

    public String getIbanPayer() {
        return ibanPayer;
    }

    public void setIbanPayer(String ibanPayer) {
        this.ibanPayer = ibanPayer;
    }

    public String getIbanPayee() {
        return ibanPayee;
    }

    public void setIbanPayee(String ibanPayee) {
        this.ibanPayee = ibanPayee;
    }

    public String getWalletPayer() {
        return walletPayer;
    }

    public void setWalletPayer(String walletPayer) {
        this.walletPayer = walletPayer;
    }

    public String getWalletPayee() {
        return walletPayee;
    }

    public void setWalletPayee(String walletPayee) {
        this.walletPayee = walletPayee;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
