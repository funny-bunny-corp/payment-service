package com.paymentic.domain;

import com.paymentic.domain.shared.SellerInfo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "payment_order")
public class PaymentOrder {

  @Id
  @Column(name = "payment_order_id")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
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

  public PaymentOrder() {
  }
  private PaymentOrder(String amount, String currency, PaymentOrderStatus status,
      Boolean isLedgerUpdated, Boolean isWalletUpdated, CheckoutId checkoutId,
      SellerInfo sellerInfo) {
    this.amount = amount;
    this.currency = currency;
    this.status = status;
    this.isLedgerUpdated = isLedgerUpdated;
    this.isWalletUpdated = isWalletUpdated;
    this.checkout = checkoutId;
    this.sellerInfo = sellerInfo;
  }
  public static PaymentOrder newPaymentInitiated(String amount, String currency, CheckoutId checkoutId,
      SellerInfo sellerInfo){
    return new PaymentOrder(amount,currency,PaymentOrderStatus.NOT_STARTED,false,false,checkoutId,sellerInfo);
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

}
