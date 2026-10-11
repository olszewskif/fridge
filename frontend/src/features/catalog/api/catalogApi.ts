import { apiClient } from '../../../api/client';
import { PageResponse } from '../../../api/types';
import { ProductResponse, CategoryResponse, UpdateProductRequest } from '../types/catalog.types';

export const catalogApi = {
  scanProduct: async (barcode: string): Promise<ProductResponse> => {
    // Hits the backend endpoint that checks DB or fetches OpenFoodFacts!
    const response = await apiClient.post<ProductResponse>(`/products/scan/${barcode}`);
    return response.data;
  },

  getCategories: async (): Promise<PageResponse<CategoryResponse>> => {
    const response = await apiClient.get<PageResponse<CategoryResponse>>('/categories');
    return response.data;
  },

  getProductById: async (id: string): Promise<ProductResponse> => {
    const response = await apiClient.get<ProductResponse>(`/products/${id}`);
    return response.data;
  },

  updateProduct: async (id: string, data: UpdateProductRequest): Promise<ProductResponse> => {
    const response = await apiClient.patch<ProductResponse>(`/products/${id}`, data);
    return response.data;
  },

  deleteProduct: async (id: string): Promise<void> => {
    await apiClient.delete(`/products/${id}`);
  },
};
