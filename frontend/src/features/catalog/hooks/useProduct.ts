import { useQuery } from '@tanstack/react-query';
import { catalogApi } from '../api/catalogApi';

export function useProduct(productId?: string) {
  return useQuery({
    queryKey: ['product', productId],
    queryFn: () => {
      if (!productId) return null;
      return catalogApi.getProductById(productId);
    },
    enabled: !!productId,
    staleTime: 1000 * 60 * 10, // 10 minutes cache
  });
}
