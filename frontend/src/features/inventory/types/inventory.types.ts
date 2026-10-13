export interface ProductSummaryDto {
  productId: string;
  name: string;
  brand?: string | null;
  barcode: string;
  imageUrl?: string | null;
  netQuantity?: {
    value: number;
    unit: string;
  } | null;
  categories?: {
    id: string;
    name: string;
  }[] | null;
}

export interface FridgeItem {
  id: string;
  productId: string;
  addedBy: string;
  addedAt: string;
  quantity: number;
  expiryDate: string | null; // 'YYYY-MM-DD'
  product?: ProductSummaryDto | null;
  // Optional enriched catalog fields when resolved
  productName?: string;
  brand?: string;
  imageUrl?: string;
}

export interface AddFridgeItemRequest {
  productId: string;
  quantity: number;
  expiryDate?: string | null;
}

export interface UpdateFridgeItemRequest {
  quantity?: number;
  expiryDate?: string | null;
}

export interface FridgeItemsFilterParams {
  search?: string;
  categoryIds?: string[];
  isExpired?: boolean;
  expiresInDays?: number;
  productId?: string;
  addedBy?: string;
  page?: number;
  size?: number;
}
