import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
  Alert,
} from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Image } from 'expo-image';
import {
  ArrowLeft,
  Barcode,
  Package,
  Layers,
  Calendar,
  Scale,
  RefreshCw,
  AlertCircle,
  Tag,
  Edit3,
  Trash2,
} from 'lucide-react-native';
import { Theme } from '../../constants/theme';
import { formatNetQuantity } from '../../utils/format';
import { useProduct } from '../../features/catalog/hooks/useProduct';
import { ProductDetailSkeleton } from '../../features/catalog/components/ProductDetailSkeleton';
import { EditFridgeItemModal } from '../../features/inventory/components/EditFridgeItemModal';
import { useRemoveFridgeItem, useFridgeItem } from '../../features/inventory/hooks/useFridgeItems';
import { useFridgeStore } from '../../store/useFridgeStore';

export default function ProductDetailScreen() {
  const router = useRouter();
  const insets = useSafeAreaInsets();
  const { activeFridge } = useFridgeStore();
  const params = useLocalSearchParams<{
    id: string;
    quantity?: string;
    expiryDate?: string;
    fridgeItemId?: string;
  }>();

  const productId = params.id;
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [currentQuantity, setCurrentQuantity] = useState(
    params.quantity ? parseInt(params.quantity, 10) : 1
  );
  const [currentExpiryDate, setCurrentExpiryDate] = useState<string | null>(
    params.expiryDate || null
  );

  const hasFridgeItem = !!params.fridgeItemId;

  const {
    data: fridgeItemData,
    isLoading: isFridgeItemLoading,
    isError: isFridgeItemError,
    error: fridgeItemError,
    refetch: refetchFridgeItem,
    isRefetching: isFridgeItemRefetching,
  } = useFridgeItem(activeFridge?.id, params.fridgeItemId);

  // Only query catalog if not opened from a fridge item
  const {
    data: catalogProduct,
    isLoading: isCatalogLoading,
    isError: isCatalogError,
    error: catalogError,
    refetch: refetchCatalog,
    isRefetching: isCatalogRefetching,
  } = useProduct(hasFridgeItem ? undefined : productId);

  React.useEffect(() => {
    if (fridgeItemData) {
      setCurrentQuantity(fridgeItemData.quantity);
      setCurrentExpiryDate(fridgeItemData.expiryDate || null);
    }
  }, [fridgeItemData]);

  const product = React.useMemo(() => {
    if (hasFridgeItem && fridgeItemData?.product) {
      const p = fridgeItemData.product;
      return {
        id: p.productId,
        name: p.name,
        brand: p.brand,
        barcode: p.barcode,
        imageUrl: p.imageUrl,
        netQuantityDto: p.netQuantity
          ? { amount: p.netQuantity.value, unit: p.netQuantity.unit }
          : null,
        categories: p.categories || [],
      };
    }
    return catalogProduct || null;
  }, [hasFridgeItem, fridgeItemData, catalogProduct]);

  const isLoading = hasFridgeItem ? isFridgeItemLoading : isCatalogLoading;
  const isError = hasFridgeItem ? isFridgeItemError : isCatalogError;
  const error = hasFridgeItem ? fridgeItemError : catalogError;
  const refetch = hasFridgeItem ? refetchFridgeItem : refetchCatalog;
  const isRefetching = hasFridgeItem ? isFridgeItemRefetching : isCatalogRefetching;

  const removeFridgeItemMutation = useRemoveFridgeItem();

  // Calculate expiry status if expiryDate was provided from fridge item
  const getExpiryDetails = (expiryDate?: string) => {
    if (!expiryDate) return null;

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [y, m, d] = expiryDate.split('-').map(Number);
    const exp = new Date(y, m - 1, d);
    const diff = Math.ceil((exp.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

    if (diff < 0) {
      return {
        label: `Expired (${Math.abs(diff)}d ago)`,
        color: Theme.colors.expired,
        bg: Theme.colors.expiredBg,
      };
    }
    if (diff === 0) {
      return {
        label: 'Expires Today',
        color: Theme.colors.expiringSoon,
        bg: Theme.colors.expiringSoonBg,
      };
    }
    if (diff <= 3) {
      return {
        label: `Expires in ${diff}d`,
        color: Theme.colors.expiringSoon,
        bg: Theme.colors.expiringSoonBg,
      };
    }
    return {
      label: `${diff} days left`,
      color: Theme.colors.fresh,
      bg: Theme.colors.freshBg,
    };
  };

  const expiryStatus = getExpiryDetails(currentExpiryDate || undefined);

  const handleRemoveFridgeItem = () => {
    if (!activeFridge || !params.fridgeItemId) return;
    Alert.alert(
      'Remove from Fridge',
      `Do you want to remove this item from ${activeFridge.name}?`,
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Remove',
          style: 'destructive',
          onPress: async () => {
            try {
              await removeFridgeItemMutation.mutateAsync({
                fridgeId: activeFridge.id,
                itemId: params.fridgeItemId!,
              });
              Alert.alert('Removed', 'Item was removed from your fridge.');
              router.back();
            } catch (err: any) {
              Alert.alert('Error', err?.message || 'Could not remove item from fridge.');
            }
          },
        },
      ]
    );
  };

  return (
    <View style={[styles.container, { paddingTop: insets.top }]}>
      {/* Top Navigation Bar */}
      <View style={styles.topBar}>
        <TouchableOpacity
          style={styles.backBtn}
          onPress={() => router.back()}
          hitSlop={12}
          activeOpacity={0.7}
        >
          <ArrowLeft size={22} color={Theme.colors.text} />
        </TouchableOpacity>
        <Text style={styles.topBarTitle}>Product Details</Text>
        <View style={styles.topActions}>
          {params.fridgeItemId && activeFridge && (
            <>
              <TouchableOpacity
                style={styles.iconBtn}
                onPress={() => setEditModalVisible(true)}
                hitSlop={8}
                activeOpacity={0.7}
              >
                <Edit3 size={18} color={Theme.colors.textSecondary} />
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.iconBtn}
                onPress={handleRemoveFridgeItem}
                hitSlop={8}
                activeOpacity={0.7}
              >
                <Trash2 size={18} color={Theme.colors.expired} />
              </TouchableOpacity>
            </>
          )}
          <TouchableOpacity
            style={styles.iconBtn}
            onPress={() => refetch()}
            disabled={isLoading || isRefetching}
            hitSlop={8}
            activeOpacity={0.7}
          >
            <RefreshCw
              size={18}
              color={isRefetching ? Theme.colors.primary : Theme.colors.textMuted}
            />
          </TouchableOpacity>
        </View>
      </View>

      {isLoading ? (
        <ProductDetailSkeleton />
      ) : isError || !product ? (
        <View style={styles.centerContainer}>
          <AlertCircle size={48} color={Theme.colors.expired} />
          <Text style={styles.errorTitle}>Unable to load product</Text>
          <Text style={styles.errorSubtitle}>
            {(error as Error)?.message || 'Product details could not be found.'}
          </Text>
          <TouchableOpacity style={styles.retryBtn} onPress={() => refetch()}>
            <Text style={styles.retryBtnText}>Retry</Text>
          </TouchableOpacity>
        </View>
      ) : (
        <ScrollView
          showsVerticalScrollIndicator={false}
          contentContainerStyle={[styles.scrollContent, { paddingBottom: insets.bottom + 32 }]}
        >
          {/* Hero Media Card */}
          <View style={styles.heroCard}>
            {product.imageUrl ? (
              <Image
                source={{ uri: product.imageUrl }}
                style={styles.productImage}
                contentFit="contain"
                transition={200}
              />
            ) : (
              <View style={styles.imagePlaceholder}>
                <Package size={56} color={Theme.colors.primary} />
              </View>
            )}
          </View>

          {/* Title & Brand */}
          <View style={styles.mainInfoSection}>
            <View style={styles.badgesRow}>
              {product.brand ? (
                <View style={styles.brandBadge}>
                  <Text style={styles.brandText}>{product.brand.toUpperCase()}</Text>
                </View>
              ) : null}
              {product.netQuantityDto ? (
                <View style={styles.netWeightBadge}>
                  <Scale size={12} color={Theme.colors.primaryLight} />
                  <Text style={styles.netWeightText}>
                    {formatNetQuantity(product.netQuantityDto)}
                  </Text>
                </View>
              ) : null}
            </View>

            <Text style={styles.productName}>{product.name}</Text>

            {/* Barcode Chip */}
            {product.barcode ? (
              <View style={styles.barcodeChip}>
                <Barcode size={16} color={Theme.colors.textSecondary} />
                <Text style={styles.barcodeText}>{product.barcode}</Text>
              </View>
            ) : null}
          </View>

          {/* Fridge Item Context (if navigated from fridge) */}
          {params.fridgeItemId && (
            <View style={styles.sectionCard}>
              <View style={styles.sectionHeader}>
                <Layers size={18} color={Theme.colors.primary} />
                <Text style={styles.sectionTitle}>CURRENT IN FRIDGE</Text>
              </View>

              <View style={styles.fridgeGrid}>
                <View style={styles.fridgeMetric}>
                  <Package size={16} color={Theme.colors.textMuted} />
                  <View>
                    <Text style={styles.metricLabel}>Stock In Fridge</Text>
                    <Text style={styles.metricValue}>
                      {currentQuantity} {currentQuantity === 1 ? 'package' : 'packages'}
                    </Text>
                  </View>
                </View>

                <View style={styles.fridgeMetric}>
                  <Calendar size={16} color={Theme.colors.textMuted} />
                  <View>
                    <Text style={styles.metricLabel}>Expiry Date</Text>
                    <Text style={styles.metricValue}>
                      {currentExpiryDate || 'No Expiry'}
                    </Text>
                    {expiryStatus && (
                      <View
                        style={[
                          styles.expiryBadge,
                          { backgroundColor: expiryStatus.bg },
                        ]}
                      >
                        <Text
                          style={[
                            styles.expiryBadgeText,
                            { color: expiryStatus.color },
                          ]}
                        >
                          {expiryStatus.label}
                        </Text>
                      </View>
                    )}
                  </View>
                </View>
              </View>
            </View>
          )}

          {/* Categories Section */}
          <View style={styles.sectionCard}>
            <View style={styles.sectionHeader}>
              <Tag size={18} color={Theme.colors.primary} />
              <Text style={styles.sectionTitle}>CATEGORIES</Text>
            </View>

            {product.categories && product.categories.length > 0 ? (
              <View style={styles.categoriesWrap}>
                {product.categories.map((cat) => (
                  <View key={cat.id} style={styles.categoryTag}>
                    <Text style={styles.categoryTagText}>{cat.name}</Text>
                  </View>
                ))}
              </View>
            ) : (
              <Text style={styles.noCategoriesText}>No categories assigned</Text>
            )}
          </View>

          {/* Technical Metadata */}
          {product.netQuantityDto && (
            <View style={styles.metaCard}>
              <Text style={styles.metaLabel}>Package Size</Text>
              <Text style={styles.metaValue}>
                {formatNetQuantity(product.netQuantityDto)}
              </Text>
            </View>
          )}

          <View style={styles.metaCard}>
            <Text style={styles.metaLabel}>Product ID</Text>
            <Text style={styles.metaValue} numberOfLines={1} ellipsizeMode="middle">
              {product.id}
            </Text>
          </View>

          {/* Quick Action to Filter Inventory by this Product */}
          <TouchableOpacity
            style={styles.findInFridgeBtn}
            onPress={() => {
              router.push({
                pathname: '/(tabs)/inventory',
                params: {
                  productId: product.id,
                  productName: product.name,
                },
              });
            }}
            activeOpacity={0.8}
          >
            <Layers size={18} color="#fff" />
            <Text style={styles.findInFridgeBtnText}>View All in Fridge Inventory</Text>
          </TouchableOpacity>
        </ScrollView>
      )}

      {params.fridgeItemId && activeFridge && (
        <EditFridgeItemModal
          visible={editModalVisible}
          onClose={() => setEditModalVisible(false)}
          fridgeId={activeFridge.id}
          itemId={params.fridgeItemId}
          currentQuantity={currentQuantity}
          currentExpiryDate={currentExpiryDate}
          productName={product?.name}
          packageSize={product?.netQuantityDto ? formatNetQuantity(product.netQuantityDto) : null}
          onSuccess={(newQty, newExp) => {
            setCurrentQuantity(newQty);
            setCurrentExpiryDate(newExp || null);
          }}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.colors.background,
  },
  topActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  iconBtn: {
    width: 38,
    height: 38,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  fridgeActionsRow: {
    flexDirection: 'row',
    gap: Theme.spacing.sm,
    marginTop: 6,
  },
  editFridgeItemBtn: {
    flex: 2,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
    paddingVertical: 10,
    backgroundColor: 'rgba(64, 0, 255, 0.12)',
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.primary,
  },
  editFridgeItemBtnText: {
    color: Theme.colors.primaryLight,
    fontSize: 13,
    fontWeight: '700',
  },
  removeFridgeItemBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
    paddingVertical: 10,
    backgroundColor: Theme.colors.expiredBg,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: 'rgba(239, 68, 68, 0.3)',
  },
  removeFridgeItemBtnText: {
    color: Theme.colors.expired,
    fontSize: 13,
    fontWeight: '700',
  },
  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: Theme.spacing.lg,
    paddingVertical: Theme.spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: Theme.colors.surfaceBorder,
  },
  backBtn: {
    width: 40,
    height: 40,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  topBarTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  refreshBtn: {
    width: 40,
    height: 40,
    borderRadius: Theme.radius.md,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  centerContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: Theme.spacing.xl,
    gap: Theme.spacing.md,
  },
  loadingText: {
    fontSize: 14,
    color: Theme.colors.textSecondary,
  },
  errorTitle: {
    fontSize: 18,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  errorSubtitle: {
    fontSize: 14,
    color: Theme.colors.textSecondary,
    textAlign: 'center',
  },
  retryBtn: {
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.sm,
  },
  retryBtnText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 14,
  },
  scrollContent: {
    paddingHorizontal: Theme.spacing.lg,
    paddingTop: Theme.spacing.lg,
    gap: Theme.spacing.md,
  },
  heroCard: {
    height: 220,
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.xl,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    justifyContent: 'center',
    overflow: 'hidden',
    padding: Theme.spacing.md,
  },
  productImage: {
    width: '100%',
    height: '100%',
  },
  imagePlaceholder: {
    width: 88,
    height: 88,
    borderRadius: 44,
    backgroundColor: 'rgba(64, 0, 255, 0.12)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  mainInfoSection: {
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.lg,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.sm,
  },
  badgesRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    flexWrap: 'wrap',
  },
  brandBadge: {
    alignSelf: 'flex-start',
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: Theme.radius.full,
  },
  brandText: {
    color: Theme.colors.primaryLight,
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 0.8,
  },
  netWeightBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: Theme.radius.full,
  },
  netWeightText: {
    color: Theme.colors.primaryLight,
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 0.8,
  },
  productName: {
    fontSize: 22,
    fontWeight: '800',
    color: Theme.colors.text,
    lineHeight: 28,
  },
  barcodeChip: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginTop: 4,
  },
  barcodeText: {
    fontSize: 13,
    color: Theme.colors.textSecondary,
    fontFamily: 'monospace',
  },
  sectionCard: {
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.lg,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.md,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  sectionTitle: {
    fontSize: 12,
    fontWeight: '800',
    color: Theme.colors.textSecondary,
    letterSpacing: 0.8,
  },
  fridgeGrid: {
    gap: Theme.spacing.md,
  },
  fridgeMetric: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 12,
  },
  metricLabel: {
    fontSize: 12,
    color: Theme.colors.textMuted,
    fontWeight: '500',
  },
  metricValue: {
    fontSize: 16,
    fontWeight: '700',
    color: Theme.colors.text,
    marginTop: 2,
  },
  expiryBadge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: Theme.radius.full,
    marginTop: 6,
  },
  expiryBadgeText: {
    fontSize: 11,
    fontWeight: '700',
  },
  categoriesWrap: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  categoryTag: {
    backgroundColor: Theme.colors.surfaceElevated,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: Theme.radius.md,
  },
  categoryTagText: {
    color: Theme.colors.text,
    fontSize: 13,
    fontWeight: '600',
  },
  noCategoriesText: {
    fontSize: 13,
    color: Theme.colors.textMuted,
    fontStyle: 'italic',
  },
  metaCard: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: Theme.spacing.md,
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.md,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  metaLabel: {
    fontSize: 12,
    color: Theme.colors.textMuted,
    fontWeight: '600',
  },
  metaValue: {
    fontSize: 12,
    color: Theme.colors.textSecondary,
    fontFamily: 'monospace',
    maxWidth: 220,
  },
  findInFridgeBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: Theme.spacing.sm,
    backgroundColor: Theme.colors.primary,
    paddingVertical: 14,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.sm,
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.35,
    shadowRadius: 8,
    elevation: 4,
  },
  findInFridgeBtnText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
});
