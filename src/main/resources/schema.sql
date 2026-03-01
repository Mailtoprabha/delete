CREATE TABLE IF NOT EXISTS payment_staging (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_reference VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(64) NOT NULL,
    debtor_account VARCHAR(128),
    creditor_account VARCHAR(128),
    amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    value_date DATE NOT NULL,
    remittance_info VARCHAR(280),
    source_format VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_main (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_reference VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(64) NOT NULL,
    debtor_account VARCHAR(128),
    creditor_account VARCHAR(128),
    amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    value_date DATE NOT NULL,
    remittance_info VARCHAR(280),
    source_format VARCHAR(20) NOT NULL
);
