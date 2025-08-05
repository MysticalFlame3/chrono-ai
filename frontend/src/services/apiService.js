import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export const registerUser = (userData) => {
  return api.post('/auth/register', userData);
};

export const loginUser = async (userData) => {
  const response = await api.post('/auth/login', userData);
  if (response.data.jwt) {
    localStorage.setItem('token', response.data.jwt);
  }
  return response.data;
};

export const logoutUser = () => {
  localStorage.removeItem('token');
};

export const getTasks = () => {
  return api.get('/tasks');
};

export const createTask = (taskData) => {
  return api.post('/tasks', taskData);
};

export const parseQueryToCron = (query) => {
  return api.post('/tasks/parse', { query });
};

export const deleteTask = (taskId) => {
  return api.delete(`/tasks/${taskId}`);
};

export const getTaskHistory = (taskId) => {
  return api.get(`/tasks/${taskId}/history`);
};