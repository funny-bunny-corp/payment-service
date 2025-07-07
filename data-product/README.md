# Payment Transactions Analytics Data Product

## Owner & Domain
**Product Owner**: Payments Engineering Team  
**Business Domain**: Payment Gateway / Financial Services  
**Data Product Name**: `payment_transactions`  
**Version**: 1.0.0  

## Description & Purpose

The Payment Transactions Analytics Data Product provides real-time, comprehensive analytics capabilities for all payment-related transactions within the Paymentic payment gateway ecosystem. This data product aggregates and structures event streams from the payment processing pipeline to enable business intelligence, fraud detection, financial reporting, and operational monitoring.

### Key Business Value
- **Real-time Payment Monitoring**: Track payment success rates, processing times, and transaction volumes in real-time
- **Revenue Analytics**: Analyze transaction amounts, processing fees, and merchant performance across different dimensions
- **Risk Management**: Monitor suspicious patterns, decline rates, and implement fraud detection algorithms
- **Operational Insights**: Understand system performance, processing bottlenecks, and customer behavior patterns
- **Regulatory Compliance**: Maintain audit trails and support financial reporting requirements

### Primary Use Cases
1. **Business Intelligence Dashboards**: Real-time payment metrics for executives and analysts
2. **Fraud Detection**: ML models analyzing transaction patterns and risk indicators
3. **Financial Reporting**: Daily/monthly revenue reconciliation and merchant payouts
4. **Customer Support**: Transaction lookup and dispute resolution
5. **Performance Monitoring**: Payment processing SLA tracking and system health metrics

## Data Sources

This data product ingests from the `payments` Kafka topic which receives CloudEvents-formatted messages from the Payment Service application. The source events include:

- `payment.created` - Initial payment request events
- `payment.done` - Payment completion events  
- `payment-order.started` - Payment processing initiation
- `payment-order.approved` - PSP approval events
- `payment-order.declined` - PSP decline events
- `payment-order.booked` - Ledger booking confirmations
- `refund.created` - Refund request events

## Schema Definition

| Field Name | Data Type | Description | Example | Required |
|------------|-----------|-------------|---------|----------|
| `paymentId` | STRING | Unique payment transaction identifier | `"pay_123e4567-e89b-12d3-a456-426614174000"` | Yes |
| `checkoutId` | STRING | Checkout session identifier | `"chk_789f0123-e45f-67g8-h901-234567890abc"` | Yes |
| `merchantId` | STRING | Merchant/seller identifier | `"merchant_acme_corp"` | Yes |
| `paymentStatus` | STRING | Current payment status | `"SUCCESS"`, `"FAILED"`, `"EXECUTING"` | Yes |
| `currency` | STRING | ISO 4217 currency code | `"USD"`, `"EUR"`, `"BRL"` | Yes |
| `eventType` | STRING | Type of payment event | `"payment.created"`, `"payment-order.approved"` | Yes |
| `eventSource` | STRING | Source system/service of the event | `"/checkout/123e4567-e89b-12d3"` | No |
| `eventSubject` | STRING | Event subject/topic | `"/payments/pay_123e4567"` | No |
| `idempotencyKey` | STRING | Unique key ensuring event idempotency | `"idem_a1b2c3d4e5f6"` | Yes |
| `buyerDocument` | STRING | Buyer identification document (PII) | `"123.456.789-00"` | No |
| `buyerName` | STRING | Buyer full name (PII) | `"John Smith"` | No |
| `cardToken` | STRING | Tokenized payment method identifier | `"tok_1A2B3C4D5E"` | No |
| `cardInfoHash` | STRING | Hashed card information for analytics | `"hash_abc123def456"` | No |
| `paymentMethod` | STRING | Payment method type | `"CREDIT_CARD"`, `"BANK_TRANSFER"` | No |
| `declineReason` | STRING | Reason for payment decline | `"INSUFFICIENT_FUNDS"`, `"EXPIRED_CARD"` | No |
| `processingRegion` | STRING | Geographic processing region | `"US_EAST"`, `"EU_WEST"` | No |
| `riskScore` | STRING | Risk assessment score | `"LOW"`, `"MEDIUM"`, `"HIGH"` | No |
| `amountCents` | LONG | Transaction amount in cents | `15099` (for $150.99) | Yes |
| `processingFeeCents` | LONG | Processing fee in cents | `450` (for $4.50) | No |
| `processingDurationMs` | LONG | Processing time in milliseconds | `1250` | No |
| `eventTimestamp` | LONG | Event timestamp (epoch milliseconds) | `1641024000000` | Yes |
| `businessDate` | INT | Business date (YYYYMMDD format) | `20220101` | No |

