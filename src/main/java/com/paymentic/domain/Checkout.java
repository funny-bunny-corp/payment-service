package com.paymentic.domain;

import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "checkout")
public class Checkout {

  @Id
  @Column(name = "checkout_id")
  private UUID id;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="document",column=@Column(name="buyer_document")),
      @AttributeOverride(name="name",column=@Column(name="buyer_name"))
  })
  private BuyerInfo buyerInfo;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="cardInfo",column=@Column(name="credit_card_info")),
      @AttributeOverride(name="token",column=@Column(name="credit_card_token"))
  })
  private CardInfo cardInfo;
  @Column(name = "is_payment_done")
  private Boolean isPaymentDone;

  @Column(name = "idempotency_key")
  private String idempotencyKey;

  public UUID getId() {
    return id;
  }

  public Checkout() {
  }
  private Checkout(UUID id,BuyerInfo buyerInfo, CardInfo cardInfo,
      Boolean isPaymentDone,String idempotencyKey) {
    this.buyerInfo = buyerInfo;
    this.cardInfo = cardInfo;
    this.isPaymentDone = isPaymentDone;
    this.id = id;
    this.idempotencyKey = idempotencyKey;

  }
  public static Checkout newCheckoutInitiated(String id,BuyerInfo buyerInfo, CardInfo cardInfo,String idempotencyKey){
    return new Checkout(UUID.fromString(id),buyerInfo,cardInfo,false,idempotencyKey);
  }
  public BuyerInfo getBuyerInfo() {
    return buyerInfo;
  }
  public CardInfo getCardInfo() {
    return cardInfo;
  }
  public Boolean getPaymentDone() {
    return isPaymentDone;
  }
  public String getIdempotencyKey() {
    return idempotencyKey;
  }
}
