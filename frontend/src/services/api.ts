import axios, { AxiosInstance, AxiosResponse } from 'axios';
import {
  PaymentRequest,
  PaymentCreated,
  PaymentData,
  PaymentOrderStatus,
  RefundRequest,
  RefundResponse,
  ApiError,
} from '../types/api';

class PaymentAPI {
  private api: AxiosInstance;

  constructor(baseURL: string = 'http://localhost:8080') {
    this.api = axios.create({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor for idempotency key
    this.api.interceptors.request.use((config) => {
      if (config.method === 'post') {
        config.headers['Idempotency-Key'] = this.generateIdempotencyKey();
      }
      return config;
    });

    // Add response interceptor for error handling
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.data) {
          const apiError: ApiError = {
            code: error.response.data.code || 'UNKNOWN_ERROR',
            description: error.response.data.description || 'An unknown error occurred',
          };
          throw apiError;
        }
        throw error;
      }
    );
  }

  private generateIdempotencyKey(): string {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  // Create a new payment
  async createPayment(paymentRequest: PaymentRequest): Promise<PaymentCreated> {
    const response: AxiosResponse<PaymentCreated> = await this.api.post(
      '/payments',
      paymentRequest
    );
    return response.data;
  }

  // Get payment details by ID
  async getPayment(paymentId: string): Promise<PaymentData> {
    const response: AxiosResponse<PaymentData> = await this.api.get(
      `/payments/${paymentId}`
    );
    return response.data;
  }

  // Get payment orders by payment ID
  async getPaymentOrders(paymentId: string): Promise<PaymentOrderStatus[]> {
    const response: AxiosResponse<PaymentOrderStatus[]> = await this.api.get(
      `/payments/${paymentId}/payment-orders`
    );
    return response.data;
  }

  // Create a refund
  async createRefund(refundRequest: RefundRequest): Promise<RefundResponse> {
    const response: AxiosResponse<RefundResponse> = await this.api.post(
      '/refunds',
      refundRequest
    );
    return response.data;
  }

  // Set authorization token
  setAuthToken(token: string): void {
    this.api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }

  // Remove authorization token
  removeAuthToken(): void {
    delete this.api.defaults.headers.common['Authorization'];
  }
}

// Export a singleton instance
export const paymentApi = new PaymentAPI();
export default paymentApi;