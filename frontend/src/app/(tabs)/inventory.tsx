import React, { useState, useMemo, useEffect, useRef } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  ActivityIndicator,
  RefreshControl,
  TouchableOpacity,
  Alert,
  useWindowDimensions,
} from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { Package, Camera, CheckSquare, Trash2, X } from 'lucide-react-native';
import { Theme } from '../../constants/theme';
import { useFridgeStore } from '../../store/useFridgeStore';
import {
  useFridgeItems,
  useRemoveFridgeItem,
  useBulkRemoveFridgeItems,
} from '../../features/inventory/hooks/useFridgeItems';
import { FridgeHeader } from '../../features/fridges/components/FridgeHeader';
import { FridgeItemCard } from '../../features/inventory/components/FridgeItemCard';
import { FridgeItemGridCard } from '../../features/inventory/components/FridgeItemGridCard';
import { InventoryLoadingSkeleton } from '../../features/inventory/components/FridgeItemSkeleton';
import {
  InventoryFilters,
  ExpiryFilterType,
} from '../../features/inventory/components/InventoryFilters';

function calculateTargetIndex(
  startIndex: number,
  dx: number,
  dy: number,
  viewMode: 'list' | 'grid',
  cardWidth: number,
  totalItems: number
): number {
  if (totalItems <= 1) return 0;

  if (viewMode === 'list') {
    const ROW_HEIGHT = 80;
    const rowDelta = Math.round(dy / ROW_HEIGHT);
    const targetIndex = startIndex + rowDelta;
    return Math.max(0, Math.min(totalItems - 1, targetIndex));
  } else {
    const ROW_HEIGHT = 236; // 220 height + 16 marginBottom
    const COL_WIDTH = cardWidth + 12; // cardWidth + 12 gap
    const startRow = Math.floor(startIndex / 2);
    const startCol = startIndex % 2;

    const rowDelta = Math.round(dy / ROW_HEIGHT);
    const colDelta = Math.round(dx / COL_WIDTH);

    const targetRow = Math.max(0, startRow + rowDelta);
    const targetCol = Math.max(0, Math.min(1, startCol + colDelta));

    const targetIndex = targetRow * 2 + targetCol;
    return Math.max(0, Math.min(totalItems - 1, targetIndex));
  }
}

