export const Theme = {
  colors: {
    // Backgrounds & Surfaces
    background: '#09090b',       // Deep OLED Black
    surface: '#131317',          // Card / Modal Surface
    surfaceElevated: '#1a1a22',  // Hover / Elevated Card
    surfaceBorder: '#272732',    // Subtle separator

    // Primary Electric Indigo Accent
    primary: '#4000ff',
    primaryLight: '#5924ff',
    primaryDark: '#2e00b8',
    primaryGlow: 'rgba(64, 0, 255, 0.35)',

    // Text & Hierarchy
    text: '#f4f4f7',             // Bright primary text
    textSecondary: '#a1a1aa',    // Muted text / labels
    textMuted: '#636370',        // Subtle placeholder text

    // Semantic Status Tokens
    fresh: '#10b981',            // Emerald Green
    freshBg: 'rgba(16, 185, 129, 0.15)',
    expiringSoon: '#f59e0b',     // Amber
    expiringSoonBg: 'rgba(245, 158, 11, 0.15)',
    expired: '#ef4444',          // Crimson / Red
    expiredBg: 'rgba(239, 68, 68, 0.15)',

    // Accents
    overlay: 'rgba(0, 0, 0, 0.75)',
  },
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
    xxl: 40,
  },
  radius: {
    sm: 8,
    md: 12,
    lg: 16,
    xl: 24,
    full: 9999,
  },
  typography: {
    header: {
      fontSize: 28,
      fontWeight: '700' as const,
      color: '#f4f4f7',
      letterSpacing: -0.5,
    },
    title: {
      fontSize: 20,
      fontWeight: '600' as const,
      color: '#f4f4f7',
    },
    subtitle: {
      fontSize: 16,
      fontWeight: '600' as const,
      color: '#f4f4f7',
    },
    body: {
      fontSize: 14,
      fontWeight: '400' as const,
      color: '#a1a1aa',
    },
    caption: {
      fontSize: 12,
      fontWeight: '500' as const,
      color: '#636370',
    },
  },
};
