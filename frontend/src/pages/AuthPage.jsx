import React, { useState } from 'react';
import LoginForm from '../components/LoginForm';
import RegistrationForm from '../components/RegistrationForm';
import { Bot } from 'lucide-react';

const AuthPage = ({ setIsLoggedIn }) => {
  const [showLogin, setShowLogin] = useState(true);

  return (
    <div className="min-h-screen bg-gradient-to-br from-neutral-900 via-neutral-800 to-neutral-700 flex items-center justify-center p-4">
      <div className="w-full max-w-6xl">
        <div className="flex flex-col lg:flex-row w-full h-full min-h-[600px] lg:min-h-[700px] bg-white rounded-3xl shadow-2xl overflow-hidden">
          
          {/* Left Side - Logo / Info */}
          <div className="hidden lg:flex flex-col justify-center items-center bg-gradient-to-br from-primary-50 to-accent-50 text-neutral-900 w-full lg:w-1/2 p-8 lg:p-16">
            <div className="text-center">
              <div className="bg-gradient-to-br from-primary-500 to-accent-500 p-6 rounded-full mb-8 shadow-2xl">
                <Bot className="w-20 h-20 text-white" />
              </div>
              <h1 className="text-4xl lg:text-6xl font-extrabold mb-6 tracking-wide bg-gradient-to-r from-primary-600 to-accent-600 bg-clip-text text-transparent">
                ChronoAI
              </h1>
              <p className="text-lg lg:text-xl text-neutral-600 leading-relaxed max-w-md">
                Streamline your workflow. Manage your tasks efficiently and professionally with AI-powered scheduling.
              </p>
            </div>
          </div>

          {/* Right Side - Form */}
          <div className="flex flex-col justify-center items-center w-full lg:w-1/2 bg-white p-6 sm:p-8 lg:p-12 xl:p-16">
            
            {/* Mobile Logo */}
            <div className="lg:hidden text-center mb-8">
              <div className="bg-gradient-to-br from-primary-500 to-accent-500 p-4 rounded-full inline-block mb-4 shadow-lg">
                <Bot className="w-12 h-12 text-white" />
              </div>
              <h1 className="text-3xl font-extrabold bg-gradient-to-r from-primary-600 to-accent-600 bg-clip-text text-transparent">
                ChronoAI
              </h1>
            </div>

            {/* Toggle Buttons */}
            <div className="flex justify-center gap-2 sm:gap-4 mb-8 lg:mb-12 w-full max-w-sm">
              <button
                onClick={() => setShowLogin(true)}
                className={`flex-1 px-4 sm:px-6 py-3 rounded-full font-semibold transition-all duration-300 text-sm sm:text-base ${
                  showLogin
                    ? 'bg-gradient-to-r from-primary-600 to-primary-700 text-white shadow-lg transform scale-105'
                    : 'bg-neutral-100 text-neutral-600 hover:bg-neutral-200 hover:text-neutral-700'
                }`}
              >
                Login
              </button>
              <button
                onClick={() => setShowLogin(false)}
                className={`flex-1 px-4 sm:px-6 py-3 rounded-full font-semibold transition-all duration-300 text-sm sm:text-base ${
                  !showLogin
                    ? 'bg-gradient-to-r from-primary-600 to-primary-700 text-white shadow-lg transform scale-105'
                    : 'bg-neutral-100 text-neutral-600 hover:bg-neutral-200 hover:text-neutral-700'
                }`}
              >
                Register
              </button>
            </div>

            {/* Form */}
            <div className="w-full max-w-sm sm:max-w-md">
              {showLogin ? (
                <LoginForm setIsLoggedIn={setIsLoggedIn} />
              ) : (
                <RegistrationForm setIsLoggedIn={setIsLoggedIn} />
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthPage;
