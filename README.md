# Bulk Payment Parser Service (Spring Boot)

This project implements a **bulk payment file ingestion pipeline** in Java/Spring for multiple input formats:

- Delimited text files
- ISO 20022 `pain.001.001.03` (V3)
- ISO 20022 `pain.001.001.09` (V9)

It provides:

1. Format-specific parsing (registry-based)
2. Dynamic/configurable business validations
3. XSD validation for PAIN XML payloads
4. Two-step persistence (staging then main tables)
5. ACK/NAK emission after processing outcome
6. AWS-ready configuration placeholders (S3 + EventBridge)

## High-level flow

1. Detect/receive file format and content
2. Parse using `ParserRegistry` + parser implementation
3. If PAIN XML, run XSD validation first
4. Run business rules from `payment.validation.*` configuration
5. On success:
   - insert records into `payment_staging`
   - promote records to `payment_main`
   - publish ACK
6. On validation/parser/load failure:
   - publish NAK

## Key components

- `service/BulkPaymentProcessingService`: orchestration and transaction boundary
- `parser/*`: file-format parser abstraction + implementations
- `validation/*`: business rule + XML schema validation
- `persistence/PaymentRepository`: staging/main JDBC operations
- `ack/AckService`: ACK/NAK interface and implementation hook

## Local run (Eclipse-friendly)

### Prerequisites
- Java 17
- Maven 3.9+
- Eclipse IDE for Enterprise Java (optional)

### Start
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

### Eclipse import
1. `File -> Import -> Maven -> Existing Maven Projects`
2. Select project root
3. Run `BulkPaymentApplication` as a Java application

## Configuration

Business validations are configurable in `src/main/resources/application.yml`:

- `payment.validation.min-amount`
- `payment.validation.max-amount`
- `payment.validation.allowed-currencies`
- `payment.validation.require-debtor-and-creditor`

AWS placeholders:

- `aws.s3.inbound-bucket`
- `aws.eventbridge.ack-bus-name`

## AWS target deployment pattern (recommended)

- **Ingestion**: S3 upload event (or Transfer Family) triggers processing
- **Compute**: ECS Fargate / EKS / Spring Boot on EC2 / Lambda wrapper
- **Data stores**: RDS PostgreSQL for staging/main
- **Messaging**: EventBridge/SNS for ACK/NAK notifications
- **Monitoring**: CloudWatch logs + alarms

> `EventBridgeAckService` is currently a logging stub. Replace with AWS SDK integration for production.
