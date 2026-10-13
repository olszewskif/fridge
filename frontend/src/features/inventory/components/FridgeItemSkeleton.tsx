import React from 'react';
import { View, StyleSheet } from 'react-native';
import { SkeletonBox } from '../../../components/Skeleton';
import { Theme } from '../../../constants/theme';

export function FridgeItemCardSkeleton() {
  return (
    <View style={styles.card}>
      <View style={styles.leftCol}>
        {/* Thumbnail Icon Box */}
        <SkeletonBox width={44} height={44} borderRadius={12} style={styles.iconWrap} />

        <View style={styles.infoCol}>
          {/* Product Name Title */}
          <SkeletonBox width="60%" height={16} borderRadius={6} style={{ marginBottom: 8 }} />

          {/* Metadata Row (Amount, Size, Expiry, Member) */}
          <View style={styles.metaRow}>
            <SkeletonBox width={36} height={12} borderRadius={4} />
            <SkeletonBox width={45} height={12} borderRadius={4} />
            <SkeletonBox width={72} height={18} borderRadius={6} />
            <SkeletonBox width={40} height={18} borderRadius={6} />
          </View>
        </View>
      </View>
    </View>
  );
}

export function FridgeItemGridCardSkeleton({ cardWidth }: { cardWidth?: number }) {
  return (
    <View style={[styles.gridCard, cardWidth ? { width: cardWidth } : undefined]}>
      {/* Top Expiry Tag Skeleton */}
      <SkeletonBox
        width={56}
        height={18}
        borderRadius={6}
        style={styles.gridExpiryBadge}
      />

      {/* Main Product Image Skeleton */}
      <SkeletonBox
        width="100%"
        height={110}
        borderRadius={Theme.radius.md}
        style={{ marginBottom: 10 }}
      />

      {/* Product Title Lines */}
      <SkeletonBox width="85%" height={14} borderRadius={4} style={{ marginBottom: 6 }} />
      <SkeletonBox width="50%" height={14} borderRadius={4} style={{ marginBottom: 12 }} />

      {/* Footer Info Row */}
      <View style={styles.gridFooterRow}>
        <SkeletonBox width={32} height={12} borderRadius={4} />
        <SkeletonBox width={45} height={12} borderRadius={4} />
      </View>

      {/* Member Tag */}
      <View style={styles.gridMemberRow}>
        <SkeletonBox width={42} height={16} borderRadius={6} />
      </View>
    </View>
  );
}

interface InventoryLoadingSkeletonProps {
  viewMode: 'list' | 'grid';
  gridCardWidth?: number;
  count?: number;
}

export function InventoryLoadingSkeleton({
  viewMode,
  gridCardWidth,
  count = 6,
}: InventoryLoadingSkeletonProps) {
  const items = Array.from({ length: count });

  if (viewMode === 'grid') {
    return (
      <View style={styles.gridWrapper}>
        {items.map((_, idx) => (
          <FridgeItemGridCardSkeleton key={idx} cardWidth={gridCardWidth} />
        ))}
      </View>
    );
  }

  return (
    <View style={styles.listWrapper}>
      {items.map((_, idx) => (
        <FridgeItemCardSkeleton key={idx} />
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    marginBottom: Theme.spacing.sm,
  },
  leftCol: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  iconWrap: {
    marginRight: 12,
  },
  infoCol: {
    flex: 1,
  },
  metaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  gridWrapper: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.xxl,
  },
  listWrapper: {
    paddingHorizontal: Theme.spacing.lg,
    paddingBottom: Theme.spacing.xxl,
  },
  gridCard: {
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    padding: Theme.spacing.md,
    marginBottom: 12,
  },
  gridExpiryBadge: {
    marginBottom: 8,
  },
  gridFooterRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  gridMemberRow: {
    marginTop: 2,
  },
});
