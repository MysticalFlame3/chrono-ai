import React, { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import DashboardPage from './pages/DashboardPage';
import AuthPage from './pages/AuthPage';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  return (
    <div className="min-h-screen bg-gradient-to-br from-neutral-900 via-neutral-800 to-neutral-700 font-sans">
      {isLoggedIn ? (
        <DashboardPage setIsLoggedIn={setIsLoggedIn} />
      ) : (
        <AuthPage setIsLoggedIn={setIsLoggedIn} />
      )}

      {/* Toast provider for beautiful notifications */}
      <Toaster
        position="top-right"
        toastOptions={{
          style: {
            background: '#1f2937', // bg-neutral-800
            color: '#f9fafb', // text-neutral-50
            borderRadius: '12px',
            border: '1px solid rgba(255, 255, 255, 0.1)',
            boxShadow: '0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04)',
          },
          duration: 3000,
          success: {
            duration: 2000,
            iconTheme: {
              primary: '#10b981',
              secondary: '#ffffff',
            },
          },
          error: {
            duration: 4000,
            iconTheme: {
              primary: '#ef4444',
              secondary: '#ffffff',
            },
          },
        }}
        gutter={16}
        containerStyle={{
          top: 20,
          right: 20,
        }}
      />
    </div>
  );
}

export default App;
