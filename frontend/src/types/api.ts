// Payment API Types based on OpenAPI specification

export interface BuyerInfo {
  document: string;
  name: string;
}

export interface CreditCardInfo {
  card_info: string;
  token: string;
}

export interface PaymentOrder {
  seller_account: string;
  amount: string;
  currency: string;
  payment_order_id: string;
}

export interface PaymentRequest {
  checkout_id?: string;
  buyer_info: BuyerInfo;
  credit_card_info: CreditCardInfo;
  payment_orders: PaymentOrder[];
}

export interface PaymentCreated {
  id: string;
}

export interface PaymentData {
  id: string;
  status: 'DONE' | 'IN_PROGRESS';
}

export interface PaymentOrderStatus {
  id: string;
  status: 'NOT_STARTED' | 'EXECUTING' | 'SUCCESS' | 'FAILED';
}

export interface RefundRequest {
  payment_id: string;
  amount: string;
  reason?: string;
  currency: string;
}

export interface RefundResponse {
  refund_id: string;
  status: string;
}

export interface ApiError {
  code: string;
  description: string;
}

// Form types for UI components
export interface PaymentFormData {
  // Buyer information
  buyerName: string;
  buyerDocument: string;
  
  // Credit card information
  cardInfo: string;
  cardToken: string;
  
  // Payment orders
  sellerAccount: string;
  amount: string;
  currency: string;
  
  // Optional checkout ID
  checkoutId?: string;
}

export interface RefundFormData {
  paymentId: string;
  refundAmount: string;
  reason?: string;
  currency: string;
}

// API Response types
export interface ApiResponse<T> {
  data?: T;
  error?: ApiError;
  loading: boolean;
}