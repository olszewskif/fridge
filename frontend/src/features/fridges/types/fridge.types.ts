export interface Fridge {
  id: string;
  name: string;
  createdAt: string;
  createdBy: string;
}

export interface CreateFridgeRequest {
  name: string;
}

export interface FridgeFilterParams {
  search?: string;
  ownerId?: string;
  page?: number;
  size?: number;
}
