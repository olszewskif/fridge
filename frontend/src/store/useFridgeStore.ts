import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Fridge } from '../features/fridges/types/fridge.types';

interface FridgeState {
  activeFridge: Fridge | null;
  setActiveFridge: (fridge: Fridge | null) => void;
}

export const useFridgeStore = create<FridgeState>()(
  persist(
    (set) => ({
      activeFridge: null,
      setActiveFridge: (fridge) => set({ activeFridge: fridge }),
    }),
    {
      name: 'active-fridge-storage',
      storage: createJSONStorage(() => AsyncStorage),
    }
  )
);
