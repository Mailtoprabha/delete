package com.example.bulkpayment;

import com.example.bulkpayment.domain.FileFormat;
import com.example.bulkpayment.parser.ParserRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PainParserTest {

    @Autowired
    private ParserRegistry parserRegistry;

    @Test
    void shouldParsePainV3Xml() {
        String xml = """
                <Document>
                  <CstmrCdtTrfInitn>
                    <PmtInf>
                      <ReqdExctnDt>2026-01-11</ReqdExctnDt>
                      <CdtTrfTxInf>
                        <PmtId><InstrId>TXN-PAIN-1</InstrId></PmtId>
                        <Amt><InstdAmt Ccy=\"EUR\">10.50</InstdAmt></Amt>
                        <DbtrAcct>DEB-100</DbtrAcct>
                        <CdtrAcct>CRD-900</CdtrAcct>
                        <RmtInf><Ustrd>Utility Bill</Ustrd></RmtInf>
                      </CdtTrfTxInf>
                    </PmtInf>
                  </CstmrCdtTrfInitn>
                </Document>
                """;

        var result = parserRegistry.getParser(FileFormat.PAIN_001_V3).parse("FILE-PAIN-1", xml);

        assertThat(result.records()).hasSize(1);
        assertThat(result.records().get(0).transactionId()).isEqualTo("TXN-PAIN-1");
    }
}
