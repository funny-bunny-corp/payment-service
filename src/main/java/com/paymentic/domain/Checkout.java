package com.paymentic.domain;

import com.paymentic.domain.shared.BuyerInfo;
import com.paymentic.domain.shared.CardInfo;
import com.paymentic.domain.shared.SellerInfo;
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

  public UUID getId() {
    return id;
  }

  public Checkout() {
  }
  private Checkout(UUID id,BuyerInfo buyerInfo, CardInfo cardInfo,
      Boolean isPaymentDone) {
    this.buyerInfo = buyerInfo;
    this.cardInfo = cardInfo;
    this.isPaymentDone = isPaymentDone;
    this.id = id;
  }
  public static Checkout newCheckoutInitiated(BuyerInfo buyerInfo, CardInfo cardInfo){
    return new Checkout(UUID.randomUUID(),buyerInfo,cardInfo,false);
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

}
