# Payment Service Events and Commands Flow

This diagram shows the events and commands that are produced and consumed by the Payment Service application using Kafka messaging.

## Event Flow Diagram

```mermaid
graph TB
    %% External Systems
    PSP[Payment Service Provider]
    Ledger[Ledger System]
    Wallet[Merchant Wallet System]
    
    %% Payment Service Application
    PaymentService[Payment Service Application]
    
    %% Kafka Topics
    PaymentTopic["🔄 Kafka Topic: payments<br/>(payment-processing)"]
    FinancialTopic["🔄 Kafka Topic: financial-reporting"]
    MerchantTopic["🔄 Kafka Topic: merchant-account"]
    
    %% Events Produced by Payment Service
    PaymentService -->|Publishes| PaymentCreated["📤 PaymentCreatedEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.payment.created<br/>When: Payment request is created"]
    PaymentService -->|Publishes| PaymentDone["📤 PaymentDoneEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.payment.done<br/>When: Payment is completed"]
    PaymentService -->|Publishes| RefundCreated["📤 RefundCreatedEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.refund.created<br/>When: Refund request is initiated"]
    
    %% Events Consumed by Payment Service
    PaymentOrderStarted["📥 PaymentOrderStartedEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.payment-order.started<br/>From: External Payment Processing"] -->|Consumes| PaymentService
    PaymentOrderApproved["📥 PaymentOrderApprovedEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.payment-order.approved<br/>From: PSP"] -->|Consumes| PaymentService
    PaymentOrderDeclined["📥 PaymentOrderDeclinedEvent<br/>Type: funny-bunny.xyz.payment-processing.v1.payment-order.declined<br/>From: PSP"] -->|Consumes| PaymentService
    PaymentOrderBooked["📥 PaymentOrderBookedEvent<br/>Type: funny-bunny.xyz.financial-reporting.v1.transaction.booked<br/>From: Ledger"] -->|Consumes| PaymentService
    WalletUpdated["📥 PaymentOrderRegisteredInWalletEvent<br/>Type: funny-bunny.xyz.merchant-account.v1.transaction.wallet-registered<br/>From: Wallet System"] -->|Consumes| PaymentService
    
    %% Topic Connections - Outbound
    PaymentCreated --> PaymentTopic
    PaymentDone --> PaymentTopic
    RefundCreated --> PaymentTopic
    
    %% Topic Connections - Inbound
    PaymentTopic --> PaymentOrderStarted
    PaymentTopic --> PaymentOrderApproved
    PaymentTopic --> PaymentOrderDeclined
    FinancialTopic --> PaymentOrderBooked
    MerchantTopic --> WalletUpdated
    
    %% External System Connections
    PSP -.->|Sends| PaymentOrderApproved
    PSP -.->|Sends| PaymentOrderDeclined
    Ledger -.->|Sends| PaymentOrderBooked
    Wallet -.->|Sends| WalletUpdated
    
    %% Internal Events (Application Events)
    PaymentService -->|Internal| CheckoutClosed["🔄 CheckoutClosedEvent<br/>Internal Spring Event"]
    PaymentService -->|Internal| PaymentOrderEvent["🔄 PaymentOrderEvent<br/>Internal Spring Event"]
    PaymentService -->|Internal| IdempotencyUsage["🔄 IdempotencyKeyRequestUsage<br/>Internal Spring Event"]
    
    %% Styling
    classDef kafkaTopic fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef publishedEvent fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef consumedEvent fill:#fff3e0,stroke:#ef6c00,stroke-width:2px
    classDef internalEvent fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px
    classDef externalSystem fill:#ffebee,stroke:#c62828,stroke-width:2px
    classDef paymentService fill:#e3f2fd,stroke:#1565c0,stroke-width:3px
    
    class PaymentTopic,FinancialTopic,MerchantTopic kafkaTopic
    class PaymentCreated,PaymentDone,RefundCreated publishedEvent
    class PaymentOrderStarted,PaymentOrderApproved,PaymentOrderDeclined,PaymentOrderBooked,WalletUpdated consumedEvent
    class CheckoutClosed,PaymentOrderEvent,IdempotencyUsage internalEvent
    class PSP,Ledger,Wallet externalSystem
    class PaymentService paymentService
```

## Event Details

### Events Published by Payment Service

| Event Name | Event Type | Kafka Topic | Trigger | Description |
|------------|------------|-------------|---------|-------------|
| **PaymentCreatedEvent** | `funny-bunny.xyz.payment-processing.v1.payment.created` | `payments` | When a payment request is created | Initiates the payment processing flow |
| **PaymentDoneEvent** | `funny-bunny.xyz.payment-processing.v1.payment.done` | `payments` | When payment is completed | Signals successful payment completion |
| **RefundCreatedEvent** | `funny-bunny.xyz.payment-processing.v1.refund.created` | `payments` | When a refund is requested | Initiates the refund processing flow |

### Events Consumed by Payment Service

| Event Name | Event Type | Kafka Topic | Source | Description |
|------------|------------|-------------|--------|-------------|
| **PaymentOrderStartedEvent** | `funny-bunny.xyz.payment-processing.v1.payment-order.started` | `payment-processing` | External Payment System | Notifies that payment order processing has started |
| **PaymentOrderApprovedEvent** | `funny-bunny.xyz.payment-processing.v1.payment-order.approved` | `payment-processing` | Payment Service Provider | Notifies that payment has been approved |
| **PaymentOrderDeclinedEvent** | `funny-bunny.xyz.payment-processing.v1.payment-order.declined` | `payment-processing` | Payment Service Provider | Notifies that payment has been declined |
| **PaymentOrderBookedEvent** | `funny-bunny.xyz.financial-reporting.v1.transaction.booked` | `financial-reporting` | Ledger System | Notifies that transaction has been recorded in ledger |
| **PaymentOrderRegisteredInWalletEvent** | `funny-bunny.xyz.merchant-account.v1.transaction.wallet-registered` | `merchant-account` | Merchant Wallet System | Notifies that payment has been registered in merchant wallet |

### Internal Application Events

| Event Name | Type | Description |
|------------|------|-------------|
| **CheckoutClosedEvent** | Spring Application Event | Internal event when checkout process is completed |
| **PaymentOrderEvent** | Spring Application Event | Internal event for payment order processing |
| **IdempotencyKeyRequestUsage** | Spring Application Event | Internal event for tracking idempotency key usage |

## Kafka Topics Configuration

- **payment-processing**: Main topic for payment events (3 partitions, 3 replicas)
- **financial-reporting**: Topic for financial and ledger events
- **merchant-account**: Topic for merchant wallet and account events

## Technology Stack

- **Spring Boot 3.4.0** with Spring Kafka
- **CloudEvents** specification for event formatting
- **Kafka** as the message broker
- **PostgreSQL** for persistence
- **OpenTelemetry** for observability