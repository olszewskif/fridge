export interface LoginUserRequest {
  email: string;
  password: string;
}

export interface RegisterUserRequest {
  displayName: string;
  email: string;
  password: string;
}

export interface RefreshTokenRequest {
  token: string;
}

export interface LogoutUserRequest {
  refreshToken: string;
}

export interface LoginUserResponse {
  userId: string;
  accessToken: string;
  refreshToken: string;
}

export interface RegisterUserResponse {
  userId: string;
  accessToken: string;
  refreshToken: string;
}

export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface AuthUser {
  id: string;
  email: string;
  role: string;
  displayName?: string;
}

export interface JwtClaims {
  id: string;
  sub: string;
  role: string;
  iat?: number;
  exp?: number;
}
