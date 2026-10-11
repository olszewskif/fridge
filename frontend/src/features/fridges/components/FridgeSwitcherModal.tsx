import React, { useState } from 'react';
import {
  Modal,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  FlatList,
  TextInput,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { Check, Plus, X, Refrigerator } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useFridgeStore } from '../../../store/useFridgeStore';
import { useFridges, useCreateFridge } from '../hooks/useFridges';
import { Fridge } from '../types/fridge.types';

interface Props {
  visible: boolean;
  onClose: () => void;
}

export function FridgeSwitcherModal({ visible, onClose }: Props) {
  const { activeFridge, setActiveFridge } = useFridgeStore();
  const { data: fridgesPage, isLoading } = useFridges();
  const createFridgeMutation = useCreateFridge();

  const [isCreating, setIsCreating] = useState(false);
  const [newFridgeName, setNewFridgeName] = useState('');

  const fridges = fridgesPage?.content || [];

  const handleSelect = (fridge: Fridge) => {
    setActiveFridge(fridge);
    onClose();
  };

  const handleCreate = async () => {
    if (!newFridgeName.trim()) return;
    try {
      await createFridgeMutation.mutateAsync({ name: newFridgeName.trim() });
      setNewFridgeName('');
      setIsCreating(false);
      onClose();
    } catch (e) {
      // Error handled by query/Axios interceptor
    }
  };

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        style={styles.overlay}
      >
        <TouchableOpacity style={styles.backdrop} activeOpacity={1} onPress={onClose} />

        <View style={styles.sheetContainer}>
          {/* Header */}
          <View style={styles.header}>
            <View style={styles.headerTitleRow}>
              <Refrigerator size={22} color={Theme.colors.primary} />
              <Text style={styles.title}>Your Fridges</Text>
            </View>
            <TouchableOpacity onPress={onClose} hitSlop={12} style={styles.closeBtn}>
              <X size={20} color={Theme.colors.textSecondary} />
            </TouchableOpacity>
          </View>

          {/* List of fridges */}
          {isLoading ? (
            <ActivityIndicator color={Theme.colors.primary} style={{ marginVertical: 32 }} />
          ) : (
            <FlatList
              data={fridges}
              keyExtractor={(item) => item.id}
              contentContainerStyle={styles.listContent}
              renderItem={({ item }) => {
                const isSelected = activeFridge?.id === item.id;
                return (
                  <TouchableOpacity
                    style={[styles.fridgeRow, isSelected && styles.fridgeRowSelected]}
                    onPress={() => handleSelect(item)}
                    activeOpacity={0.7}
                  >
                    <View style={styles.fridgeInfo}>
                      <Text style={[styles.fridgeName, isSelected && styles.fridgeNameSelected]}>
                        {item.name}
                      </Text>
                    </View>
                    {isSelected && (
                      <View style={styles.checkBadge}>
                        <Check size={16} color="#fff" strokeWidth={3} />
                      </View>
                    )}
                  </TouchableOpacity>
                );
              }}
              ListEmptyComponent={
                <Text style={styles.emptyText}>No fridges found. Create your first one!</Text>
              }
            />
          )}

          {/* Create new fridge form or toggle */}
          {isCreating ? (
            <View style={styles.createBox}>
              <TextInput
                style={styles.input}
                placeholder="Fridge name (e.g. Kitchen Fridge)"
                placeholderTextColor={Theme.colors.textMuted}
                value={newFridgeName}
                onChangeText={setNewFridgeName}
                autoFocus
              />
              <View style={styles.createActions}>
                <TouchableOpacity
                  style={styles.cancelBtn}
                  onPress={() => {
                    setIsCreating(false);
                    setNewFridgeName('');
                  }}
                >
                  <Text style={styles.cancelBtnText}>Cancel</Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[styles.saveBtn, !newFridgeName.trim() && styles.saveBtnDisabled]}
                  onPress={handleCreate}
                  disabled={!newFridgeName.trim() || createFridgeMutation.isPending}
                >
                  {createFridgeMutation.isPending ? (
                    <ActivityIndicator size="small" color="#fff" />
                  ) : (
                    <Text style={styles.saveBtnText}>Save Fridge</Text>
                  )}
                </TouchableOpacity>
              </View>
            </View>
          ) : (
            <TouchableOpacity
              style={styles.addTrigger}
              onPress={() => setIsCreating(true)}
              activeOpacity={0.8}
            >
              <Plus size={18} color={Theme.colors.primary} />
              <Text style={styles.addTriggerText}>Add another fridge</Text>
            </TouchableOpacity>
          )}
        </View>
      </KeyboardAvoidingView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    justifyContent: 'flex-end',
    backgroundColor: Theme.colors.overlay,
  },
  backdrop: {
    ...StyleSheet.absoluteFill,
  },
  sheetContainer: {
    backgroundColor: Theme.colors.surface,
    borderTopLeftRadius: Theme.radius.xl,
    borderTopRightRadius: Theme.radius.xl,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    paddingTop: Theme.spacing.lg,
    paddingBottom: Platform.OS === 'ios' ? Theme.spacing.xl : Theme.spacing.lg,
    paddingHorizontal: Theme.spacing.lg,
    maxHeight: '75%',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: Theme.spacing.md,
  },
  headerTitleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
  },
  title: {
    fontSize: 20,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  closeBtn: {
    padding: Theme.spacing.xs,
  },
  listContent: {
    paddingVertical: Theme.spacing.xs,
    gap: Theme.spacing.sm,
  },
  fridgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  fridgeRowSelected: {
    borderColor: Theme.colors.primary,
    backgroundColor: 'rgba(64, 0, 255, 0.1)',
  },
  fridgeInfo: {
    flex: 1,
  },
  fridgeName: {
    fontSize: 16,
    fontWeight: '600',
    color: Theme.colors.text,
  },
  fridgeNameSelected: {
    color: '#fff',
    fontWeight: '700',
  },
  checkBadge: {
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: Theme.colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emptyText: {
    color: Theme.colors.textMuted,
    textAlign: 'center',
    marginVertical: Theme.spacing.lg,
    fontSize: 14,
  },
  addTrigger: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: Theme.spacing.sm,
    paddingVertical: Theme.spacing.md,
    marginTop: Theme.spacing.sm,
    borderWidth: 1,
    borderStyle: 'dashed',
    borderColor: Theme.colors.surfaceBorder,
    borderRadius: Theme.radius.md,
  },
  addTriggerText: {
    color: Theme.colors.text,
    fontSize: 15,
    fontWeight: '600',
  },
  createBox: {
    marginTop: Theme.spacing.md,
    gap: Theme.spacing.sm,
  },
  input: {
    backgroundColor: Theme.colors.surfaceElevated,
    color: Theme.colors.text,
    paddingHorizontal: Theme.spacing.md,
    paddingVertical: 12,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    fontSize: 15,
  },
  createActions: {
    flexDirection: 'row',
    gap: Theme.spacing.sm,
  },
  cancelBtn: {
    flex: 1,
    paddingVertical: 12,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surfaceElevated,
  },
  cancelBtnText: {
    color: Theme.colors.textSecondary,
    fontWeight: '600',
  },
  saveBtn: {
    flex: 2,
    paddingVertical: 12,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.primary,
  },
  saveBtnDisabled: {
    opacity: 0.5,
  },
  saveBtnText: {
    color: '#fff',
    fontWeight: '700',
  },
});
