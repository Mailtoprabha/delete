package com.example.bulkpayment;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.service.BulkPaymentProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BulkPaymentProcessingServiceTest {

    @Autowired
    private BulkPaymentProcessingService service;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldProcessTextFileAndLoadStagingAndMain() {
        String content = "FILE001|TXN-1|DE001|CR001|100.20|EUR|2026-01-11|Invoice 123";

        service.process("FILE001", FileFormat.TEXT, content);

        Integer stagingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM payment_staging WHERE file_reference = 'FILE001'", Integer.class);
        Integer mainCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM payment_main WHERE file_reference = 'FILE001'", Integer.class);

        assertThat(stagingCount).isEqualTo(1);
        assertThat(mainCount).isEqualTo(1);
    }

    @Test
    void shouldRejectWhenBusinessValidationFails() {
        String content = "FILE002|TXN-2|DE001|CR001|0.00|EUR|2026-01-11|Invoice 456";

        service.process("FILE002", FileFormat.TEXT, content);

        Integer stagingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM payment_staging WHERE file_reference = 'FILE002'", Integer.class);
        assertThat(stagingCount).isEqualTo(0);
    }
}
