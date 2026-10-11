import { apiClient } from '../../../api/client';
import { PageResponse } from '../../../api/types';
import { Fridge, CreateFridgeRequest, FridgeFilterParams } from '../types/fridge.types';

export const fridgeApi = {
  getFridges: async (params?: FridgeFilterParams): Promise<PageResponse<Fridge>> => {
    const response = await apiClient.get<PageResponse<Fridge>>('/fridges', { params });
    return response.data;
  },

  getFridgeById: async (id: string): Promise<Fridge> => {
    const response = await apiClient.get<Fridge>(`/fridges/${id}`);
    return response.data;
  },

  createFridge: async (data: CreateFridgeRequest): Promise<Fridge> => {
    const response = await apiClient.post<Fridge>('/fridges', data);
    return response.data;
  },

  deleteFridge: async (id: string): Promise<void> => {
    await apiClient.delete(`/fridges/${id}`);
  },
};
