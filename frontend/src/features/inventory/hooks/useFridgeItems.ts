import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { inventoryApi } from '../api/inventoryApi';
import {
  FridgeItemsFilterParams,
  AddFridgeItemRequest,
  UpdateFridgeItemRequest,
} from '../types/inventory.types';

export const INVENTORY_QUERY_KEY = ['fridge-items'];

export function useFridgeItems(fridgeId?: string | null, params?: FridgeItemsFilterParams) {
  return useQuery({
    queryKey: [...INVENTORY_QUERY_KEY, fridgeId, params],
    queryFn: () => {
      if (!fridgeId) {
        return Promise.resolve({
          content: [],
          totalElements: 0,
          totalPages: 0,
          size: 20,
          number: 0,
          first: true,
          last: true,
          empty: true,
        });
      }
      return inventoryApi.getItems(fridgeId, params);
    },
    enabled: !!fridgeId,
  });
}

export function useFridgeItem(fridgeId?: string | null, itemId?: string | null) {
  return useQuery({
    queryKey: ['fridge-item', fridgeId, itemId],
    queryFn: () => {
      if (!fridgeId || !itemId) return null;
      return inventoryApi.getItemById(fridgeId, itemId);
    },
    enabled: !!fridgeId && !!itemId,
  });
}

export function useAddFridgeItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ fridgeId, data }: { fridgeId: string; data: AddFridgeItemRequest }) =>
      inventoryApi.addItem(fridgeId, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({
        queryKey: [...INVENTORY_QUERY_KEY, variables.fridgeId],
      });
    },
  });
}

export function useUpdateFridgeItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({
      fridgeId,
      itemId,
      data,
    }: {
      fridgeId: string;
      itemId: string;
      data: UpdateFridgeItemRequest;
    }) => inventoryApi.updateItem(fridgeId, itemId, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({
        queryKey: [...INVENTORY_QUERY_KEY, variables.fridgeId],
      });
    },
  });
}

export function useRemoveFridgeItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ fridgeId, itemId }: { fridgeId: string; itemId: string }) =>
      inventoryApi.removeItem(fridgeId, itemId),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({
        queryKey: [...INVENTORY_QUERY_KEY, variables.fridgeId],
      });
    },
  });
}

export function useBulkRemoveFridgeItems() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ fridgeId, itemIds }: { fridgeId: string; itemIds: string[] }) =>
      inventoryApi.bulkRemoveItems(fridgeId, itemIds),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({
        queryKey: [...INVENTORY_QUERY_KEY, variables.fridgeId],
      });
    },
  });
}
