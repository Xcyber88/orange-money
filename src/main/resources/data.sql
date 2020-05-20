DROP TABLE IF EXISTS transaction;

CREATE TABLE transaction(
    id INT AUTO_INCREMENT  PRIMARY KEY,
    transaction_type VARCHAR(250) NOT NULL,
    iban_payer VARCHAR(30),
    iban_payee VARCHAR(30),
    wallet_payer VARCHAR(30),
    wallet_payee VARCHAR(30),
    cnp VARCHAR(13) NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(250),
    amount INT NOT NULL
);
