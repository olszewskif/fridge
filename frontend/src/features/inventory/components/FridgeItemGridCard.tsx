import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
import { Image } from 'expo-image';
import { Package, User, Check } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { FridgeItem } from '../types/inventory.types';
import { useProduct } from '../../catalog/hooks/useProduct';
import { formatNetQuantity } from '../../../utils/format';
import { useDragSelectCard } from '../hooks/useDragSelectCard';

interface Props {
  item: FridgeItem;
  index?: number;
  cardWidth?: number;
  isSelectionMode?: boolean;
  isSelected?: boolean;
  onPress?: () => void;
  onDragStart?: (index: number) => void;
  onDragMove?: (index: number, dx: number, dy: number) => void;
  onDragEnd?: () => void;
}

export function FridgeItemGridCard({
  item,
  index = 0,
  cardWidth,
  isSelectionMode,
  isSelected,
  onPress,
  onDragStart,
  onDragMove,
  onDragEnd,
}: Props) {
  const router = useRouter();
  // Only query catalog if product was not already enriched by the backend
  const hasEmbeddedProduct = !!item.product;
  const { data: fetchedProduct } = useProduct(hasEmbeddedProduct ? undefined : item.productId);

  const handlePress = () => {
    if (onPress) {
      onPress();
      return;
    }
    if (isSelectionMode) {
      return;
    }
    router.push({
      pathname: '/product/[id]',
      params: {
        id: item.productId,
        quantity: String(item.quantity),
        expiryDate: item.expiryDate || '',
        fridgeItemId: item.id,
      },
    });
  };

  const panResponder = useDragSelectCard({
    itemId: item.id,
    index,
    isSelectionMode,
    onPress: handlePress,
    onDragStart,
    onDragMove,
    onDragEnd,
  });

  // Expiry calculation helper
  const getExpiryStatus = () => {
    if (!item.expiryDate) {
      return { label: 'No Expiry', color: Theme.colors.textMuted, bg: Theme.colors.surfaceElevated };
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const [year, month, day] = item.expiryDate.split('-').map(Number);
    const expDate = new Date(year, month - 1, day);

    const diffDays = Math.ceil((expDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));

    if (diffDays < 0) {
      return {
        label: `Expired (${Math.abs(diffDays)}d)`,
        color: Theme.colors.expired,
        bg: Theme.colors.expiredBg,
      };
    }
    if (diffDays === 0) {
      return { label: 'Today', color: Theme.colors.expiringSoon, bg: Theme.colors.expiringSoonBg };
    }
    if (diffDays <= 3) {
      return {
        label: `${diffDays}d left`,
        color: Theme.colors.expiringSoon,
        bg: Theme.colors.expiringSoonBg,
      };
    }
    return {
      label: `${diffDays}d left`,
      color: Theme.colors.fresh,
      bg: Theme.colors.freshBg,
    };
  };

  const status = getExpiryStatus();
  const productName =
    item.product?.name ||
    fetchedProduct?.name ||
    item.productName ||
    item.brand ||
    `Product ${item.productId.slice(0, 8)}`;

  const imageUrl = item.product?.imageUrl || fetchedProduct?.imageUrl;

  const packageSizeStr = item.product?.netQuantity
    ? `${item.product.netQuantity.value} ${item.product.netQuantity.unit}`
    : fetchedProduct?.netQuantityDto
    ? formatNetQuantity(fetchedProduct.netQuantityDto)
    : null;

  return (
    <View
      {...panResponder.panHandlers}
      style={[
        styles.card,
        cardWidth ? { width: cardWidth } : undefined,
        isSelectionMode && isSelected && styles.cardSelected,
      ]}
    >
      {/* Selection Checkbox Overlay */}
      {isSelectionMode && (
        <View style={[styles.checkbox, isSelected && styles.checkboxActive]}>
          {isSelected && <Check size={12} color="#fff" />}
        </View>
      )}

      {/* Expiry Badge */}
      <View style={[styles.expiryBadge, { backgroundColor: status.bg }]}>
        <Text style={[styles.expiryBadgeText, { color: status.color }]}>{status.label}</Text>
      </View>

      {/* Product Image */}
      <View style={styles.imageContainer}>
        {imageUrl ? (
          <Image
            source={{ uri: imageUrl }}
            style={styles.image}
            contentFit="contain"
            transition={150}
          />
        ) : (
          <View style={styles.imagePlaceholder}>
            <Package size={32} color={Theme.colors.primary} />
          </View>
        )}
      </View>

      {/* Info Body */}
      <View style={styles.infoBody}>
        <Text style={styles.productName} numberOfLines={2}>
          {productName}
        </Text>

        <View style={styles.footerRow}>
          <Text style={styles.amountText}>
            {item.quantity} {item.quantity === 1 ? 'pc' : 'pcs'}
          </Text>
          {packageSizeStr && (
            <Text style={styles.packageSizeText} numberOfLines={1}>
              {packageSizeStr}
            </Text>
          )}
        </View>

        {/* Member row (fixed height container so card height never jumps) */}
        <View style={styles.memberRowContainer}>
          {item.addedBy ? (
            <View style={styles.memberTag}>
              <User size={10} color={Theme.colors.textMuted} />
              <Text style={styles.memberText} numberOfLines={1}>
                {item.addedBy.slice(0, 4)}
              </Text>
            </View>
          ) : null}
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    height: 220,
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    marginBottom: Theme.spacing.md,
    padding: Theme.spacing.sm,
    overflow: 'hidden',
    justifyContent: 'space-between',
  },
  cardSelected: {
    borderColor: Theme.colors.primary,
    backgroundColor: 'rgba(64, 0, 255, 0.08)',
  },
  checkbox: {
    position: 'absolute',
    top: 8,
    left: 8,
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: Theme.colors.surfaceBorder,
    backgroundColor: Theme.colors.surfaceElevated,
    zIndex: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  checkboxActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  expiryBadge: {
    position: 'absolute',
    top: 8,
    right: 8,
    paddingHorizontal: 7,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
    zIndex: 5,
  },
  expiryBadgeText: {
    fontSize: 10,
    fontWeight: '700',
  },
  imageContainer: {
    height: 84,
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 14,
    marginBottom: 6,
  },
  image: {
    width: '100%',
    height: '100%',
  },
  imagePlaceholder: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(64, 0, 255, 0.1)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  infoBody: {
    gap: 4,
  },
  productName: {
    fontSize: 13,
    lineHeight: 18,
    height: 36,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  footerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    height: 18,
  },
  amountText: {
    fontSize: 12,
    fontWeight: '700',
    color: Theme.colors.textSecondary,
  },
  packageSizeText: {
    fontSize: 11,
    color: Theme.colors.primaryLight,
    fontWeight: '600',
  },
  memberRowContainer: {
    height: 20,
    justifyContent: 'center',
  },
  memberTag: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
    backgroundColor: Theme.colors.surfaceElevated,
    alignSelf: 'flex-start',
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
  },
  memberText: {
    fontSize: 10,
    color: Theme.colors.textMuted,
    fontWeight: '600',
  },
});
