import React, { useState } from 'react';
import {
  Modal,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Image,
  ActivityIndicator,
  ScrollView,
  Platform,
  KeyboardAvoidingView,
} from 'react-native';
import { X, Plus, Minus, Calendar, Check, Package, Scale, Tag } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { ProductResponse } from '../../catalog/types/catalog.types';
import { useFridgeStore } from '../../../store/useFridgeStore';
import { useAddFridgeItem } from '../../inventory/hooks/useFridgeItems';
import { formatNetQuantity } from '../../../utils/format';

interface Props {
  visible: boolean;
  product: ProductResponse | null;
  onClose: () => void;
  onSuccess: () => void;
}

export function ConfirmAddItemSheet({ visible, product, onClose, onSuccess }: Props) {
  const { activeFridge } = useFridgeStore();
  const addItemMutation = useAddFridgeItem();

  const [quantity, setQuantity] = useState('1');

  const handleStep = (delta: number) => {
    const current = parseInt(quantity, 10) || 1;
    const next = Math.max(1, current + delta);
    setQuantity(String(next));
  };

  // Format today + offset to YYYY-MM-DD
  const formatOffsetDate = (days: number) => {
    const d = new Date();
    d.setDate(d.getDate() + days);
    return d.toISOString().split('T')[0];
  };

  const [expiryDate, setExpiryDate] = useState(formatOffsetDate(7)); // default +7 days

  if (!product) return null;

  const packageSizeStr = formatNetQuantity(product.netQuantityDto);

  const handleAdd = async () => {
    if (!activeFridge) return;
    const qtyNumber = parseInt(quantity, 10);
    if (!qtyNumber || qtyNumber <= 0) {
      alert('Please enter a valid quantity of at least 1');
      return;
    }

    try {
      await addItemMutation.mutateAsync({
        fridgeId: activeFridge.id,
        data: {
          productId: product.id,
          quantity: qtyNumber,
          expiryDate: expiryDate ? expiryDate : null,
        },
      });
      onSuccess();
    } catch (e) {
      // Error handled by interceptor
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
            <Text style={styles.headerTitle}>Add Scanned Item</Text>
            <TouchableOpacity onPress={onClose} hitSlop={10} style={styles.closeBtn}>
              <X size={20} color={Theme.colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scroll}>
            {/* Scanned Product Card */}
            <View style={styles.productCard}>
              {product.imageUrl ? (
                <Image source={{ uri: product.imageUrl }} style={styles.productImg} />
              ) : (
                <View style={styles.placeholderImg}>
                  <Package size={28} color={Theme.colors.primary} />
                </View>
              )}
              <View style={styles.productDetails}>
                <Text style={styles.productName} numberOfLines={2}>
                  {product.name}
                </Text>
                <View style={styles.metaRow}>
                  {product.brand && <Text style={styles.brandName}>{product.brand}</Text>}
                  {packageSizeStr ? (
                    <View style={styles.netWeightBadge}>
                      <Scale size={12} color={Theme.colors.primaryLight} />
                      <Text style={styles.netWeightText}>{packageSizeStr}</Text>
                    </View>
                  ) : null}
                </View>
                {product.categories && product.categories.length > 0 && (
                  <View style={styles.categoriesRow}>
                    {product.categories.map((cat) => (
                      <View key={cat.id} style={styles.categoryBadge}>
                        <Tag size={10} color={Theme.colors.primaryLight} />
                        <Text style={styles.categoryBadgeText}>{cat.name}</Text>
                      </View>
                    ))}
                  </View>
                )}
                <Text style={styles.barcodeText}>Barcode: {product.barcode}</Text>
              </View>
            </View>

            {/* Target Fridge Note */}
            <View style={styles.targetFridgeBanner}>
              <Text style={styles.targetFridgeText}>
                Adding to: <Text style={styles.boldFridgeName}>{activeFridge?.name}</Text>
              </Text>
            </View>

            {/* Quantity Selector */}
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>NUMBER OF PACKAGES</Text>
              <View style={styles.qtyRow}>
                <View style={styles.counterBox}>
                  <TouchableOpacity
                    style={styles.counterBtn}
                    onPress={() => handleStep(-1)}
                  >
                    <Minus size={18} color={Theme.colors.text} />
                  </TouchableOpacity>
                  <TextInput
                    style={styles.qtyInput}
                    value={quantity}
                    onChangeText={setQuantity}
                    keyboardType="numeric"
                    selectTextOnFocus
                    textAlign="center"
                  />
                  <TouchableOpacity
                    style={styles.counterBtn}
                    onPress={() => handleStep(1)}
                  >
                    <Plus size={18} color={Theme.colors.text} />
                  </TouchableOpacity>
                </View>
                <View style={styles.unitPillStatic}>
                  <Text style={styles.unitPillStaticText}>
                    {parseInt(quantity, 10) === 1 ? 'Package' : 'Packages'}
                  </Text>
                </View>
              </View>
            </View>

            {/* Expiry Date Presets */}
            <View style={styles.section}>
              <Text style={styles.sectionTitle}>EXPIRATION DATE</Text>
              <View style={styles.expiryPresetsRow}>
                {[
                  { label: '+3 Days', days: 3 },
                  { label: '+1 Week', days: 7 },
                  { label: '+2 Weeks', days: 14 },
                  { label: '+1 Month', days: 30 },
                ].map((preset) => {
                  const targetDate = formatOffsetDate(preset.days);
                  const isSelected = expiryDate === targetDate;
                  return (
                    <TouchableOpacity
                      key={preset.label}
                      style={[styles.presetBtn, isSelected && styles.presetBtnActive]}
                      onPress={() => setExpiryDate(targetDate)}
                    >
                      <Text
                        style={[styles.presetBtnText, isSelected && styles.presetBtnTextActive]}
                      >
                        {preset.label}
                      </Text>
                    </TouchableOpacity>
                  );
                })}
              </View>

              <View style={styles.dateInputWrap}>
                <Calendar size={18} color={Theme.colors.primary} />
                <TextInput
                  style={styles.dateInput}
                  value={expiryDate}
                  onChangeText={setExpiryDate}
                  placeholder="YYYY-MM-DD"
                  placeholderTextColor={Theme.colors.textMuted}
                />
              </View>
            </View>
          </ScrollView>

          {/* Action Button */}
          <TouchableOpacity
            style={[styles.addBtn, addItemMutation.isPending && styles.addBtnDisabled]}
            onPress={handleAdd}
            disabled={addItemMutation.isPending}
            activeOpacity={0.8}
          >
            {addItemMutation.isPending ? (
              <ActivityIndicator color="#fff" />
            ) : (
              <View style={styles.btnContent}>
                <Check size={20} color="#fff" strokeWidth={2.5} />
                <Text style={styles.addBtnText}>Confirm & Add to Fridge</Text>
              </View>
            )}
          </TouchableOpacity>
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
    maxHeight: '85%',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: Theme.spacing.md,
  },
  headerTitle: {
    fontSize: 20,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  closeBtn: {
    padding: Theme.spacing.xs,
  },
  scroll: {
    paddingBottom: Theme.spacing.md,
  },
  productCard: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surfaceElevated,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.md,
  },
  productImg: {
    width: 60,
    height: 60,
    borderRadius: Theme.radius.md,
    backgroundColor: '#fff',
  },
  placeholderImg: {
    width: 60,
    height: 60,
    borderRadius: Theme.radius.md,
    backgroundColor: 'rgba(64, 0, 255, 0.12)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  productDetails: {
    flex: 1,
  },
  productName: {
    fontSize: 16,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  metaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginTop: 3,
    flexWrap: 'wrap',
  },
  brandName: {
    fontSize: 14,
    color: Theme.colors.textSecondary,
    fontWeight: '500',
  },
  netWeightBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
  },
  netWeightText: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.primaryLight,
  },
  categoriesRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 6,
    marginTop: 4,
  },
  categoryBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    backgroundColor: Theme.colors.surface,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: Theme.radius.md,
  },
  categoryBadgeText: {
    fontSize: 11,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  barcodeText: {
    fontSize: 12,
    color: Theme.colors.textMuted,
    marginTop: 3,
  },
  targetFridgeBanner: {
    marginTop: Theme.spacing.sm,
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: Theme.radius.md,
    backgroundColor: 'rgba(64, 0, 255, 0.08)',
    borderWidth: 1,
    borderColor: 'rgba(64, 0, 255, 0.2)',
  },
  targetFridgeText: {
    fontSize: 13,
    color: Theme.colors.textSecondary,
  },
  boldFridgeName: {
    color: '#fff',
    fontWeight: '700',
  },
  section: {
    marginTop: Theme.spacing.lg,
  },
  sectionTitle: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.textSecondary,
    letterSpacing: 0.8,
    marginBottom: Theme.spacing.sm,
  },
  qtyRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.md,
  },
  counterBox: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: Theme.colors.surfaceElevated,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    padding: 6,
  },
  counterBtn: {
    width: 40,
    height: 40,
    borderRadius: Theme.radius.sm,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
  },
  qtyInput: {
    flex: 1,
    fontSize: 18,
    fontWeight: '700',
    color: Theme.colors.text,
    paddingHorizontal: 8,
    paddingVertical: 4,
  },
  unitPillStatic: {
    paddingHorizontal: 16,
    paddingVertical: 14,
    backgroundColor: Theme.colors.surfaceElevated,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    justifyContent: 'center',
  },
  unitPillStaticText: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  expiryPresetsRow: {
    flexDirection: 'row',
    gap: Theme.spacing.xs,
    marginBottom: Theme.spacing.sm,
  },
  presetBtn: {
    flex: 1,
    paddingVertical: 8,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  presetBtnActive: {
    backgroundColor: 'rgba(64, 0, 255, 0.2)',
    borderColor: Theme.colors.primary,
  },
  presetBtnText: {
    fontSize: 12,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  presetBtnTextActive: {
    color: '#fff',
    fontWeight: '700',
  },
  dateInputWrap: {
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
  dateInput: {
    flex: 1,
    color: Theme.colors.text,
    fontSize: 15,
  },
  addBtn: {
    backgroundColor: Theme.colors.primary,
    paddingVertical: 14,
    borderRadius: Theme.radius.md,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: Theme.spacing.md,
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
    elevation: 4,
  },
  addBtnDisabled: {
    opacity: 0.6,
  },
  btnContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
  },
  addBtnText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '700',
  },
});
