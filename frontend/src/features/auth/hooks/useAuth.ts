import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useRouter } from 'expo-router';
import { authApi } from '../api/authApi';
import { LoginUserRequest, RegisterUserRequest } from '../types/auth.types';
import { useAuthStore } from '../../../store/useAuthStore';
import { useFridgeStore } from '../../../store/useFridgeStore';
import { parseUserFromToken } from '../utils/jwt';

export function useLogin() {
  const { setAuth } = useAuthStore();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (credentials: LoginUserRequest) => authApi.login(credentials),
    onSuccess: (data, variables) => {
      const emailFallback = variables.email.split('@')[0];
      const parsedUser = parseUserFromToken(data.accessToken, emailFallback);
      if (parsedUser) {
        setAuth(data.accessToken, data.refreshToken, parsedUser);
      }
      queryClient.clear();
    },
  });
}

export function useRegister() {
  const { setAuth } = useAuthStore();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: RegisterUserRequest) => authApi.register(data),
    onSuccess: (data, variables) => {
      const parsedUser = parseUserFromToken(data.accessToken, variables.displayName);
      if (parsedUser) {
        setAuth(data.accessToken, data.refreshToken, {
          ...parsedUser,
          displayName: variables.displayName,
        });
      }
      queryClient.clear();
    },
  });
}

export function useLogout() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { refreshToken, logout } = useAuthStore();
  const { setActiveFridge } = useFridgeStore();

  return async () => {
    try {
      if (refreshToken) {
        await authApi.logout({ refreshToken });
      }
    } catch (err) {
      // Even if network fails or token was already revoked, always proceed with client wipe
      if (__DEV__) {
        console.warn('Backend logout encountered error, clearing client state anyway:', err);
      }
    } finally {
      logout();
      setActiveFridge(null);
      queryClient.clear();
      router.replace('/(auth)/login' as any);
    }
  };
}