## Data Quality Guarantees (SLA/SLO)

### Freshness
- **Target**: < 30 seconds from event generation to availability in Pinot
- **SLA**: 95% of events available within 2 minutes

### Completeness  
- **Target**: 99.9% of payment events captured
- **SLA**: No more than 0.1% data loss per day
- **Missing Data Handling**: Automated alerts for missing required fields

### Accuracy
- **Data Validation**: Schema validation on ingestion with automatic error handling
- **Duplicate Prevention**: Idempotency key enforcement prevents duplicate processing
- **Referential Integrity**: Payment IDs must be valid UUIDs

### Availability
- **Target**: 99.9% query availability during business hours
- **Recovery Time**: < 15 minutes for service restoration

## Example Queries

### 1. Real-time Payment Success Rate by Merchant
```sql
SELECT 
    merchantId,
    COUNT(*) as total_payments,
    SUM(CASE WHEN paymentStatus = 'SUCCESS' THEN 1 ELSE 0 END) as successful_payments,
    (SUM(CASE WHEN paymentStatus = 'SUCCESS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as success_rate_pct
FROM payment_transactions 
WHERE eventTimestamp >= (now() - 86400000)  -- Last 24 hours
    AND eventType IN ('payment-order.approved', 'payment-order.declined')
GROUP BY merchantId 
ORDER BY total_payments DESC
LIMIT 10;
```

### 2. Daily Revenue Analysis by Currency
```sql
SELECT 
    businessDate,
    currency,
    COUNT(DISTINCT paymentId) as transaction_count,
    SUM(amountCents)/100.0 as total_revenue,
    AVG(amountCents)/100.0 as avg_transaction_value,
    SUM(processingFeeCents)/100.0 as total_fees
FROM payment_transactions 
WHERE eventType = 'payment-order.approved'
    AND businessDate >= 20240101
GROUP BY businessDate, currency 
ORDER BY businessDate DESC, total_revenue DESC;
```

### 3. Payment Processing Performance Analysis
```sql
SELECT 
    paymentMethod,
    processingRegion,
    COUNT(*) as payment_count,
    AVG(processingDurationMs) as avg_processing_time_ms,
    PERCENTILE(processingDurationMs, 95) as p95_processing_time_ms,
    COUNT(CASE WHEN processingDurationMs > 5000 THEN 1 END) as slow_payments
FROM payment_transactions 
WHERE eventTimestamp >= (now() - 3600000)  -- Last hour
    AND eventType = 'payment-order.approved'
    AND processingDurationMs IS NOT NULL
GROUP BY paymentMethod, processingRegion 
ORDER BY avg_processing_time_ms DESC;
```

## Access & Integration

### Query Access
- **Pinot Broker URL**: `http://pinot-broker:8099`  
- **Table Name**: `payment_transactions`
- **Authentication**: API key required for production access

### Data Lineage
```
Payment Service → Kafka (payments topic) → Apache Pinot → Analytics Dashboards
                                        → ML Models
                                        → Reporting Systems
```

### Related Data Products
- `merchant_profiles` - Merchant analytics data product
- `fraud_detection_features` - ML feature store for fraud models  
- `financial_reconciliation` - Accounting and settlement data product

## Data Governance

### Privacy & Compliance
- **PII Fields**: `buyerDocument`, `buyerName` are considered PII and access is logged
- **Data Retention**: 365 days (configurable based on regulatory requirements)
- **Geographic Restrictions**: EU data remains in EU regions per GDPR requirements

### Data Classification
- **Sensitivity Level**: Internal - Confidential
- **Regulatory Impact**: PCI DSS, GDPR, SOX compliance requirements
- **Access Control**: Role-based access with audit logging

## Support & Contacts

**Primary Contacts**:
- Data Product Owner: payments-data-team@paymentic.com
- Technical Support: platform-engineering@paymentic.com  
- Business Stakeholders: business-intelligence@paymentic.com

**Documentation**: 
- [Paymentic Data Platform Wiki](https://wiki.paymentic.com/data-platform)
- [Payment Service API Documentation](https://docs.paymentic.com/payment-service)

**Incident Response**: Create ticket in JIRA project PAYMENTS-DATA or contact #payments-data-support Slack channel

---

*Last Updated: January 2025*  
*Data Product Version: 1.0.0*  
*Schema Version: 1.0.0*