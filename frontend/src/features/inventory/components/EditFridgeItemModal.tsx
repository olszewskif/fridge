import React, { useState, useEffect } from 'react';
import {
  Modal,
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Platform,
  KeyboardAvoidingView,
  ActivityIndicator,
  Alert,
} from 'react-native';
import { X, Check, Edit3, Calendar, Layers, Plus, Minus } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useUpdateFridgeItem } from '../hooks/useFridgeItems';

interface Props {
  visible: boolean;
  onClose: () => void;
  fridgeId: string;
  itemId: string;
  currentQuantity: number;
  currentExpiryDate?: string | null;
  productName?: string;
  packageSize?: string | null;
  onSuccess?: (newQuantity: number, newExpiryDate?: string | null) => void;
}

export function EditFridgeItemModal({
  visible,
  onClose,
  fridgeId,
  itemId,
  currentQuantity,
  currentExpiryDate,
  productName,
  packageSize,
  onSuccess,
}: Props) {
  const updateItemMutation = useUpdateFridgeItem();

  const [quantity, setQuantity] = useState(currentQuantity || 1);
  const [expiryDate, setExpiryDate] = useState<string>(currentExpiryDate || '');

  useEffect(() => {
    if (visible) {
      setQuantity(currentQuantity || 1);
      setExpiryDate(currentExpiryDate || '');
    }
  }, [visible, currentQuantity, currentExpiryDate]);

  const handleSetQuickExpiry = (daysToAdd: number) => {
    const target = new Date();
    target.setDate(target.getDate() + daysToAdd);
    const isoDate = target.toISOString().split('T')[0];
    setExpiryDate(isoDate);
  };

  const handleSave = async () => {
    if (quantity < 1) {
      Alert.alert('Invalid Quantity', 'Package quantity must be at least 1.');
      return;
    }

    try {
      await updateItemMutation.mutateAsync({
        fridgeId,
        itemId,
        data: {
          quantity,
          expiryDate: expiryDate.trim() ? expiryDate.trim() : null,
        },
      });
      if (onSuccess) {
        onSuccess(quantity, expiryDate.trim() ? expiryDate.trim() : null);
      }
      onClose();
    } catch (err: any) {
      Alert.alert(
        'Update Failed',
        err?.response?.data?.detail || err?.message || 'Could not update fridge item.'
      );
    }
  };

  return (
    <Modal visible={visible} transparent animationType="slide" onRequestClose={onClose}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        style={styles.overlay}
      >
        <TouchableOpacity style={styles.backdrop} activeOpacity={1} onPress={onClose} />

        <View style={styles.container}>
          {/* Header */}
          <View style={styles.header}>
            <View style={styles.titleRow}>
              <Edit3 size={20} color={Theme.colors.primary} />
              <Text style={styles.headerTitle}>Edit Fridge Stock</Text>
            </View>
            <TouchableOpacity onPress={onClose} hitSlop={10} style={styles.closeBtn}>
              <X size={20} color={Theme.colors.textSecondary} />
            </TouchableOpacity>
          </View>

          {productName && (
            <View style={styles.productBanner}>
              <Text style={styles.productBannerName} numberOfLines={1}>
                {productName}
              </Text>
              {packageSize && (
                <Text style={styles.productBannerMeta}>Package size: {packageSize}</Text>
              )}
            </View>
          )}

          <ScrollView
            showsVerticalScrollIndicator={false}
            contentContainerStyle={styles.scrollContent}
          >
            {/* Quantity Stepper */}
            <View style={styles.section}>
              <View style={styles.sectionHeader}>
                <Layers size={16} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>NUMBER OF PACKAGES (IN FRIDGE)</Text>
              </View>

              <View style={styles.stepperRow}>
                <TouchableOpacity
                  style={[styles.stepperBtn, quantity <= 1 && styles.stepperBtnDisabled]}
                  onPress={() => setQuantity((q) => Math.max(1, q - 1))}
                  disabled={quantity <= 1}
                >
                  <Minus size={20} color={quantity <= 1 ? Theme.colors.textMuted : Theme.colors.text} />
                </TouchableOpacity>

                <View style={styles.stepperDisplay}>
                  <Text style={styles.stepperValue}>{quantity}</Text>
                  <Text style={styles.stepperUnit}>{quantity === 1 ? 'package' : 'packages'}</Text>
                </View>

                <TouchableOpacity
                  style={styles.stepperBtn}
                  onPress={() => setQuantity((q) => q + 1)}
                >
                  <Plus size={20} color={Theme.colors.text} />
                </TouchableOpacity>
              </View>
            </View>

            {/* Expiry Date */}
            <View style={styles.section}>
              <View style={styles.sectionHeader}>
                <Calendar size={16} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>EXPIRY DATE (YYYY-MM-DD)</Text>
              </View>

              {/* Quick Presets */}
              <View style={styles.quickDatesRow}>
                <TouchableOpacity
                  style={styles.quickDateChip}
                  onPress={() => handleSetQuickExpiry(3)}
                >
                  <Text style={styles.quickDateText}>+3 days</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={styles.quickDateChip}
                  onPress={() => handleSetQuickExpiry(7)}
                >
                  <Text style={styles.quickDateText}>+1 week</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={styles.quickDateChip}
                  onPress={() => handleSetQuickExpiry(14)}
                >
                  <Text style={styles.quickDateText}>+2 weeks</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={styles.quickDateChip}
                  onPress={() => handleSetQuickExpiry(30)}
                >
                  <Text style={styles.quickDateText}>+1 month</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[styles.quickDateChip, !expiryDate && styles.quickDateChipActive]}
                  onPress={() => setExpiryDate('')}
                >
                  <Text style={[styles.quickDateText, !expiryDate && styles.quickDateTextActive]}>
                    No Expiry
                  </Text>
                </TouchableOpacity>
              </View>

              <View style={styles.inputWrap}>
                <TextInput
                  style={styles.textInput}
                  placeholder="YYYY-MM-DD"
                  placeholderTextColor={Theme.colors.textMuted}
                  value={expiryDate}
                  onChangeText={setExpiryDate}
                  maxLength={10}
                />
                {expiryDate.length > 0 && (
                  <TouchableOpacity onPress={() => setExpiryDate('')} hitSlop={8}>
                    <X size={16} color={Theme.colors.textMuted} />
                  </TouchableOpacity>
                )}
              </View>
            </View>
          </ScrollView>

          {/* Action Buttons */}
          <View style={styles.actionsRow}>
            <TouchableOpacity
              style={styles.cancelBtn}
              onPress={onClose}
              disabled={updateItemMutation.isPending}
            >
              <Text style={styles.cancelBtnText}>Cancel</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.saveBtn}
              onPress={handleSave}
              disabled={updateItemMutation.isPending}
            >
              {updateItemMutation.isPending ? (
                <ActivityIndicator color="#fff" size="small" />
              ) : (
                <>
                  <Check size={18} color="#fff" />
                  <Text style={styles.saveBtnText}>Update Item</Text>
                </>
              )}
            </TouchableOpacity>
          </View>
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
  container: {
    backgroundColor: Theme.colors.surface,
    borderTopLeftRadius: Theme.radius.xl,
    borderTopRightRadius: Theme.radius.xl,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    paddingTop: Theme.spacing.lg,
    paddingBottom: Platform.OS === 'ios' ? Theme.spacing.xl : Theme.spacing.lg,
    paddingHorizontal: Theme.spacing.lg,
    maxHeight: '80%',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: Theme.spacing.sm,
  },
  titleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  closeBtn: {
    padding: Theme.spacing.xs,
  },
  productBanner: {
    backgroundColor: Theme.colors.surfaceElevated,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    padding: Theme.spacing.md,
    marginBottom: Theme.spacing.md,
    gap: 2,
  },
  productBannerName: {
    fontSize: 15,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  productBannerMeta: {
    fontSize: 12,
    color: Theme.colors.textSecondary,
  },
  scrollContent: {
    gap: Theme.spacing.lg,
    paddingBottom: Theme.spacing.md,
  },
  section: {
    gap: Theme.spacing.sm,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  sectionTitle: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.textSecondary,
    letterSpacing: 0.8,
  },
  stepperRow: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surfaceElevated,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    padding: 6,
  },
  stepperBtn: {
    width: 48,
    height: 48,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  stepperBtnDisabled: {
    opacity: 0.4,
  },
  stepperDisplay: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  stepperValue: {
    fontSize: 22,
    fontWeight: '800',
    color: Theme.colors.text,
  },
  stepperUnit: {
    fontSize: 12,
    color: Theme.colors.textSecondary,
    fontWeight: '500',
  },
  quickDatesRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 6,
  },
  quickDateChip: {
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: Theme.radius.full,
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  quickDateChipActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  quickDateText: {
    fontSize: 12,
    color: Theme.colors.textSecondary,
    fontWeight: '600',
  },
  quickDateTextActive: {
    color: '#fff',
    fontWeight: '700',
  },
  inputWrap: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surfaceElevated,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    paddingHorizontal: Theme.spacing.md,
    paddingVertical: 10,
    gap: Theme.spacing.sm,
  },
  textInput: {
    flex: 1,
    color: Theme.colors.text,
    fontSize: 14,
    fontFamily: 'monospace',
  },
  actionsRow: {
    flexDirection: 'row',
    gap: Theme.spacing.md,
    marginTop: Theme.spacing.sm,
  },
  cancelBtn: {
    flex: 1,
    paddingVertical: 14,
    borderRadius: Theme.radius.md,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  cancelBtnText: {
    color: Theme.colors.textSecondary,
    fontSize: 15,
    fontWeight: '600',
  },
  saveBtn: {
    flex: 2,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    backgroundColor: Theme.colors.primary,
    paddingVertical: 14,
    borderRadius: Theme.radius.md,
  },
  saveBtnText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
});
