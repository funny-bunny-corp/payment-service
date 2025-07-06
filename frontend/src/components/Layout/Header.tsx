import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { CreditCardIcon, ArrowPathIcon, MagnifyingGlassIcon } from '@heroicons/react/24/outline';

const Header: React.FC = () => {
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <header className="bg-white/90 backdrop-blur-sm shadow-lg border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center">
            <Link to="/" className="flex items-center space-x-2">
              <div className="w-8 h-8 bg-gradient-to-r from-light-blue-500 to-light-purple-500 rounded-lg flex items-center justify-center">
                <CreditCardIcon className="w-5 h-5 text-white" />
              </div>
              <span className="text-xl font-bold bg-gradient-to-r from-light-blue-600 to-light-purple-600 bg-clip-text text-transparent">
                Paymentic
              </span>
            </Link>
          </div>

          {/* Navigation */}
          <nav className="hidden md:flex space-x-8">
            <Link
              to="/"
              className={`flex items-center space-x-2 px-3 py-2 rounded-lg transition-all duration-300 ${
                isActive('/')
                  ? 'bg-gradient-to-r from-light-blue-100 to-light-purple-100 text-gray-800'
                  : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
              }`}
            >
              <CreditCardIcon className="w-5 h-5" />
              <span>Create Payment</span>
            </Link>
            
            <Link
              to="/payments"
              className={`flex items-center space-x-2 px-3 py-2 rounded-lg transition-all duration-300 ${
                isActive('/payments')
                  ? 'bg-gradient-to-r from-light-blue-100 to-light-purple-100 text-gray-800'
                  : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
              }`}
            >
              <MagnifyingGlassIcon className="w-5 h-5" />
              <span>View Payments</span>
            </Link>
            
            <Link
              to="/refunds"
              className={`flex items-center space-x-2 px-3 py-2 rounded-lg transition-all duration-300 ${
                isActive('/refunds')
                  ? 'bg-gradient-to-r from-light-blue-100 to-light-purple-100 text-gray-800'
                  : 'text-gray-600 hover:text-gray-800 hover:bg-gray-100'
              }`}
            >
              <ArrowPathIcon className="w-5 h-5" />
              <span>Refunds</span>
            </Link>
          </nav>

          {/* Mobile menu button */}
          <div className="md:hidden">
            <button className="text-gray-600 hover:text-gray-800 p-2">
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;