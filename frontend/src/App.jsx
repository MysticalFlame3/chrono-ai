import { useState, useEffect } from 'react';
import RegistrationForm from './components/RegistrationForm';
import LoginForm from './components/LoginForm';
import DashboardPage from './pages/DashboardPage';

function App() {
  // Check if a token exists in local storage to keep the user logged in
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  const [isLoginView, setIsLoginView] = useState(true);

  if (isLoggedIn) {
    return (
      <div className="bg-gray-900 text-white min-h-screen p-4">
        <DashboardPage setIsLoggedIn={setIsLoggedIn} />
      </div>
    );
  }

  return (
    <div className="bg-gray-900 text-white min-h-screen flex flex-col items-center justify-center p-4">
      <div className="w-full max-w-md">
        {isLoginView ? (
          <LoginForm setIsLoggedIn={setIsLoggedIn} />
        ) : (
          <RegistrationForm />
        )}
        <button
          onClick={() => setIsLoginView(!isLoginView)}
          className="mt-4 text-sm text-blue-400 hover:underline"
        >
          {isLoginView
            ? 'Need an account? Register'
            : 'Already have an account? Login'}
        </button>
      </div>
    </div>
  );
}

export default App;