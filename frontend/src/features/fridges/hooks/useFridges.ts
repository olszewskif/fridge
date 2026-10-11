import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useEffect } from 'react';
import { fridgeApi } from '../api/fridgeApi';
import { Fridge, FridgeFilterParams, CreateFridgeRequest } from '../types/fridge.types';
import { useFridgeStore } from '../../../store/useFridgeStore';

export const FRIDGES_QUERY_KEY = ['fridges'];

export function useFridges(params?: FridgeFilterParams) {
  const { activeFridge, setActiveFridge } = useFridgeStore();

  const query = useQuery({
    queryKey: [...FRIDGES_QUERY_KEY, params],
    queryFn: () => fridgeApi.getFridges(params),
  });

  // If no active fridge is set, or current active fridge was deleted,
  // automatically select the first available fridge!
  useEffect(() => {
    if (query.data?.content && query.data.content.length > 0) {
      if (!activeFridge || !query.data.content.some((f) => f.id === activeFridge.id)) {
        setActiveFridge(query.data.content[0]);
      }
    }
  }, [query.data, activeFridge, setActiveFridge]);

  return query;
}

export function useCreateFridge() {
  const queryClient = useQueryClient();
  const { setActiveFridge } = useFridgeStore();

  return useMutation({
    mutationFn: (data: CreateFridgeRequest) => fridgeApi.createFridge(data),
    onSuccess: (newFridge) => {
      queryClient.invalidateQueries({ queryKey: FRIDGES_QUERY_KEY });
      setActiveFridge(newFridge);
    },
  });
}
