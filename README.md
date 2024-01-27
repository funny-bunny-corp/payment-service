# Payment Service

In a payment gateway context, a payment service that handles both payment and refund requests would be designed to facilitate seamless financial transactions for online purchases.

## 1. Payment Processing
### Transaction Initiation
- Allows initiation of payment transactions with details like amount, currency, and payment method.

## 2. Refund Processing
### Refund Initiation
- Merchants can initiate refunds specifying the amount and original transaction ID.

### Refund Verification
- Verifies the validity of refund requests against previous transactions and merchant policies.

## Technology stack
- Java 21
- Spring Boot 3.2.1
- PostgreSQL
- Kafka

## Development requirements
- java 21
- maven 3.8.1
- docker-compose

### Payment Gateway Ecosystem
You can find more details about the ecosystem in the following links:

- [Docs](https://github.com/paymentic/docs)
- [Schemas](https://github.com/paymentic/schemas)
- [Development Environment](https://github.com/paymentic/infra-development)
