import { apiClient } from '../../../api/client';
import { PageResponse } from '../../../api/types';
import {
  FridgeItem,
  AddFridgeItemRequest,
  UpdateFridgeItemRequest,
  FridgeItemsFilterParams,
} from '../types/inventory.types';

export const inventoryApi = {
  getItems: async (
    fridgeId: string,
    params?: FridgeItemsFilterParams
  ): Promise<PageResponse<FridgeItem>> => {
    const response = await apiClient.get<PageResponse<FridgeItem>>(
      `/fridges/${fridgeId}/items`,
      { params }
    );
    return response.data;
  },

  getItemById: async (fridgeId: string, itemId: string): Promise<FridgeItem> => {
    const response = await apiClient.get<FridgeItem>(`/fridges/${fridgeId}/items/${itemId}`);
    return response.data;
  },

  addItem: async (fridgeId: string, data: AddFridgeItemRequest): Promise<FridgeItem> => {
    const response = await apiClient.post<FridgeItem>(`/fridges/${fridgeId}/items`, data);
    return response.data;
  },

  updateItem: async (
    fridgeId: string,
    itemId: string,
    data: UpdateFridgeItemRequest
  ): Promise<FridgeItem> => {
    const response = await apiClient.patch<FridgeItem>(
      `/fridges/${fridgeId}/items/${itemId}`,
      data
    );
    return response.data;
  },

  removeItem: async (fridgeId: string, itemId: string): Promise<void> => {
    await apiClient.delete(`/fridges/${fridgeId}/items/${itemId}`);
  },

  bulkRemoveItems: async (fridgeId: string, itemIds: string[]): Promise<void> => {
    await apiClient.post(`/fridges/${fridgeId}/items/bulk-delete`, { itemIds });
  },
};
