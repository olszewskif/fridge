import { useQuery } from '@tanstack/react-query';
import { catalogApi } from '../api/catalogApi';
import { CategoryResponse } from '../types/catalog.types';

export const CATEGORIES_QUERY_KEY = ['categories'];

export function useCategories() {
  return useQuery({
    queryKey: CATEGORIES_QUERY_KEY,
    queryFn: async () => {
      const page = await catalogApi.getCategories();
      return page.content || [];
    },
    staleTime: 1000 * 60 * 10, // 10 minutes cache
  });
}
