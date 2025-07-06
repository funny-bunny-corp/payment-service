import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { ArrowPathIcon, ExclamationCircleIcon, CheckCircleIcon } from '@heroicons/react/24/outline';
import { paymentApi } from '../services/api';
import { RefundFormData, RefundRequest, ApiError } from '../types/api';

const Refunds: React.FC = () => {
  const [searchParams] = useSearchParams();
  const [formData, setFormData] = useState<RefundFormData>({
    paymentId: searchParams.get('paymentId') || '',
    refundAmount: '',
    reason: '',
    currency: 'BRL',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [refundId, setRefundId] = useState<string | null>(null);

  useEffect(() => {
    const paymentId = searchParams.get('paymentId');
    if (paymentId) {
      setFormData(prev => ({ ...prev, paymentId }));
    }
  }, [searchParams]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const refundRequest: RefundRequest = {
        payment_id: formData.paymentId,
        amount: formData.refundAmount,
        currency: formData.currency,
        reason: formData.reason || undefined,
      };

      const response = await paymentApi.createRefund(refundRequest);
      setRefundId(response.refund_id);
      setSuccess(`Refund request created successfully! Refund ID: ${response.refund_id}`);
      
      // Reset form
      setFormData({
        paymentId: '',
        refundAmount: '',
        reason: '',
        currency: 'BRL',
      });
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.description || 'Failed to create refund request');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold bg-gradient-to-r from-light-blue-600 to-light-purple-600 bg-clip-text text-transparent mb-2">
          Create Refund
        </h1>
        <p className="text-gray-600">
          Request a refund for a completed payment transaction
        </p>
      </div>

      <form onSubmit={handleSubmit} className="card space-y-6">
        {/* Payment ID */}
        <div>
          <label htmlFor="paymentId" className="block text-sm font-medium text-gray-700 mb-1">
            Payment ID <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            id="paymentId"
            name="paymentId"
            value={formData.paymentId}
            onChange={handleInputChange}
            required
            className="input-field"
            placeholder="Enter the payment ID to refund"
          />
          <p className="text-sm text-gray-500 mt-1">
            The unique identifier of the payment transaction you want to refund
          </p>
        </div>

        {/* Refund Amount and Currency */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label htmlFor="refundAmount" className="block text-sm font-medium text-gray-700 mb-1">
              Refund Amount <span className="text-red-500">*</span>
            </label>
            <input
              type="number"
              id="refundAmount"
              name="refundAmount"
              value={formData.refundAmount}
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
              Currency <span className="text-red-500">*</span>
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

        {/* Reason */}
        <div>
          <label htmlFor="reason" className="block text-sm font-medium text-gray-700 mb-1">
            Reason (Optional)
          </label>
          <textarea
            id="reason"
            name="reason"
            value={formData.reason}
            onChange={handleInputChange}
            rows={3}
            className="input-field"
            placeholder="Enter the reason for the refund (optional)"
          />
          <p className="text-sm text-gray-500 mt-1">
            Providing a reason helps with transaction records and customer service
          </p>
        </div>

        {/* Important Notice */}
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <div className="flex items-start">
            <ExclamationCircleIcon className="w-5 h-5 text-yellow-600 mt-0.5 mr-3 flex-shrink-0" />
            <div>
              <h4 className="text-sm font-medium text-yellow-800">Important Notice</h4>
              <p className="text-sm text-yellow-700 mt-1">
                • Refund amount must be equal to or less than the original payment amount
                <br />
                • Refunds are processed asynchronously and may take time to complete
                <br />
                • You will receive a refund ID to track the status of your refund
              </p>
            </div>
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <div className="flex items-start">
              <ExclamationCircleIcon className="w-5 h-5 text-red-600 mt-0.5 mr-3 flex-shrink-0" />
              <p className="text-red-800 text-sm">{error}</p>
            </div>
          </div>
        )}

        {/* Success Message */}
        {success && (
          <div className="bg-green-50 border border-green-200 rounded-lg p-4">
            <div className="flex items-start">
              <CheckCircleIcon className="w-5 h-5 text-green-600 mt-0.5 mr-3 flex-shrink-0" />
              <div>
                <p className="text-green-800 text-sm font-medium">Refund Request Created Successfully!</p>
                <p className="text-green-700 text-sm mt-1">{success}</p>
                {refundId && (
                  <div className="mt-2 p-2 bg-green-100 rounded border">
                    <p className="text-green-800 text-xs">
                      <strong>Refund ID:</strong> <span className="font-mono">{refundId}</span>
                    </p>
                  </div>
                )}
              </div>
            </div>
          </div>
        )}

        {/* Submit Button */}
        <button
          type="submit"
          disabled={loading}
          className="btn-primary w-full disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <ArrowPathIcon className="w-5 h-5 mr-2" />
          {loading ? 'Creating Refund...' : 'Create Refund Request'}
        </button>
      </form>

      {/* Additional Information */}
      <div className="mt-8 card">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Refund Process Information</h3>
        <div className="space-y-3 text-sm text-gray-600">
          <div className="flex items-start">
            <div className="w-2 h-2 bg-light-blue-500 rounded-full mt-2 mr-3 flex-shrink-0"></div>
            <p>
              <strong>Step 1:</strong> Submit the refund request with the payment ID and amount
            </p>
          </div>
          <div className="flex items-start">
            <div className="w-2 h-2 bg-light-blue-500 rounded-full mt-2 mr-3 flex-shrink-0"></div>
            <p>
              <strong>Step 2:</strong> The system validates the payment and refund details
            </p>
          </div>
          <div className="flex items-start">
            <div className="w-2 h-2 bg-light-blue-500 rounded-full mt-2 mr-3 flex-shrink-0"></div>
            <p>
              <strong>Step 3:</strong> If approved, the refund is processed and funds are returned
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Refunds;