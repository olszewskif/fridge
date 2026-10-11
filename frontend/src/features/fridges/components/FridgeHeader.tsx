import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { ChevronDown } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useFridgeStore } from '../../../store/useFridgeStore';
import { FridgeSwitcherModal } from './FridgeSwitcherModal';

export function FridgeHeader() {
  const insets = useSafeAreaInsets();
  const { activeFridge } = useFridgeStore();
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <>
      <View style={[styles.container, { paddingTop: Math.max(insets.top, 16) + 8 }]}>
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
      </View>

      <FridgeSwitcherModal visible={modalVisible} onClose={() => setModalVisible(false)} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.md,
  },
  headerBtn: {
    alignSelf: 'flex-start',
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
});
