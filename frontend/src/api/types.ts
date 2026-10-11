export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface ApiError {
  status: number;
  title?: string;
  detail?: string;
  instance?: string;
  errors?: Array<{
    field: string;
    message: string;
  }>;
}
