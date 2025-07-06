import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout/Layout';
import CreatePayment from './pages/CreatePayment';
import ViewPayments from './pages/ViewPayments';
import Refunds from './pages/Refunds';
import './App.css';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<CreatePayment />} />
          <Route path="/payments" element={<ViewPayments />} />
          <Route path="/refunds" element={<Refunds />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
