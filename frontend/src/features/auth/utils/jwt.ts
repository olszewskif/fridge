import { AuthUser, JwtClaims } from '../types/auth.types';

const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

function atobPolyfill(input: string = ''): string {
  const str = input.replace(/=+$/, '');
  if (str.length % 4 === 1) {
    throw new Error("'atob' failed: The string to be decoded is not correctly encoded.");
  }
  let output = '';
  for (
    let bc = 0, bs = 0, buffer, i = 0;
    (buffer = str.charAt(i++));
    ~buffer && ((bs = bc % 4 ? bs * 64 + buffer : buffer), bc++ % 4)
      ? (output += String.fromCharCode(255 & (bs >> ((-2 * bc) & 6))))
      : 0
  ) {
    buffer = chars.indexOf(buffer);
  }
  return output;
}

export function decodeJwt(token: string): JwtClaims | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    while (base64.length % 4 !== 0) {
      base64 += '=';
    }

    const decodeFn = typeof atob === 'function' ? atob : atobPolyfill;
    const decodedStr = decodeFn(base64);

    try {
      const jsonStr = decodeURIComponent(
        Array.prototype.map
          .call(decodedStr, (c: string) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonStr) as JwtClaims;
    } catch {
      return JSON.parse(decodedStr) as JwtClaims;
    }
  } catch {
    return null;
  }
}

export function isTokenExpired(token: string, bufferSeconds = 10): boolean {
  const claims = decodeJwt(token);
  if (!claims || !claims.exp) return false;
  return Date.now() >= (claims.exp - bufferSeconds) * 1000;
}

export function parseUserFromToken(token: string, fallbackDisplayName?: string): AuthUser | null {
  const claims = decodeJwt(token);
  if (!claims) return null;

  const email = claims.sub || '';
  const fallback = fallbackDisplayName || (email ? email.split('@')[0] : 'User');

  return {
    id: claims.id,
    email: email,
    role: claims.role,
    displayName: fallback,
  };
}
