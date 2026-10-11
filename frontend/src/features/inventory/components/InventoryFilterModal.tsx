import React from 'react';
import {
  Modal,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  Platform,
  KeyboardAvoidingView,
  TextInput,
} from 'react-native';
import { X, Check, RotateCcw, Tag, Calendar, User, SlidersHorizontal } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useCategories } from '../../catalog/hooks/useCategories';
import { ExpiryFilterType } from './InventoryFilters';

interface Props {
  visible: boolean;
  onClose: () => void;
  expiryFilter: ExpiryFilterType;
  onExpiryFilterChange: (type: ExpiryFilterType) => void;
  selectedCategoryIds: string[];
  onCategoryToggle: (categoryId: string) => void;
  addedBy: string;
  onAddedByChange: (userId: string) => void;
  availableUsers?: string[];
  selectedProductId?: string;
  selectedProductName?: string;
  onClearProductFilter?: () => void;
  onResetAll: () => void;
}

export function InventoryFilterModal({
  visible,
  onClose,
  expiryFilter,
  onExpiryFilterChange,
  selectedCategoryIds,
  onCategoryToggle,
  addedBy,
  onAddedByChange,
  availableUsers = [],
  selectedProductId,
  selectedProductName,
  onClearProductFilter,
  onResetAll,
}: Props) {
  const { data: categories = [] } = useCategories();

  const activeFiltersCount =
    (expiryFilter !== 'ALL' ? 1 : 0) +
    selectedCategoryIds.length +
    (addedBy.trim() ? 1 : 0) +
    (selectedProductId ? 1 : 0);

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
              <SlidersHorizontal size={20} color={Theme.colors.primary} />
              <Text style={styles.headerTitle}>Filter Inventory</Text>
              {activeFiltersCount > 0 && (
                <View style={styles.countBadge}>
                  <Text style={styles.countBadgeText}>{activeFiltersCount}</Text>
                </View>
              )}
            </View>

            <View style={styles.headerActions}>
              {activeFiltersCount > 0 && (
                <TouchableOpacity
                  onPress={onResetAll}
                  style={styles.resetBtn}
                  hitSlop={8}
                >
                  <RotateCcw size={14} color={Theme.colors.textSecondary} />
                  <Text style={styles.resetBtnText}>Reset</Text>
                </TouchableOpacity>
              )}
              <TouchableOpacity onPress={onClose} hitSlop={10} style={styles.closeBtn}>
                <X size={20} color={Theme.colors.textSecondary} />
              </TouchableOpacity>
            </View>
          </View>

          <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scroll}>
            {/* Specific Product Lock (if active) */}
            {selectedProductId && (
              <View style={styles.section}>
                <Text style={styles.sectionTitle}>FILTERED BY PRODUCT</Text>
                <View style={styles.activeProductPill}>
                  <Text style={styles.activeProductText} numberOfLines={1}>
                    {selectedProductName || `Product: ${selectedProductId.slice(0, 8)}`}
                  </Text>
                  {onClearProductFilter && (
                    <TouchableOpacity onPress={onClearProductFilter} hitSlop={8}>
                      <X size={16} color={Theme.colors.text} />
                    </TouchableOpacity>
                  )}
                </View>
              </View>
            )}

            {/* Expiry Status */}
            <View style={styles.section}>
              <View style={styles.sectionHeader}>
                <Calendar size={16} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>EXPIRY STATUS</Text>
              </View>

              <View style={styles.pillsRow}>
                <TouchableOpacity
                  style={[styles.pill, expiryFilter === 'ALL' && styles.pillActive]}
                  onPress={() => onExpiryFilterChange('ALL')}
                >
                  <Text style={[styles.pillText, expiryFilter === 'ALL' && styles.pillTextActive]}>
                    All Items
                  </Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[
                    styles.pill,
                    expiryFilter === 'EXPIRING_SOON' && styles.pillActiveExpiring,
                  ]}
                  onPress={() => onExpiryFilterChange('EXPIRING_SOON')}
                >
                  <Text
                    style={[
                      styles.pillText,
                      expiryFilter === 'EXPIRING_SOON' && styles.pillTextExpiring,
                    ]}
                  >
                    Expiring Soon (≤3d)
                  </Text>
                </TouchableOpacity>

                <TouchableOpacity
                  style={[
                    styles.pill,
                    expiryFilter === 'EXPIRED' && styles.pillActiveExpired,
                  ]}
                  onPress={() => onExpiryFilterChange('EXPIRED')}
                >
                  <Text
                    style={[
                      styles.pillText,
                      expiryFilter === 'EXPIRED' && styles.pillTextExpired,
                    ]}
                  >
                    Expired
                  </Text>
                </TouchableOpacity>
              </View>
            </View>

            {/* Categories */}
            <View style={styles.section}>
              <View style={styles.sectionHeader}>
                <Tag size={16} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>CATEGORIES</Text>
              </View>

              {categories.length > 0 ? (
                <View style={styles.categoriesWrap}>
                  {categories.map((cat) => {
                    const isSelected = selectedCategoryIds.includes(cat.id);
                    return (
                      <TouchableOpacity
                        key={cat.id}
                        style={[styles.categoryChip, isSelected && styles.categoryChipActive]}
                        onPress={() => onCategoryToggle(cat.id)}
                        activeOpacity={0.7}
                      >
                        {isSelected && <Check size={14} color="#fff" style={{ marginRight: 4 }} />}
                        <Text
                          style={[
                            styles.categoryChipText,
                            isSelected && styles.categoryChipTextActive,
                          ]}
                        >
                          {cat.name}
                        </Text>
                      </TouchableOpacity>
                    );
                  })}
                </View>
              ) : (
                <Text style={styles.emptyNote}>No categories available</Text>
              )}
            </View>

            {/* Added By (Fridge Members) */}
            <View style={styles.section}>
              <View style={styles.sectionHeader}>
                <User size={16} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>ADDED BY (FRIDGE MEMBERS)</Text>
              </View>

              <View style={styles.pillsRow}>
                <TouchableOpacity
                  style={[styles.pill, !addedBy && styles.pillActive]}
                  onPress={() => onAddedByChange('')}
                >
                  <Text style={[styles.pillText, !addedBy && styles.pillTextActive]}>
                    All Members
                  </Text>
                </TouchableOpacity>

                {availableUsers.map((userId) => {
                  const isSelected = addedBy === userId;
                  return (
                    <TouchableOpacity
                      key={userId}
                      style={[styles.pill, isSelected && styles.pillActive]}
                      onPress={() => onAddedByChange(isSelected ? '' : userId)}
                    >
                      <Text style={[styles.pillText, isSelected && styles.pillTextActive]}>
                        Member {userId.slice(0, 6)}
                      </Text>
                    </TouchableOpacity>
                  );
                })}
              </View>

              {availableUsers.length === 0 && (
                <Text style={styles.emptyNote}>No member activity recorded in this fridge yet</Text>
              )}
            </View>
          </ScrollView>

          {/* Apply Button */}
          <TouchableOpacity style={styles.applyBtn} onPress={onClose} activeOpacity={0.8}>
            <Text style={styles.applyBtnText}>Show Results</Text>
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
    maxHeight: '80%',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: Theme.spacing.md,
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
  countBadge: {
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
  },
  countBadgeText: {
    color: '#fff',
    fontSize: 11,
    fontWeight: '800',
  },
  headerActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  resetBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  resetBtnText: {
    fontSize: 13,
    color: Theme.colors.textSecondary,
    fontWeight: '600',
  },
  closeBtn: {
    padding: Theme.spacing.xs,
  },
  scroll: {
    paddingBottom: Theme.spacing.md,
    gap: Theme.spacing.lg,
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
  activeProductPill: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    borderWidth: 1,
    borderColor: Theme.colors.primary,
    paddingHorizontal: 12,
    paddingVertical: 10,
    borderRadius: Theme.radius.md,
  },
  activeProductText: {
    color: Theme.colors.primaryLight,
    fontWeight: '700',
    fontSize: 14,
    flex: 1,
  },
  pillsRow: {
    flexDirection: 'row',
    gap: Theme.spacing.xs,
    flexWrap: 'wrap',
  },
  pill: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: Theme.radius.full,
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  pillActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  pillActiveExpiring: {
    backgroundColor: Theme.colors.expiringSoonBg,
    borderColor: Theme.colors.expiringSoon,
  },
  pillActiveExpired: {
    backgroundColor: Theme.colors.expiredBg,
    borderColor: Theme.colors.expired,
  },
  pillText: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  pillTextActive: {
    color: '#fff',
    fontWeight: '700',
  },
  pillTextExpiring: {
    color: Theme.colors.expiringSoon,
    fontWeight: '700',
  },
  pillTextExpired: {
    color: Theme.colors.expired,
    fontWeight: '700',
  },
  categoriesWrap: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  categoryChip: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  categoryChipActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  categoryChipText: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  categoryChipTextActive: {
    color: '#fff',
    fontWeight: '700',
  },
  emptyNote: {
    fontSize: 13,
    color: Theme.colors.textMuted,
    fontStyle: 'italic',
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
  },
  applyBtn: {
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
  applyBtnText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '700',
  },
});
