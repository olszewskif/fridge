import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
  RefreshControl,
} from 'react-native';
import { useRouter } from 'expo-router';
import {
  Package,
  AlertTriangle,
  Flame,
  Camera,
  ArrowRight,
} from 'lucide-react-native';
import { Theme } from '../../constants/theme';
import { useFridgeStore } from '../../store/useFridgeStore';
import { useFridgeItems } from '../../features/inventory/hooks/useFridgeItems';
import { FridgeHeader } from '../../features/fridges/components/FridgeHeader';
import { FridgeItemCard } from '../../features/inventory/components/FridgeItemCard';
import { FridgeItemCardSkeleton } from '../../features/inventory/components/FridgeItemSkeleton';

export default function HomeScreen() {
  const router = useRouter();
  const { activeFridge } = useFridgeStore();

  const {
    data: itemsPage,
    isLoading,
    refetch,
    isRefetching,
  } = useFridgeItems(activeFridge?.id);

  const items = itemsPage?.content || [];

  const totalItems = itemsPage?.totalElements || items.length;

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const expiredCount = items.filter((item) => {
    if (!item.expiryDate) return false;
    const [y, m, d] = item.expiryDate.split('-').map(Number);
    const exp = new Date(y, m - 1, d);
    return exp < today;
  }).length;

  const expiringSoonCount = items.filter((item) => {
    if (!item.expiryDate) return false;
    const [y, m, d] = item.expiryDate.split('-').map(Number);
    const exp = new Date(y, m - 1, d);
    const diff = Math.ceil((exp.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
    return diff >= 0 && diff <= 3;
  }).length;

  return (
    <View style={styles.container}>
      <FridgeHeader />

      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
        refreshControl={
          <RefreshControl
            refreshing={isRefetching}
            onRefresh={refetch}
            tintColor={Theme.colors.primary}
          />
        }
      >
        {/* Metric Overview Cards: Lean, flattened, and directly filterable */}
        <View style={styles.metricsGrid}>
          {/* Total Items */}
          <TouchableOpacity
            style={styles.metricCard}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/inventory',
                params: { expiryFilter: 'ALL' },
              })
            }
            activeOpacity={0.7}
          >
            <View style={styles.metricHeaderRow}>
              <Text style={styles.metricVal}>{totalItems}</Text>
              <Package size={14} color={Theme.colors.primaryLight} />
            </View>
            <Text style={styles.metricLabel} numberOfLines={1}>
              Total Stock
            </Text>
          </TouchableOpacity>

          {/* Expiring Soon */}
          <TouchableOpacity
            style={[
              styles.metricCard,
              expiringSoonCount > 0 && styles.metricCardExpiring,
            ]}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/inventory',
                params: { expiryFilter: 'EXPIRING_SOON' },
              })
            }
            activeOpacity={0.7}
          >
            <View style={styles.metricHeaderRow}>
              <Text
                style={[
                  styles.metricVal,
                  expiringSoonCount > 0 && { color: Theme.colors.expiringSoon },
                ]}
              >
                {expiringSoonCount}
              </Text>
              <AlertTriangle
                size={14}
                color={expiringSoonCount > 0 ? Theme.colors.expiringSoon : Theme.colors.textMuted}
              />
            </View>
            <Text style={styles.metricLabel} numberOfLines={1}>
              Expiring
            </Text>
          </TouchableOpacity>

          {/* Expired */}
          <TouchableOpacity
            style={[styles.metricCard, expiredCount > 0 && styles.metricCardExpired]}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/inventory',
                params: { expiryFilter: 'EXPIRED' },
              })
            }
            activeOpacity={0.7}
          >
            <View style={styles.metricHeaderRow}>
              <Text
                style={[
                  styles.metricVal,
                  expiredCount > 0 && { color: Theme.colors.expired },
                ]}
              >
                {expiredCount}
              </Text>
              <Flame
                size={14}
                color={expiredCount > 0 ? Theme.colors.expired : Theme.colors.textMuted}
              />
            </View>
            <Text style={styles.metricLabel} numberOfLines={1}>
              Expired
            </Text>
          </TouchableOpacity>
        </View>

        {/* Actionable Expiry Banner (Navigates with direct filter) */}
        {expiredCount > 0 ? (
          <TouchableOpacity
            style={styles.alertBannerExpired}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/inventory',
                params: { expiryFilter: 'EXPIRED' },
              })
            }
            activeOpacity={0.8}
          >
            <View style={styles.alertBannerContent}>
              <Flame size={16} color={Theme.colors.expired} />
              <Text style={styles.alertBannerTextExpired}>
                {expiredCount} {expiredCount === 1 ? 'item has' : 'items have'} expired. Tap to view.
              </Text>
            </View>
            <ArrowRight size={14} color={Theme.colors.expired} />
          </TouchableOpacity>
        ) : expiringSoonCount > 0 ? (
          <TouchableOpacity
            style={styles.alertBannerExpiring}
            onPress={() =>
              router.push({
                pathname: '/(tabs)/inventory',
                params: { expiryFilter: 'EXPIRING_SOON' },
              })
            }
            activeOpacity={0.8}
          >
            <View style={styles.alertBannerContent}>
              <AlertTriangle size={16} color={Theme.colors.expiringSoon} />
              <Text style={styles.alertBannerTextExpiring}>
                {expiringSoonCount} {expiringSoonCount === 1 ? 'item expires' : 'items expire'} soon. Tap to review.
              </Text>
            </View>
            <ArrowRight size={14} color={Theme.colors.expiringSoon} />
          </TouchableOpacity>
        ) : null}

        {/* Recent Items Section */}
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>RECENT ITEMS</Text>
          {totalItems > 0 && (
            <TouchableOpacity
              onPress={() =>
                router.push({
                  pathname: '/(tabs)/inventory',
                  params: { expiryFilter: 'ALL' },
                })
              }
              hitSlop={8}
            >
              <Text style={styles.seeAllLink}>View All ({totalItems})</Text>
            </TouchableOpacity>
          )}
        </View>

        {isLoading ? (
          <View style={styles.recentItemsList}>
            <FridgeItemCardSkeleton />
            <FridgeItemCardSkeleton />
            <FridgeItemCardSkeleton />
          </View>
        ) : items.length === 0 ? (
          <View style={styles.emptyState}>
            <View style={styles.emptyIconCircle}>
              <Package size={36} color={Theme.colors.surfaceBorder} />
            </View>
            <Text style={styles.emptyTitle}>Your fridge is empty</Text>
            <Text style={styles.emptyDesc}>
              Scan barcodes to stock ingredients into {activeFridge?.name || 'your fridge'}.
            </Text>
            <TouchableOpacity
              style={styles.emptyScanBtn}
              onPress={() => router.push('/scan')}
              activeOpacity={0.8}
            >
              <Camera size={18} color="#fff" />
              <Text style={styles.emptyScanBtnText}>Scan First Item</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View style={styles.recentItemsList}>
            {items.slice(0, 3).map((item, index) => (
              <FridgeItemCard key={item.id} item={item} index={index} />
            ))}
          </View>
        )}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Theme.colors.background,
  },
  scrollContent: {
    paddingHorizontal: Theme.spacing.lg,
    paddingTop: Theme.spacing.xs,
    paddingBottom: 110, // tab bar clearance
  },
  metricsGrid: {
    flexDirection: 'row',
    gap: 8,
    marginBottom: Theme.spacing.md,
  },
  metricCard: {
    flex: 1,
    backgroundColor: Theme.colors.surface,
    paddingVertical: 10,
    paddingHorizontal: 12,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    justifyContent: 'space-between',
    minHeight: 58,
  },
  metricCardExpiring: {
    borderColor: 'rgba(245, 158, 11, 0.4)',
    backgroundColor: 'rgba(245, 158, 11, 0.08)',
  },
  metricCardExpired: {
    borderColor: 'rgba(239, 68, 68, 0.4)',
    backgroundColor: 'rgba(239, 68, 68, 0.08)',
  },
  metricHeaderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: 2,
  },
  metricVal: {
    fontSize: 20,
    fontWeight: '800',
    color: Theme.colors.text,
  },
  metricLabel: {
    fontSize: 11,
    color: Theme.colors.textSecondary,
    fontWeight: '600',
  },
  alertBannerExpired: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: Theme.colors.expiredBg,
    borderWidth: 1,
    borderColor: 'rgba(239, 68, 68, 0.3)',
    paddingHorizontal: Theme.spacing.md,
    paddingVertical: 10,
    borderRadius: Theme.radius.md,
    marginBottom: Theme.spacing.md,
  },
  alertBannerExpiring: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: Theme.colors.expiringSoonBg,
    borderWidth: 1,
    borderColor: 'rgba(245, 158, 11, 0.3)',
    paddingHorizontal: Theme.spacing.md,
    paddingVertical: 10,
    borderRadius: Theme.radius.md,
    marginBottom: Theme.spacing.md,
  },
  alertBannerContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    flex: 1,
  },
  alertBannerTextExpired: {
    color: Theme.colors.expired,
    fontSize: 12,
    fontWeight: '600',
    flex: 1,
  },
  alertBannerTextExpiring: {
    color: Theme.colors.expiringSoon,
    fontSize: 12,
    fontWeight: '600',
    flex: 1,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: Theme.spacing.sm,
    marginBottom: Theme.spacing.md,
  },
  sectionTitle: {
    fontSize: 12,
    fontWeight: '800',
    color: Theme.colors.textMuted,
    letterSpacing: 1,
  },
  seeAllLink: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.primaryLight,
  },
  recentItemsList: {
    gap: 10,
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 44,
    gap: Theme.spacing.sm,
  },
  emptyIconCircle: {
    width: 68,
    height: 68,
    borderRadius: 34,
    backgroundColor: Theme.colors.surface,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 4,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
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
    maxWidth: 270,
    lineHeight: 20,
  },
  emptyScanBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.sm,
  },
  emptyScanBtnText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 14,
  },
});
