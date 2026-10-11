import { useMutation } from '@tanstack/react-query';
import { catalogApi } from '../api/catalogApi';
import { ProductResponse } from '../types/catalog.types';

export function useScanProduct() {
  return useMutation<ProductResponse, Error, string>({
    mutationFn: (barcode: string) => catalogApi.scanProduct(barcode),
  });
}
