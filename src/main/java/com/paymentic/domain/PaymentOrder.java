package com.paymentic.domain;

import com.paymentic.domain.application.HashService;
import com.paymentic.domain.shared.SellerInfo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import org.apache.kafka.common.protocol.types.Field.Str;

@Entity(name = "payment_order")
public class PaymentOrder {
  private static final String IDEMPOTENCY_PATTERN = "%s-%s";

  @Id
  @Column(name = "payment_order_id")
  private UUID id;
  private String amount;
  private String currency;
  @Column(name = "payment_order_status")
  @Enumerated(EnumType.STRING)
  private PaymentOrderStatus status;
  @Column(name = "ledger_updated")
  private Boolean isLedgerUpdated;
  @Column(name = "wallet_updated")
  private Boolean isWalletUpdated;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="id",column=@Column(name="checkout_id"))
  })
  private CheckoutId checkout;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="sellerId",column=@Column(name="seller_id"))
  })
  private SellerInfo sellerInfo;

  @Column(name = "idempotency_key")
  private String idempotencyKey;

  public PaymentOrder() {
  }
  private PaymentOrder(UUID id,String amount, String currency, PaymentOrderStatus status,
      Boolean isLedgerUpdated, Boolean isWalletUpdated, CheckoutId checkoutId,
      SellerInfo sellerInfo,String idempotencyKey) {
    this.id = id;
    this.amount = amount;
    this.currency = currency;
    this.status = status;
    this.isLedgerUpdated = isLedgerUpdated;
    this.isWalletUpdated = isWalletUpdated;
    this.checkout = checkoutId;
    this.sellerInfo = sellerInfo;
    this.idempotencyKey = idempotencyKey;
  }
  public static PaymentOrder newPaymentInitiated(String amount, String currency, CheckoutId checkoutId,
      SellerInfo sellerInfo){
    var id = UUID.randomUUID();
    var idempotencyKey = HashService.createMD5Hash(String.format(IDEMPOTENCY_PATTERN,checkoutId.getId(), id.toString()));
    return new PaymentOrder(id,amount,currency,PaymentOrderStatus.NOT_STARTED,false,false,checkoutId,sellerInfo,idempotencyKey);
  }
  public UUID getId() {
    return id;
  }
  public String getAmount() {
    return amount;
  }
  public String getCurrency() {
    return currency;
  }
  public PaymentOrderStatus getStatus() {
    return status;
  }
  public Boolean getLedgerUpdated() {
    return isLedgerUpdated;
  }
  public Boolean getWalletUpdated() {
    return isWalletUpdated;
  }
  public CheckoutId getCheckout() {
    return checkout;
  }
  public SellerInfo getSellerInfo() {
    return sellerInfo;
  }
  public String getIdempotencyKey() {
    return idempotencyKey;
  }
  public PaymentOrder markStarted(){
    this.status = PaymentOrderStatus.EXECUTING;
    return this;
  }

}
