# Paymentic Frontend

A modern, responsive React frontend application for the Paymentic payment processing platform. Built with TypeScript, TailwindCSS, and featuring a beautiful light blue and light purple color scheme.

## Features

- **Create Payments**: Process new payment transactions with buyer information, credit card details, and payment orders
- **View Payments**: Search for and view payment details, status, and associated payment orders
- **Refunds**: Request refunds for completed payment transactions
- **Responsive Design**: Modern UI that works on desktop and mobile devices
- **Beautiful UI**: Light blue and light purple gradient theme with smooth animations

## Tech Stack

- **React 19**: Modern React with hooks and functional components
- **TypeScript**: Type-safe JavaScript for better development experience
- **TailwindCSS**: Utility-first CSS framework for rapid UI development
- **React Router**: Client-side routing for SPA navigation
- **Axios**: HTTP client for API requests
- **Heroicons**: Beautiful SVG icons

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open your browser and navigate to `http://localhost:3000`

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   └── Layout/
│   │       ├── Header.tsx          # Navigation header
│   │       └── Layout.tsx          # Main layout wrapper
│   │   ├── pages/
│   │   │   ├── CreatePayment.tsx       # Payment creation form
│   │   │   ├── ViewPayments.tsx        # Payment search and details
│   │   │   └── Refunds.tsx             # Refund request form
│   │   ├── services/
│   │   │   └── api.ts                  # API service layer
│   │   ├── types/
│   │   │   └── api.ts                  # TypeScript type definitions
│   │   ├── App.tsx                     # Main application component
│   │   ├── index.tsx                   # Application entry point
│   │   └── index.css                   # Global styles and TailwindCSS
│   ├── public/
│   ├── package.json
│   ├── tailwind.config.js              # TailwindCSS configuration
│   └── postcss.config.js               # PostCSS configuration
```

## API Integration

The frontend integrates with the Paymentic API through the following endpoints:

- `POST /payments` - Create a new payment
- `GET /payments/{id}` - Get payment details
- `GET /payments/{id}/payment-orders` - Get payment orders
- `POST /refunds` - Create a refund request

### API Configuration

The API base URL is configured in `src/services/api.ts`. By default, it points to `http://localhost:8080`. You can modify this to point to your backend server.

## Color Scheme

The application uses a custom light blue and light purple color palette:

- **Light Blue**: `#0ea5e9` (primary), `#38bdf8` (secondary)
- **Light Purple**: `#a855f7` (primary), `#c084fc` (secondary)

## Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm run eject` - Eject from Create React App

## Features in Detail

### Create Payment
- Comprehensive form with buyer information, credit card details, and payment orders
- Input validation and error handling
- Success feedback with payment ID
- Automatic navigation to payment details

### View Payments
- Search payments by ID
- Display payment status and details
- Show associated payment orders
- Quick access to create refunds

### Refunds
- Create refund requests for existing payments
- Support for partial refunds
- Optional reason field
- Process information and status tracking

## Customization

### Styling
The application uses TailwindCSS for styling. Custom styles and components are defined in:
- `src/index.css` - Global styles and component classes
- `tailwind.config.js` - TailwindCSS configuration with custom colors

### API Configuration
Modify `src/services/api.ts` to change:
- Base URL
- Request/response interceptors
- Error handling
- Authentication headers

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

This project is licensed under the MIT License.
