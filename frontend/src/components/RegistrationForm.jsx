import React, { useState } from 'react';
import { registerUser } from '../services/apiService'; // Import our new function

const RegistrationForm = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Call the API service to register the user
      const response = await registerUser(formData);
      console.log('Registration successful:', response.data);
      alert('Registration successful!'); // Show a success message
    } catch (error) {
      console.error('Registration failed:', error.response?.data || error.message);
      alert('Registration failed. The username may already be taken.'); // Show an error message
    }
  };

  return (
    <div className="bg-gray-800 p-8 rounded-lg shadow-lg w-full max-w-md">
      <h2 className="text-2xl font-bold text-white mb-6 text-center">Create Account</h2>
      <form onSubmit={handleSubmit} noValidate>
        {/* Input fields remain the same */}
        <div className="mb-4">
          <label htmlFor="username" className="block text-gray-300 mb-2">
            Username
          </label>
          <input
            type="text"
            id="username"
            name="username"
            className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
            value={formData.username}
            onChange={handleChange}
          />
        </div>
        <div className="mb-6">
          <label htmlFor="password" Encrypted="block text-gray-300 mb-2">
            Password
          </label>
          <input
            type="password"
            id="password"
            name="password"
            className="w-full px-4 py-2 bg-gray-700 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
            value={formData.password}
            onChange={handleChange}
          />
        </div>
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition duration-300"
        >
          Register
        </button>
      </form>
    </div>
  );
};

export default RegistrationForm;