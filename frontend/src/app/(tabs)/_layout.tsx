import React from 'react';
import { View, StyleSheet, TouchableOpacity, Platform } from 'react-native';
import { Tabs, useRouter } from 'expo-router';
import { Home, Package, Camera } from 'lucide-react-native';
import { Theme } from '../../constants/theme';

export default function TabsLayout() {
  const router = useRouter();

  return (
    <Tabs
      screenOptions={{
        headerShown: false,
        tabBarStyle: styles.tabBar,
        tabBarActiveTintColor: '#fff',
        tabBarInactiveTintColor: Theme.colors.textMuted,
        tabBarShowLabel: true,
        tabBarLabelStyle: styles.tabBarLabel,
      }}
    >
      {/* Tab 1: Home Dashboard */}
      <Tabs.Screen
        name="index"
        options={{
          title: 'Home',
          tabBarIcon: ({ color, focused }) => (
            <View style={[styles.iconWrap, focused && styles.iconWrapActive]}>
              <Home size={22} color={focused ? '#fff' : color} strokeWidth={focused ? 2.5 : 2} />
            </View>
          ),
        }}
      />

      {/* Center Action Button: Quick Barcode Scan */}
      <Tabs.Screen
        name="scan-trigger"
        options={{
          title: '',
          tabBarButton: () => (
            <TouchableOpacity
              style={styles.centerScanBtn}
              onPress={() => router.push('/scan')}
              activeOpacity={0.8}
            >
              <View style={styles.centerScanInner}>
                <Camera size={26} color="#fff" strokeWidth={2.5} />
              </View>
            </TouchableOpacity>
          ),
        }}
      />

      {/* Tab 3: Full Inventory */}
      <Tabs.Screen
        name="inventory"
        options={{
          title: 'Inventory',
          tabBarIcon: ({ color, focused }) => (
            <View style={[styles.iconWrap, focused && styles.iconWrapActive]}>
              <Package size={22} color={focused ? '#fff' : color} strokeWidth={focused ? 2.5 : 2} />
            </View>
          ),
        }}
      />
    </Tabs>
  );
}

const styles = StyleSheet.create({
  tabBar: {
    backgroundColor: '#0c0c10',
    borderTopWidth: 1,
    borderTopColor: Theme.colors.surfaceBorder,
    height: Platform.OS === 'ios' ? 88 : 70,
    paddingTop: 8,
    paddingBottom: Platform.OS === 'ios' ? 28 : 10,
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
    elevation: 8,
  },
  tabBarLabel: {
    fontSize: 12,
    fontWeight: '600',
    marginTop: 2,
  },
  iconWrap: {
    width: 36,
    height: 36,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 18,
  },
  iconWrapActive: {
    backgroundColor: 'rgba(64, 0, 255, 0.2)',
  },
  centerScanBtn: {
    top: -18,
    justifyContent: 'center',
    alignItems: 'center',
  },
  centerScanInner: {
    width: 58,
    height: 58,
    borderRadius: 29,
    backgroundColor: Theme.colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 8,
    borderWidth: 4,
    borderColor: '#0c0c10', // Cutout effect with tab bar
  },
});
