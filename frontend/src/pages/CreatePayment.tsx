import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CreditCardIcon, UserIcon, BuildingOfficeIcon } from '@heroicons/react/24/outline';
import { paymentApi } from '../services/api';
import { PaymentFormData, PaymentRequest, ApiError } from '../types/api';

const CreatePayment: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<PaymentFormData>({
    buyerName: '',
    buyerDocument: '',
    cardInfo: '',
    cardToken: '',
    sellerAccount: '',
    amount: '',
    currency: 'BRL',
    checkoutId: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const paymentRequest: PaymentRequest = {
        checkout_id: formData.checkoutId || undefined,
        buyer_info: {
          name: formData.buyerName,
          document: formData.buyerDocument,
        },
        credit_card_info: {
          card_info: formData.cardInfo,
          token: formData.cardToken,
        },
        payment_orders: [
          {
            seller_account: formData.sellerAccount,
            amount: formData.amount,
            currency: formData.currency,
            payment_order_id: `po-${Date.now()}`,
          },
        ],
      };

      const response = await paymentApi.createPayment(paymentRequest);
      setSuccess(`Payment created successfully! Payment ID: ${response.id}`);
      
      // Navigate to payment details page after 2 seconds
      setTimeout(() => {
        navigate(`/payment/${response.id}`);
      }, 2000);
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.description || 'Failed to create payment');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold bg-gradient-to-r from-light-blue-600 to-light-purple-600 bg-clip-text text-transparent mb-2">
          Create Payment
        </h1>
        <p className="text-gray-600">
          Process a new payment through the Paymentic platform
        </p>
      </div>

      <form onSubmit={handleSubmit} className="card space-y-6">
        {/* Buyer Information */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold text-gray-800 flex items-center">
            <UserIcon className="w-5 h-5 mr-2" />
            Buyer Information
          </h3>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="buyerName" className="block text-sm font-medium text-gray-700 mb-1">
                Full Name
              </label>
              <input
                type="text"
                id="buyerName"
                name="buyerName"
                value={formData.buyerName}
                onChange={handleInputChange}
                required
                className="input-field"
                placeholder="John Doe"
              />
            </div>
            
            <div>
              <label htmlFor="buyerDocument" className="block text-sm font-medium text-gray-700 mb-1">
                Document (CPF)
              </label>
              <input
                type="text"
                id="buyerDocument"
                name="buyerDocument"
                value={formData.buyerDocument}
                onChange={handleInputChange}
                required
                className="input-field"
                placeholder="123.456.789-00"
              />
            </div>
          </div>
        </div>

        {/* Credit Card Information */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold text-gray-800 flex items-center">
            <CreditCardIcon className="w-5 h-5 mr-2" />
            Credit Card Information
          </h3>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="cardInfo" className="block text-sm font-medium text-gray-700 mb-1">
                Card Info
              </label>
              <input
                type="text"
                id="cardInfo"
                name="cardInfo"
                value={formData.cardInfo}
                onChange={handleInputChange}
                required
                className="input-field"
                placeholder="CARD1234567890"
              />
            </div>
            
            <div>
              <label htmlFor="cardToken" className="block text-sm font-medium text-gray-700 mb-1">
                Card Token
              </label>
              <input
                type="text"
                id="cardToken"
                name="cardToken"
                value={formData.cardToken}
                onChange={handleInputChange}
                required
                className="input-field"
                placeholder="token-123456"
              />
            </div>
          </div>
        </div>

        {/* Payment Details */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold text-gray-800 flex items-center">
            <BuildingOfficeIcon className="w-5 h-5 mr-2" />
            Payment Details
          </h3>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label htmlFor="sellerAccount" className="block text-sm font-medium text-gray-700 mb-1">
                Seller Account
              </label>
              <input
                type="text"
                id="sellerAccount"
                name="sellerAccount"
                value={formData.sellerAccount}
                onChange={handleInputChange}
                required
                className="input-field"
                placeholder="seller-account-id"
              />
            </div>
            
            <div>
              <label htmlFor="amount" className="block text-sm font-medium text-gray-700 mb-1">
                Amount
              </label>
              <input
                type="number"
                id="amount"
                name="amount"
                value={formData.amount}
                onChange={handleInputChange}
                required
                step="0.01"
                min="0"
                className="input-field"
                placeholder="100.00"
              />
            </div>
            
            <div>
              <label htmlFor="currency" className="block text-sm font-medium text-gray-700 mb-1">
                Currency
              </label>
              <select
                id="currency"
                name="currency"
                value={formData.currency}
                onChange={handleInputChange}
                required
                className="input-field"
              >
                <option value="BRL">BRL</option>
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
              </select>
            </div>
          </div>
        </div>

        {/* Optional Checkout ID */}
        <div>
          <label htmlFor="checkoutId" className="block text-sm font-medium text-gray-700 mb-1">
            Checkout ID (Optional)
          </label>
          <input
            type="text"
            id="checkoutId"
            name="checkoutId"
            value={formData.checkoutId}
            onChange={handleInputChange}
            className="input-field"
            placeholder="checkout-id-123"
          />
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <p className="text-red-800 text-sm">{error}</p>
          </div>
        )}

        {/* Success Message */}
        {success && (
          <div className="bg-green-50 border border-green-200 rounded-lg p-4">
            <p className="text-green-800 text-sm">{success}</p>
          </div>
        )}

        {/* Submit Button */}
        <button
          type="submit"
          disabled={loading}
          className="btn-primary w-full disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {loading ? 'Creating Payment...' : 'Create Payment'}
        </button>
      </form>
    </div>
  );
};

export default CreatePayment;