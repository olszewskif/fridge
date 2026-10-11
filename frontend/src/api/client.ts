import axios from 'axios';
import { ENV } from '../config/env';

export const apiClient = axios.create({
  baseURL: ENV.API_BASE_URL,
  timeout: ENV.API_TIMEOUT_MS,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

// Response interceptor for clear debugging in dev
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (__DEV__) {
      const url = error.config?.url;
      const method = error.config?.method?.toUpperCase();
      const status = error.response?.status;
      const detail = error.response?.data?.detail || error.message;
      console.warn(`[API ERROR] ${method} ${url} -> ${status}: ${detail}`);
    }
    return Promise.reject(error);
  }
);
