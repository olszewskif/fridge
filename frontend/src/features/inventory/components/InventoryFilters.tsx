import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import {
  Search,
  X,
  SlidersHorizontal,
  Package,
  Tag,
  User,
  AlertTriangle,
  Flame,
  LayoutGrid,
  LayoutList,
} from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useCategories } from '../../catalog/hooks/useCategories';
import { InventoryFilterModal } from './InventoryFilterModal';

export type ExpiryFilterType = 'ALL' | 'EXPIRING_SOON' | 'EXPIRED';

interface Props {
  search: string;
  onSearchChange: (text: string) => void;
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
  viewMode?: 'list' | 'grid';
  onViewModeChange?: (mode: 'list' | 'grid') => void;
}

export function InventoryFilters({
  search,
  onSearchChange,
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
  viewMode = 'list',
  onViewModeChange,
}: Props) {
  const [modalVisible, setModalVisible] = useState(false);
  const { data: categories = [] } = useCategories();

  const activeFiltersCount =
    (expiryFilter !== 'ALL' ? 1 : 0) +
    selectedCategoryIds.length +
    (addedBy.trim() ? 1 : 0) +
    (selectedProductId ? 1 : 0);

  return (
    <View style={styles.container}>
      {/* Search Bar + Filter Modal Trigger */}
      <View style={styles.searchRow}>
        <View style={styles.searchBar}>
          <Search size={18} color={Theme.colors.textMuted} />
          <TextInput
            style={styles.input}
            placeholder="Search items by name or brand..."
            placeholderTextColor={Theme.colors.textMuted}
            value={search}
            onChangeText={onSearchChange}
            returnKeyType="search"
          />
          {search.length > 0 && (
            <TouchableOpacity onPress={() => onSearchChange('')} hitSlop={8}>
              <X size={16} color={Theme.colors.textSecondary} />
            </TouchableOpacity>
          )}
        </View>

        {onViewModeChange && (
          <TouchableOpacity
            style={styles.viewToggleBtn}
            onPress={() => onViewModeChange(viewMode === 'list' ? 'grid' : 'list')}
            activeOpacity={0.7}
          >
            {viewMode === 'list' ? (
              <LayoutGrid size={18} color={Theme.colors.textSecondary} />
            ) : (
              <LayoutList size={18} color={Theme.colors.textSecondary} />
            )}
          </TouchableOpacity>
        )}

        <TouchableOpacity
          style={[styles.filterTriggerBtn, activeFiltersCount > 0 && styles.filterTriggerBtnActive]}
          onPress={() => setModalVisible(true)}
          activeOpacity={0.7}
        >
          <SlidersHorizontal
            size={18}
            color={activeFiltersCount > 0 ? '#fff' : Theme.colors.textSecondary}
          />
          {activeFiltersCount > 0 && (
            <View style={styles.activeBadge}>
              <Text style={styles.activeBadgeText}>{activeFiltersCount}</Text>
            </View>
          )}
        </TouchableOpacity>
      </View>

      {/* Horizontal Category Chips Bar */}
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.chipsRow}
      >
        <TouchableOpacity
          style={[
            styles.categoryChip,
            selectedCategoryIds.length === 0 && styles.categoryChipActive,
          ]}
          onPress={() => {
            if (selectedCategoryIds.length > 0) {
              // Clear category filters
              selectedCategoryIds.forEach((id) => onCategoryToggle(id));
            }
          }}
          activeOpacity={0.7}
        >
          <Text
            style={[
              styles.categoryChipText,
              selectedCategoryIds.length === 0 && styles.categoryChipTextActive,
            ]}
          >
            All Categories
          </Text>
        </TouchableOpacity>

        {categories.map((cat) => {
          const isSelected = selectedCategoryIds.includes(cat.id);
          return (
            <TouchableOpacity
              key={cat.id}
              style={[styles.categoryChip, isSelected && styles.categoryChipActive]}
              onPress={() => onCategoryToggle(cat.id)}
              activeOpacity={0.7}
            >
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
      </ScrollView>

      {/* Active Filter Pills Bar (if filters are active) */}
      {activeFiltersCount > 0 && (
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.activePillsRow}
        >
          {/* Active Product */}
          {selectedProductId && (
            <View style={styles.activePill}>
              <Package size={12} color={Theme.colors.primaryLight} />
              <Text style={styles.activePillText} numberOfLines={1}>
                {selectedProductName || selectedProductId.slice(0, 8)}
              </Text>
              {onClearProductFilter && (
                <TouchableOpacity onPress={onClearProductFilter} hitSlop={6}>
                  <X size={12} color={Theme.colors.primaryLight} />
                </TouchableOpacity>
              )}
            </View>
          )}

          {/* Active Expiry */}
          {expiryFilter === 'EXPIRING_SOON' && (
            <View style={[styles.activePill, styles.activePillExpiring]}>
              <AlertTriangle size={12} color={Theme.colors.expiringSoon} />
              <Text style={[styles.activePillText, { color: Theme.colors.expiringSoon }]}>
                Expiring Soon
              </Text>
              <TouchableOpacity onPress={() => onExpiryFilterChange('ALL')} hitSlop={6}>
                <X size={12} color={Theme.colors.expiringSoon} />
              </TouchableOpacity>
            </View>
          )}

          {expiryFilter === 'EXPIRED' && (
            <View style={[styles.activePill, styles.activePillExpired]}>
              <Flame size={12} color={Theme.colors.expired} />
              <Text style={[styles.activePillText, { color: Theme.colors.expired }]}>
                Expired
              </Text>
              <TouchableOpacity onPress={() => onExpiryFilterChange('ALL')} hitSlop={6}>
                <X size={12} color={Theme.colors.expired} />
              </TouchableOpacity>
            </View>
          )}

          {/* Active User */}
          {addedBy.trim().length > 0 && (
            <View style={styles.activePill}>
              <User size={12} color={Theme.colors.primaryLight} />
              <Text style={styles.activePillText} numberOfLines={1}>
                User: {addedBy.slice(0, 8)}
              </Text>
              <TouchableOpacity onPress={() => onAddedByChange('')} hitSlop={6}>
                <X size={12} color={Theme.colors.primaryLight} />
              </TouchableOpacity>
            </View>
          )}

          {/* Clear All Link */}
          <TouchableOpacity onPress={onResetAll} style={styles.clearAllLink}>
            <Text style={styles.clearAllLinkText}>Clear all</Text>
          </TouchableOpacity>
        </ScrollView>
      )}

      {/* Filter Bottom Sheet Modal */}
      <InventoryFilterModal
        visible={modalVisible}
        onClose={() => setModalVisible(false)}
        expiryFilter={expiryFilter}
        onExpiryFilterChange={onExpiryFilterChange}
        selectedCategoryIds={selectedCategoryIds}
        onCategoryToggle={onCategoryToggle}
        addedBy={addedBy}
        onAddedByChange={onAddedByChange}
        availableUsers={availableUsers}
        selectedProductId={selectedProductId}
        selectedProductName={selectedProductName}
        onClearProductFilter={onClearProductFilter}
        onResetAll={onResetAll}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.xs,
    gap: Theme.spacing.sm,
  },
  searchRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
  },
  searchBar: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surface,
    paddingHorizontal: Theme.spacing.md,
    paddingVertical: 10,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.sm,
  },
  input: {
    flex: 1,
    color: Theme.colors.text,
    fontSize: 15,
  },
  viewToggleBtn: {
    width: 44,
    height: 44,
    borderRadius: Theme.radius.lg,
    backgroundColor: Theme.colors.surface,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    justifyContent: 'center',
  },
  filterTriggerBtn: {
    width: 44,
    height: 44,
    borderRadius: Theme.radius.lg,
    backgroundColor: Theme.colors.surface,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    justifyContent: 'center',
  },
  filterTriggerBtnActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  activeBadge: {
    position: 'absolute',
    top: -4,
    right: -4,
    backgroundColor: '#fff',
    width: 18,
    height: 18,
    borderRadius: 9,
    alignItems: 'center',
    justifyContent: 'center',
  },
  activeBadgeText: {
    fontSize: 10,
    fontWeight: '800',
    color: Theme.colors.primary,
  },
  chipsRow: {
    flexDirection: 'row',
    gap: Theme.spacing.xs,
    paddingVertical: 2,
  },
  categoryChip: {
    paddingHorizontal: 14,
    paddingVertical: 7,
    borderRadius: Theme.radius.full,
    backgroundColor: Theme.colors.surface,
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
  activePillsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    paddingTop: 2,
  },
  activePill: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    borderWidth: 1,
    borderColor: Theme.colors.primary,
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: Theme.radius.full,
  },
  activePillExpiring: {
    backgroundColor: Theme.colors.expiringSoonBg,
    borderColor: Theme.colors.expiringSoon,
  },
  activePillExpired: {
    backgroundColor: Theme.colors.expiredBg,
    borderColor: Theme.colors.expired,
  },
  activePillText: {
    fontSize: 12,
    fontWeight: '700',
    color: Theme.colors.primaryLight,
  },
  clearAllLink: {
    paddingHorizontal: 8,
    paddingVertical: 4,
  },
  clearAllLinkText: {
    fontSize: 12,
    color: Theme.colors.textMuted,
    fontWeight: '600',
    textDecorationLine: 'underline',
  },
});
