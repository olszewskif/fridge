import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
import { Image } from 'expo-image';
import { Package, ChevronRight, User, Check } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { FridgeItem } from '../types/inventory.types';
import { useProduct } from '../../catalog/hooks/useProduct';
import { formatNetQuantity } from '../../../utils/format';
import { useDragSelectCard } from '../hooks/useDragSelectCard';

interface Props {
  item: FridgeItem;
  index?: number;
  isSelectionMode?: boolean;
  isSelected?: boolean;
  onPress?: () => void;
  onDragStart?: (index: number) => void;
  onDragMove?: (index: number, dx: number, dy: number) => void;
  onDragEnd?: () => void;
}

export function FridgeItemCard({
  item,
  index = 0,
  isSelectionMode,
  isSelected,
  onPress,
  onDragStart,
  onDragMove,
  onDragEnd,
}: Props) {
  const router = useRouter();
  const { data: product } = useProduct(item.productId);

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
        label: `Expired (${Math.abs(diffDays)}d ago)`,
        color: Theme.colors.expired,
        bg: Theme.colors.expiredBg,
      };
    }
    if (diffDays === 0) {
      return { label: 'Expires Today', color: Theme.colors.expiringSoon, bg: Theme.colors.expiringSoonBg };
    }
    if (diffDays <= 3) {
      return {
        label: `Expires in ${diffDays}d`,
        color: Theme.colors.expiringSoon,
        bg: Theme.colors.expiringSoonBg,
      };
    }
    return {
      label: `${diffDays} days left`,
      color: Theme.colors.fresh,
      bg: Theme.colors.freshBg,
    };
  };

  const status = getExpiryStatus();
  const productName = product?.name || item.productName || item.brand || `Product ${item.productId.slice(0, 8)}`;
  const packageSizeStr = product?.netQuantityDto ? formatNetQuantity(product.netQuantityDto) : null;

  return (
    <View
      {...panResponder.panHandlers}
      style={[
        styles.card,
        isSelectionMode && isSelected && styles.cardSelected,
      ]}
    >
      {/* Selection Checkbox */}
      {isSelectionMode && (
        <View style={[styles.checkbox, isSelected && styles.checkboxActive]}>
          {isSelected && <Check size={12} color="#fff" />}
        </View>
      )}

      <View style={styles.leftCol}>
        <View style={styles.iconWrap}>
          {product?.imageUrl ? (
            <Image
              source={{ uri: product.imageUrl }}
              style={styles.thumbImage}
              contentFit="contain"
              transition={150}
            />
          ) : (
            <Package size={22} color={Theme.colors.primary} />
          )}
        </View>

        <View style={styles.infoCol}>
          <Text style={styles.productName} numberOfLines={1}>
            {productName}
          </Text>

          <View style={styles.metaRow}>
            <Text style={styles.amountText}>
              {item.quantity} {item.quantity === 1 ? 'pc' : 'pcs'}
            </Text>

            {packageSizeStr && (
              <>
                <View style={styles.dot} />
                <Text style={styles.netSizeText}>{packageSizeStr}</Text>
              </>
            )}

            <View style={styles.dot} />
            <View style={[styles.badge, { backgroundColor: status.bg }]}>
              <Text style={[styles.badgeText, { color: status.color }]}>{status.label}</Text>
            </View>

            {item.addedBy && (
              <View style={styles.memberTag}>
                <User size={10} color={Theme.colors.textMuted} />
                <Text style={styles.memberTagText}>{item.addedBy.slice(0, 4)}</Text>
              </View>
            )}
          </View>
        </View>

        {!isSelectionMode && (
          <ChevronRight size={18} color={Theme.colors.textMuted} style={styles.chevron} />
        )}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    marginBottom: Theme.spacing.sm,
  },
  cardSelected: {
    borderColor: Theme.colors.primary,
    backgroundColor: 'rgba(64, 0, 255, 0.08)',
  },
  checkbox: {
    width: 22,
    height: 22,
    borderRadius: 11,
    borderWidth: 2,
    borderColor: Theme.colors.surfaceBorder,
    backgroundColor: Theme.colors.surfaceElevated,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 12,
  },
  checkboxActive: {
    backgroundColor: Theme.colors.primary,
    borderColor: Theme.colors.primary,
  },
  leftCol: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.md,
    flex: 1,
  },
  iconWrap: {
    width: 44,
    height: 44,
    borderRadius: Theme.radius.md,
    backgroundColor: 'rgba(64, 0, 255, 0.12)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  infoCol: {
    flex: 1,
  },
  productName: {
    fontSize: 16,
    fontWeight: '700',
    color: Theme.colors.text,
    marginBottom: 4,
  },
  metaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  amountText: {
    fontSize: 13,
    color: Theme.colors.textSecondary,
    fontWeight: '600',
  },
  dot: {
    width: 3,
    height: 3,
    borderRadius: 1.5,
    backgroundColor: Theme.colors.textMuted,
  },
  badge: {
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
  },
  badgeText: {
    fontSize: 11,
    fontWeight: '700',
  },
  chevron: {
    marginLeft: 4,
  },
  thumbImage: {
    width: '100%',
    height: '100%',
    borderRadius: Theme.radius.sm,
  },
  netSizeText: {
    fontSize: 12,
    color: Theme.colors.primaryLight,
    fontWeight: '600',
  },
  memberTag: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 3,
    backgroundColor: Theme.colors.surfaceElevated,
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: Theme.radius.full,
    marginLeft: 2,
  },
  memberTagText: {
    fontSize: 10,
    color: Theme.colors.textMuted,
    fontWeight: '600',
  },
});
