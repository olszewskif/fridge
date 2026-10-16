import { apiClient } from '../../../api/client';
import {
  LoginUserRequest,
  LoginUserResponse,
  RegisterUserRequest,
  RegisterUserResponse,
  RefreshTokenRequest,
  RefreshTokenResponse,
  LogoutUserRequest,
} from '../types/auth.types';

export const authApi = {
  login: async (credentials: LoginUserRequest): Promise<LoginUserResponse> => {
    const response = await apiClient.post<LoginUserResponse>('/auth/login', credentials);
    return response.data;
  },

  register: async (data: RegisterUserRequest): Promise<RegisterUserResponse> => {
    const response = await apiClient.post<RegisterUserResponse>('/auth/signup', data);
    return response.data;
  },

  refreshToken: async (data: RefreshTokenRequest): Promise<RefreshTokenResponse> => {
    const response = await apiClient.post<RefreshTokenResponse>('/auth/refresh', data);
    return response.data;
  },

  logout: async (data: LogoutUserRequest): Promise<void> => {
    await apiClient.post('/auth/logout', data);
  },
};
