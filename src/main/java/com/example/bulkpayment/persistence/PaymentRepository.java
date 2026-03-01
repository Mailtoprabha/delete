package com.example.bulkpayment.persistence;

import com.example.bulkpayment.domain.PaymentRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveToStaging(List<PaymentRecord> records) {
        String sql = """
                INSERT INTO payment_staging
                (file_reference, transaction_id, debtor_account, creditor_account, amount, currency, value_date, remittance_info, source_format)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, records, records.size(), (ps, record) -> {
            ps.setString(1, record.fileReference());
            ps.setString(2, record.transactionId());
            ps.setString(3, record.debtorAccount());
            ps.setString(4, record.creditorAccount());
            ps.setBigDecimal(5, record.amount());
            ps.setString(6, record.currency());
            ps.setDate(7, Date.valueOf(record.valueDate()));
            ps.setString(8, record.remittanceInfo());
            ps.setString(9, record.sourceFormat());
        });
    }

    public void promoteToMain(String fileReference) {
        jdbcTemplate.update("""
                INSERT INTO payment_main
                (file_reference, transaction_id, debtor_account, creditor_account, amount, currency, value_date, remittance_info, source_format)
                SELECT file_reference, transaction_id, debtor_account, creditor_account, amount, currency, value_date, remittance_info, source_format
                FROM payment_staging
                WHERE file_reference = ?
                """, fileReference);
    }
}
