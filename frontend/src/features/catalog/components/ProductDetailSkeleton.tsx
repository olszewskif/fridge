import React from 'react';
import { View, StyleSheet, ScrollView } from 'react-native';
import { SkeletonBox } from '../../../components/Skeleton';
import { Theme } from '../../../constants/theme';

export function ProductDetailSkeleton() {
  return (
    <ScrollView
      showsVerticalScrollIndicator={false}
      contentContainerStyle={styles.container}
    >
      {/* Hero Image Card */}
      <SkeletonBox
        width="100%"
        height={220}
        borderRadius={Theme.radius.xl}
        style={styles.heroCard}
      />

      {/* Expiry Pill */}
      <SkeletonBox
        width={130}
        height={28}
        borderRadius={Theme.radius.full}
        style={{ marginBottom: 16 }}
      />

      {/* Product Title */}
      <SkeletonBox
        width="80%"
        height={28}
        borderRadius={8}
        style={{ marginBottom: 8 }}
      />

      {/* Brand & Barcode Line */}
      <SkeletonBox
        width="45%"
        height={16}
        borderRadius={6}
        style={{ marginBottom: 24 }}
      />

      {/* Details Specs Card */}
      <View style={styles.specsCard}>
        <View style={styles.specRow}>
          <SkeletonBox width={80} height={14} borderRadius={4} />
          <SkeletonBox width={100} height={14} borderRadius={4} />
        </View>
        <View style={styles.specRow}>
          <SkeletonBox width={60} height={14} borderRadius={4} />
          <SkeletonBox width={70} height={14} borderRadius={4} />
        </View>
        <View style={styles.specRow}>
          <SkeletonBox width={90} height={14} borderRadius={4} />
          <SkeletonBox width={120} height={14} borderRadius={4} />
        </View>
      </View>

      {/* Categories Chips */}
      <View style={styles.tagsRow}>
        <SkeletonBox width={70} height={28} borderRadius={Theme.radius.full} />
        <SkeletonBox width={85} height={28} borderRadius={Theme.radius.full} />
        <SkeletonBox width={60} height={28} borderRadius={Theme.radius.full} />
      </View>

      {/* Action Button */}
      <SkeletonBox
        width="100%"
        height={50}
        borderRadius={Theme.radius.lg}
        style={styles.actionBtn}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Theme.spacing.lg,
    paddingTop: Theme.spacing.md,
    paddingBottom: 48,
  },
  heroCard: {
    marginBottom: 20,
  },
  specsCard: {
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    padding: Theme.spacing.md,
    gap: 16,
    marginBottom: 24,
  },
  specRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tagsRow: {
    flexDirection: 'row',
    gap: 8,
    marginBottom: 32,
  },
  actionBtn: {
    marginTop: 8,
  },
});
