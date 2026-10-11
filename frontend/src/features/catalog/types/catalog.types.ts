export interface CategorySummary {
  id: string;
  name: string;
  parentId?: string | null;
}

export interface NetQuantityDto {
  amount: number;
  unit: string;
}

export interface ProductResponse {
  id: string;
  name: string;
  brand?: string;
  barcode: string;
  netQuantityDto?: NetQuantityDto | null;
  imageUrl?: string;
  categories: CategorySummary[];
}

export interface CategoryResponse {
  id: string;
  name: string;
  parentId?: string | null;
}

export interface UpdateProductRequest {
  name?: string;
  brand?: string;
  barcode?: string;
  netQuantity?: NetQuantityDto | null;
  imageUrl?: string;
}
