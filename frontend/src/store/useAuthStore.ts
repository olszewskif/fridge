import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AuthUser } from '../features/auth/types/auth.types';
import { parseUserFromToken } from '../features/auth/utils/jwt';

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  user: AuthUser | null;
  isAuthenticated: boolean;
  hasHydrated: boolean;
  setAuth: (accessToken: string, refreshToken: string, user: AuthUser) => void;
  updateTokens: (accessToken: string, refreshToken: string) => void;
  logout: () => void;
  setHasHydrated: (hydrated: boolean) => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      accessToken: null,
      refreshToken: null,
      user: null,
      isAuthenticated: false,
      hasHydrated: false,
      setAuth: (accessToken, refreshToken, user) => {
        set({
          accessToken,
          refreshToken,
          user,
          isAuthenticated: true,
        });
      },
      updateTokens: (accessToken, refreshToken) => {
        const currentUser = get().user;
        const updatedUser = currentUser || parseUserFromToken(accessToken);
        set({
          accessToken,
          refreshToken,
          user: updatedUser,
          isAuthenticated: true,
        });
      },
      logout: () => {
        set({
          accessToken: null,
          refreshToken: null,
          user: null,
          isAuthenticated: false,
        });
      },
      setHasHydrated: (hasHydrated) => set({ hasHydrated }),
    }),
    {
      name: 'auth-storage',
      storage: createJSONStorage(() => AsyncStorage),
      onRehydrateStorage: () => (state) => {
        if (state) {
          if (state.accessToken && !state.user) {
            const parsed = parseUserFromToken(state.accessToken);
            if (parsed) {
              state.user = parsed;
            }
          }
          state.isAuthenticated = !!(state.accessToken && state.refreshToken);
        }
        useAuthStore.getState().setHasHydrated(true);
      },
    }
  )
);
