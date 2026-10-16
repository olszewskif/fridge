import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { ChevronDown } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useFridgeStore } from '../../../store/useFridgeStore';
import { useAuthStore } from '../../../store/useAuthStore';
import { FridgeSwitcherModal } from './FridgeSwitcherModal';
import { AccountModal } from '../../auth/components/AccountModal';

export function FridgeHeader() {
  const insets = useSafeAreaInsets();
  const { activeFridge } = useFridgeStore();
  const { user } = useAuthStore();
  const [modalVisible, setModalVisible] = useState(false);
  const [accountVisible, setAccountVisible] = useState(false);

  const initial = (user?.displayName || user?.email || 'U')[0].toUpperCase();

  return (
    <>
      <View style={[styles.container, { paddingTop: Math.max(insets.top, 16) + 8 }]}>
        <View style={styles.topBar}>
          <TouchableOpacity
            style={styles.headerBtn}
            onPress={() => setModalVisible(true)}
            activeOpacity={0.7}
            hitSlop={12}
          >
            <View style={styles.titleRow}>
              <Text style={styles.h1Title} numberOfLines={1}>
                {activeFridge ? activeFridge.name : 'Select Fridge'}
              </Text>
              <View style={styles.chevronPill}>
                <ChevronDown size={18} color={Theme.colors.primaryLight} />
              </View>
            </View>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.avatarBtn}
            onPress={() => setAccountVisible(true)}
            activeOpacity={0.7}
            hitSlop={12}
          >
            <Text style={styles.avatarLetter}>{initial}</Text>
          </TouchableOpacity>
        </View>
      </View>

      <FridgeSwitcherModal visible={modalVisible} onClose={() => setModalVisible(false)} />
      <AccountModal visible={accountVisible} onClose={() => setAccountVisible(false)} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.md,
  },
  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  headerBtn: {
    flex: 1,
    marginRight: 12,
  },
  titleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  h1Title: {
    fontSize: 28,
    fontWeight: '800',
    color: Theme.colors.text,
    letterSpacing: -0.6,
  },
  chevronPill: {
    width: 28,
    height: 28,
    borderRadius: 14,
    backgroundColor: Theme.colors.surface,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    justifyContent: 'center',
  },
  avatarBtn: {
    width: 38,
    height: 38,
    borderRadius: 19,
    backgroundColor: Theme.colors.primary,
    borderWidth: 1.5,
    borderColor: Theme.colors.primaryLight,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
  },
  avatarLetter: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '800',
  },
});
