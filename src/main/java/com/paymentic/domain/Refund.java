package com.paymentic.domain;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "refund")
public class Refund {

  @Id
  @Column(name = "refund_id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="id",column=@Column(name="payment_id"))
  })
  private PaymentOrderId payment;
  private LocalDateTime at;
  private String amount;
  private String currency;
  public Refund(){}
  public Refund(PaymentOrderId payment, LocalDateTime at, String amount, String currency) {
    this.payment = payment;
    this.at = at;
    this.amount = amount;
    this.currency = currency;
  }
  public UUID getId() {
    return id;
  }
  public PaymentOrderId getPayment() {
    return payment;
  }
  public LocalDateTime getAt() {
    return at;
  }
  public String getAmount() {
    return amount;
  }
  public String getCurrency() {
    return currency;
  }
}
