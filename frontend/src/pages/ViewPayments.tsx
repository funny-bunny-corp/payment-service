import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, CheckCircleIcon, ClockIcon } from '@heroicons/react/24/outline';
import { paymentApi } from '../services/api';
import { PaymentData, PaymentOrderStatus, ApiError } from '../types/api';

const ViewPayments: React.FC = () => {
  const navigate = useNavigate();
  const [paymentId, setPaymentId] = useState('');
  const [paymentData, setPaymentData] = useState<PaymentData | null>(null);
  const [paymentOrders, setPaymentOrders] = useState<PaymentOrderStatus[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!paymentId.trim()) {
      setError('Please enter a payment ID');
      return;
    }

    setLoading(true);
    setError(null);
    setPaymentData(null);
    setPaymentOrders([]);

    try {
      // Fetch payment data and payment orders in parallel
      const [payment, orders] = await Promise.all([
        paymentApi.getPayment(paymentId),
        paymentApi.getPaymentOrders(paymentId),
      ]);

      setPaymentData(payment);
      setPaymentOrders(orders);
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError.description || 'Failed to fetch payment data');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'DONE':
      case 'SUCCESS':
        return 'text-green-600 bg-green-100';
      case 'IN_PROGRESS':
      case 'EXECUTING':
        return 'text-yellow-600 bg-yellow-100';
      case 'NOT_STARTED':
        return 'text-gray-600 bg-gray-100';
      case 'FAILED':
        return 'text-red-600 bg-red-100';
      default:
        return 'text-gray-600 bg-gray-100';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'DONE':
      case 'SUCCESS':
        return <CheckCircleIcon className="w-5 h-5" />;
      case 'IN_PROGRESS':
      case 'EXECUTING':
      case 'NOT_STARTED':
        return <ClockIcon className="w-5 h-5" />;
      default:
        return <ClockIcon className="w-5 h-5" />;
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold bg-gradient-to-r from-light-blue-600 to-light-purple-600 bg-clip-text text-transparent mb-2">
          View Payments
        </h1>
        <p className="text-gray-600">
          Search and view payment details and status
        </p>
      </div>

      {/* Search Form */}
      <form onSubmit={handleSearch} className="card mb-8">
        <div className="flex items-center space-x-4">
          <div className="flex-1">
            <label htmlFor="paymentId" className="block text-sm font-medium text-gray-700 mb-1">
              Payment ID
            </label>
            <input
              type="text"
              id="paymentId"
              value={paymentId}
              onChange={(e) => setPaymentId(e.target.value)}
              className="input-field"
              placeholder="Enter payment ID to search..."
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="btn-primary mt-6 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <MagnifyingGlassIcon className="w-5 h-5 mr-2" />
            {loading ? 'Searching...' : 'Search'}
          </button>
        </div>
      </form>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
          <p className="text-red-800 text-sm">{error}</p>
        </div>
      )}

      {/* Payment Data */}
      {paymentData && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Payment Overview */}
          <div className="card">
            <h3 className="text-lg font-semibold text-gray-800 mb-4">Payment Overview</h3>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Payment ID:</span>
                <span className="font-mono text-sm bg-gray-100 px-2 py-1 rounded">
                  {paymentData.id}
                </span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">Status:</span>
                <span className={`flex items-center space-x-2 px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(paymentData.status)}`}>
                  {getStatusIcon(paymentData.status)}
                  <span>{paymentData.status}</span>
                </span>
              </div>
            </div>
            
            <div className="mt-6 pt-4 border-t border-gray-200">
              <button
                onClick={() => navigate(`/refunds?paymentId=${paymentData.id}`)}
                className="btn-secondary w-full"
              >
                Create Refund
              </button>
            </div>
          </div>

          {/* Payment Orders */}
          <div className="card">
            <h3 className="text-lg font-semibold text-gray-800 mb-4">Payment Orders</h3>
            {paymentOrders.length > 0 ? (
              <div className="space-y-3">
                {paymentOrders.map((order, index) => (
                  <div key={order.id} className="border border-gray-200 rounded-lg p-4">
                    <div className="flex justify-between items-start mb-2">
                      <div className="flex-1">
                        <p className="text-sm font-medium text-gray-800">
                          Order #{index + 1}
                        </p>
                        <p className="text-xs text-gray-600 font-mono">
                          {order.id}
                        </p>
                      </div>
                      <span className={`flex items-center space-x-1 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
                        {getStatusIcon(order.status)}
                        <span>{order.status}</span>
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-gray-500">
                <ClockIcon className="w-12 h-12 mx-auto mb-2 text-gray-300" />
                <p>No payment orders found</p>
              </div>
            )}
          </div>
        </div>
      )}

      {/* No Results State */}
      {!paymentData && !loading && !error && (
        <div className="card text-center py-12">
          <MagnifyingGlassIcon className="w-16 h-16 mx-auto mb-4 text-gray-300" />
          <h3 className="text-lg font-semibold text-gray-800 mb-2">Search for a Payment</h3>
          <p className="text-gray-600">
            Enter a payment ID above to view payment details and status
          </p>
        </div>
      )}
    </div>
  );
};

export default ViewPayments;