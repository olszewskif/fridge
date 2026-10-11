import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { ChevronDown, Refrigerator } from 'lucide-react-native';
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
        <View>
          <Text style={styles.eyebrow}>CURRENT FRIDGE</Text>
          <TouchableOpacity
            style={styles.selectorBtn}
            onPress={() => setModalVisible(true)}
            activeOpacity={0.7}
          >
            <Refrigerator size={20} color={Theme.colors.primary} />
            <Text style={styles.fridgeName} numberOfLines={1}>
              {activeFridge ? activeFridge.name : 'Select a Fridge'}
            </Text>
            <View style={styles.chevronWrap}>
              <ChevronDown size={16} color={Theme.colors.textSecondary} />
            </View>
          </TouchableOpacity>
        </View>
      </View>

      <FridgeSwitcherModal visible={modalVisible} onClose={() => setModalVisible(false)} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.sm,
  },
  eyebrow: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.primary,
    letterSpacing: 1,
    marginBottom: 4,
  },
  selectorBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    alignSelf: 'flex-start',
    backgroundColor: Theme.colors.surface,
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: Theme.radius.full,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  fridgeName: {
    fontSize: 17,
    fontWeight: '700',
    color: Theme.colors.text,
    maxWidth: 200,
  },
  chevronWrap: {
    marginLeft: 2,
  },
});