export default function InventoryScreen() {
  const router = useRouter();
  const routeParams = useLocalSearchParams<{
    productId?: string;
    productName?: string;
    expiryFilter?: ExpiryFilterType;
  }>();
  const { activeFridge } = useFridgeStore();
  const { width: windowWidth } = useWindowDimensions();

  const GRID_GAP = 12;
  const HORIZONTAL_PADDING = Theme.spacing.lg * 2; // 24 * 2 = 48
  const gridCardWidth = Math.floor((windowWidth - HORIZONTAL_PADDING - GRID_GAP) / 2);

  const [search, setSearch] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');
  const [expiryFilter, setExpiryFilter] = useState<ExpiryFilterType>(
    routeParams.expiryFilter || 'ALL'
  );
  const [selectedCategoryIds, setSelectedCategoryIds] = useState<string[]>([]);
  const [addedBy, setAddedBy] = useState('');
  const [selectedProductId, setSelectedProductId] = useState<string | undefined>(
    routeParams.productId
  );
  const [selectedProductName, setSelectedProductName] = useState<string | undefined>(
    routeParams.productName
  );

  const [viewMode, setViewMode] = useState<'list' | 'grid'>('list');
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [selectedItemIds, setSelectedItemIds] = useState<Set<string>>(new Set());
  const [isDeletingBatch, setIsDeletingBatch] = useState(false);
  const [scrollEnabled, setScrollEnabled] = useState(true);

  const flatListRef = useRef<FlatList>(null);
  const dragStartIndexRef = useRef<number | null>(null);
  const initialSelectedIdsRef = useRef<Set<string>>(new Set());

  // Buffer search input so backend requests debounce by 200ms
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(search);
    }, 200);

    return () => clearTimeout(timer);
  }, [search]);

  useEffect(() => {
    if (routeParams.expiryFilter) {
      setExpiryFilter(routeParams.expiryFilter);
    }
    if (routeParams.productId) {
      setSelectedProductId(routeParams.productId);
      setSelectedProductName(routeParams.productName);
    }
  }, [routeParams.expiryFilter, routeParams.productId, routeParams.productName]);

  const handleCategoryToggle = (catId: string) => {
    setSelectedCategoryIds((prev) =>
      prev.includes(catId) ? prev.filter((id) => id !== catId) : [...prev, catId]
    );
  };

  const handleResetAll = () => {
    setSearch('');
    setDebouncedSearch('');
    setExpiryFilter('ALL');
    setSelectedCategoryIds([]);
    setAddedBy('');
    setSelectedProductId(undefined);
    setSelectedProductName(undefined);
  };

  // Build filter params for the backend with debouncedSearch
  const filterParams = useMemo(() => {
    return {
      search: debouncedSearch.trim() ? debouncedSearch.trim() : undefined,
      isExpired: expiryFilter === 'EXPIRED' ? true : undefined,
      expiresInDays: expiryFilter === 'EXPIRING_SOON' ? 3 : undefined,
      categoryIds: selectedCategoryIds.length > 0 ? selectedCategoryIds : undefined,
      productId: selectedProductId ? selectedProductId : undefined,
      addedBy: addedBy.trim() ? addedBy.trim() : undefined,
    };
  }, [debouncedSearch, expiryFilter, selectedCategoryIds, selectedProductId, addedBy]);

  const {
    data: itemsPage,
    isLoading,
    refetch,
    isRefetching,
  } = useFridgeItems(activeFridge?.id, filterParams);

  // Unfiltered items query to preserve all fridge members even when filtering
  const { data: allItemsPage } = useFridgeItems(activeFridge?.id, { size: 100 });

  const removeItemMutation = useRemoveFridgeItem();
  const bulkRemoveMutation = useBulkRemoveFridgeItems();

  const items = itemsPage?.content || [];

  const availableUsers = useMemo(() => {
    const set = new Set<string>();
    if (activeFridge?.createdBy) {
      set.add(activeFridge.createdBy);
    }
    allItemsPage?.content?.forEach((item) => {
      if (item.addedBy) set.add(item.addedBy);
    });
    if (addedBy.trim()) {
      set.add(addedBy.trim());
    }
    items.forEach((item) => {
      if (item.addedBy) set.add(item.addedBy);
    });
    return Array.from(set);
  }, [allItemsPage, items, activeFridge?.createdBy, addedBy]);

  const handleToggleSelect = (id: string) => {
    setSelectedItemIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  const handleDragStart = (startIndex: number) => {
    setIsSelectionMode(true);
    dragStartIndexRef.current = startIndex;
    initialSelectedIdsRef.current = new Set(selectedItemIds);
    setScrollEnabled(false);

    const targetItem = items[startIndex];
    if (targetItem) {
      setSelectedItemIds((prev) => {
        const next = new Set(prev);
        next.add(targetItem.id);
        return next;
      });
    }
  };

  const handleDragMove = (_: number, dx: number, dy: number) => {
    if (dragStartIndexRef.current === null || items.length === 0) return;

    const startIndex = dragStartIndexRef.current;
    const targetIndex = calculateTargetIndex(
      startIndex,
      dx,
      dy,
      viewMode,
      gridCardWidth,
      items.length
    );

    const minIdx = Math.min(startIndex, targetIndex);
    const maxIdx = Math.max(startIndex, targetIndex);

    const next = new Set(initialSelectedIdsRef.current);
    for (let i = minIdx; i <= maxIdx; i++) {
      if (items[i]) {
        next.add(items[i].id);
      }
    }

    setSelectedItemIds(next);
  };

  const handleDragEnd = () => {
    dragStartIndexRef.current = null;
    setScrollEnabled(true);
  };

  const handleSelectAll = () => {
    if (selectedItemIds.size === items.length) {
      setSelectedItemIds(new Set());
    } else {
      setSelectedItemIds(new Set(items.map((i) => i.id)));
    }
  };

  const handleCancelSelection = () => {
    setIsSelectionMode(false);
    setSelectedItemIds(new Set());
  };

  const handleDeleteSelected = () => {
    if (!activeFridge || selectedItemIds.size === 0) return;
    const count = selectedItemIds.size;
    Alert.alert(
      `Remove ${count} ${count === 1 ? 'Item' : 'Items'}`,
      `Are you sure you want to remove ${count} selected ${count === 1 ? 'item' : 'items'} from ${activeFridge.name}?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: `Delete (${count})`,
          style: 'destructive',
          onPress: async () => {
            setIsDeletingBatch(true);
            try {
              await bulkRemoveMutation.mutateAsync({
                fridgeId: activeFridge.id,
                itemIds: Array.from(selectedItemIds),
              });
              handleCancelSelection();
            } catch (err: any) {
              Alert.alert('Error', err?.message || 'Could not complete deletions.');
            } finally {
              setIsDeletingBatch(false);
            }
          },
        },
      ]
    );
  };

  return (
    <View style={styles.container}>
      <FridgeHeader />

      {isSelectionMode ? (
        <View style={styles.selectionBar}>
          <View style={styles.selectionCountWrap}>
            <CheckSquare size={18} color={Theme.colors.primary} />
            <Text style={styles.selectionCountText}>
              {selectedItemIds.size} of {items.length} selected
            </Text>
          </View>
          <View style={styles.selectionActions}>
            <TouchableOpacity
              onPress={handleSelectAll}
              style={styles.selectionActionBtn}
              activeOpacity={0.7}
            >
              <Text style={styles.selectionActionBtnText}>
                {selectedItemIds.size === items.length ? 'Deselect All' : 'Select All'}
              </Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleCancelSelection}
              style={styles.selectionCancelBtn}
              activeOpacity={0.7}
            >
              <X size={16} color={Theme.colors.textSecondary} />
              <Text style={styles.selectionCancelBtnText}>Done</Text>
            </TouchableOpacity>
          </View>
        </View>
      ) : (
        <InventoryFilters
          search={search}
          onSearchChange={setSearch}
          expiryFilter={expiryFilter}
          onExpiryFilterChange={setExpiryFilter}
          selectedCategoryIds={selectedCategoryIds}
          onCategoryToggle={handleCategoryToggle}
          addedBy={addedBy}
          onAddedByChange={setAddedBy}
          availableUsers={availableUsers}
          selectedProductId={selectedProductId}
          selectedProductName={selectedProductName}
          onClearProductFilter={() => {
            setSelectedProductId(undefined);
            setSelectedProductName(undefined);
          }}
          onResetAll={handleResetAll}
          viewMode={viewMode}
          onViewModeChange={setViewMode}
        />
      )}

      {isLoading ? (
        <InventoryLoadingSkeleton
          viewMode={viewMode}
          gridCardWidth={gridCardWidth}
          count={viewMode === 'grid' ? 6 : 8}
        />
      ) : (
        <FlatList
          ref={flatListRef}
          key={viewMode}
          data={items}
          keyExtractor={(item) => item.id}
          numColumns={viewMode === 'grid' ? 2 : 1}
          columnWrapperStyle={viewMode === 'grid' ? { gap: GRID_GAP } : undefined}
          scrollEnabled={scrollEnabled}
          contentContainerStyle={[
            styles.listContent,
            isSelectionMode && { paddingBottom: 160 },
          ]}
          showsVerticalScrollIndicator={false}
          refreshControl={
            <RefreshControl
              refreshing={isRefetching}
              onRefresh={refetch}
              tintColor={Theme.colors.primary}
            />
          }
          renderItem={({ item, index }) => {
            const isSelected = selectedItemIds.has(item.id);
            if (viewMode === 'grid') {
              return (
                <FridgeItemGridCard
                  item={item}
                  index={index}
                  cardWidth={gridCardWidth}
                  isSelectionMode={isSelectionMode}
                  isSelected={isSelected}
                  onPress={isSelectionMode ? () => handleToggleSelect(item.id) : undefined}
                  onDragStart={handleDragStart}
                  onDragMove={handleDragMove}
                  onDragEnd={handleDragEnd}
                />
              );
            }
            return (
              <FridgeItemCard
                item={item}
                index={index}
                isSelectionMode={isSelectionMode}
                isSelected={isSelected}
                onPress={isSelectionMode ? () => handleToggleSelect(item.id) : undefined}
                onDragStart={handleDragStart}
                onDragMove={handleDragMove}
                onDragEnd={handleDragEnd}
              />
            );
          }}
          ListEmptyComponent={
            <View style={styles.emptyContainer}>
              <Package size={48} color={Theme.colors.surfaceBorder} />
              <Text style={styles.emptyTitle}>
                {search.length > 0 ? 'No matching items' : 'No items in this category'}
              </Text>
              <Text style={styles.emptyDesc}>
                {search.length > 0
                  ? `Nothing in ${activeFridge?.name} matches "${search}"`
                  : 'Add items by scanning their barcode with the camera'}
              </Text>
              {search.length === 0 && (
                <TouchableOpacity
                  style={styles.scanCta}
                  onPress={() => router.push('/scan')}
                  activeOpacity={0.8}
                >
                  <Camera size={18} color="#fff" />
                  <Text style={styles.scanCtaText}>Scan Items Now</Text>
                </TouchableOpacity>
              )}
            </View>
          }
        />
      )}

      {/* Floating Bottom Batch Delete Action Bar */}
      {isSelectionMode && (
        <View style={styles.floatingDeleteContainer}>
          <TouchableOpacity
            style={[
              styles.floatingDeleteBtn,
              selectedItemIds.size === 0 && styles.floatingDeleteBtnDisabled,
            ]}
            onPress={handleDeleteSelected}
            disabled={selectedItemIds.size === 0 || isDeletingBatch}
            activeOpacity={0.8}
          >
            {isDeletingBatch ? (
              <ActivityIndicator color="#fff" size="small" />
            ) : (
              <>
                <Trash2 size={18} color="#fff" />
                <Text style={styles.floatingDeleteText}>
                  {selectedItemIds.size === 0
                    ? 'Select items to delete'
                    : `Delete (${selectedItemIds.size}) ${selectedItemIds.size === 1 ? 'Item' : 'Items'}`}
                </Text>
              </>
            )}
          </TouchableOpacity>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.colors.background,
  },
  listContent: {
    paddingHorizontal: Theme.spacing.lg,
    paddingTop: Theme.spacing.sm,
    paddingBottom: 120, // tab bar clearance
  },
  loadingContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  emptyContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 48,
    gap: Theme.spacing.sm,
  },
  emptyTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  emptyDesc: {
    fontSize: 14,
    color: Theme.colors.textSecondary,
    textAlign: 'center',
    maxWidth: 260,
    lineHeight: 20,
  },
  scanCta: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.sm,
  },
  scanCtaText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 14,
  },
  selectionBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: Theme.spacing.lg,
    paddingVertical: Theme.spacing.md,
    backgroundColor: Theme.colors.surface,
    borderBottomWidth: 1,
    borderBottomColor: Theme.colors.surfaceBorder,
    marginBottom: Theme.spacing.sm,
  },
  selectionCountWrap: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  selectionCountText: {
    fontSize: 14,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  selectionActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.md,
  },
  selectionActionBtn: {
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  selectionActionBtnText: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.primaryLight,
  },
  selectionCancelBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  selectionCancelBtnText: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.textSecondary,
  },
  floatingDeleteContainer: {
    position: 'absolute',
    bottom: 95, // above bottom navigation tab bar
    left: Theme.spacing.lg,
    right: Theme.spacing.lg,
    alignItems: 'center',
  },
  floatingDeleteBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    backgroundColor: Theme.colors.expired,
    paddingVertical: 14,
    paddingHorizontal: 28,
    borderRadius: Theme.radius.full,
    width: '100%',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
    elevation: 6,
  },
  floatingDeleteBtnDisabled: {
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    opacity: 0.6,
  },
  floatingDeleteText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
});
