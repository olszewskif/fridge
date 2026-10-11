import { Platform } from 'react-native';

/**
 * Global environment and network configuration.
 * On real devices, replace LOCAL_IP with your computer's Wi-Fi IPv4 address.
 * On Android Emulator: use 10.0.2.2.
 * On iOS Simulator or Web: use localhost.
 */
const LOCAL_IP = '192.168.1.149'; // Your Wi-Fi router IP accessible by mobile devices
const PORT = '8080';

const getDefaultApiUrl = () => {
  if (Platform.OS === 'android') {
    // If running in Android emulator:
    // return `http://10.0.2.2:${PORT}/api/v1`;
    return `http://${LOCAL_IP}:${PORT}/api/v1`;
  }
  if (Platform.OS === 'web') {
    return `http://localhost:${PORT}/api/v1`;
  }
  // iOS or physical device:
  return `http://${LOCAL_IP}:${PORT}/api/v1`;
};

export const ENV = {
  API_BASE_URL: getDefaultApiUrl(),
  API_TIMEOUT_MS: 10000,
};
