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
  ShieldCheck,
  Plus,
} from 'lucide-react-native';
import { Theme } from '../../constants/theme';
import { useFridgeStore } from '../../store/useFridgeStore';
import { useFridgeItems } from '../../features/inventory/hooks/useFridgeItems';
import { FridgeHeader } from '../../features/fridges/components/FridgeHeader';
import { FridgeItemCard } from '../../features/inventory/components/FridgeItemCard';

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

  // Metric computations
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
        {/* Metric Overview Cards */}
        <View style={styles.metricsGrid}>
          {/* Total Items */}
          <View style={styles.metricCard}>
            <View style={styles.metricIconWrap}>
              <Package size={20} color={Theme.colors.primary} />
            </View>
            <Text style={styles.metricVal}>{totalItems}</Text>
            <Text style={styles.metricLabel}>Total Items</Text>
          </View>

          {/* Expiring Soon */}
          <View style={[styles.metricCard, expiringSoonCount > 0 && styles.metricCardExpiring]}>
            <View
              style={[
                styles.metricIconWrap,
                expiringSoonCount > 0 && { backgroundColor: Theme.colors.expiringSoonBg },
              ]}
            >
              <AlertTriangle
                size={20}
                color={expiringSoonCount > 0 ? Theme.colors.expiringSoon : Theme.colors.textMuted}
              />
            </View>
            <Text
              style={[
                styles.metricVal,
                expiringSoonCount > 0 && { color: Theme.colors.expiringSoon },
              ]}
            >
              {expiringSoonCount}
            </Text>
            <Text style={styles.metricLabel}>Expiring Soon</Text>
          </View>

          {/* Expired */}
          <View style={[styles.metricCard, expiredCount > 0 && styles.metricCardExpired]}>
            <View
              style={[
                styles.metricIconWrap,
                expiredCount > 0 && { backgroundColor: Theme.colors.expiredBg },
              ]}
            >
              <Flame
                size={20}
                color={expiredCount > 0 ? Theme.colors.expired : Theme.colors.textMuted}
              />
            </View>
            <Text style={[styles.metricVal, expiredCount > 0 && { color: Theme.colors.expired }]}>
              {expiredCount}
            </Text>
            <Text style={styles.metricLabel}>Expired</Text>
          </View>
        </View>

        {/* Action Banners */}
        {expiredCount > 0 && (
          <TouchableOpacity
            style={styles.alertBanner}
            onPress={() => router.push('/(tabs)/inventory')}
            activeOpacity={0.8}
          >
            <View style={styles.alertBannerContent}>
              <Flame size={20} color={Theme.colors.expired} />
              <Text style={styles.alertBannerText}>
                {expiredCount} {expiredCount === 1 ? 'item has' : 'items have'} expired! Check
                inventory.
              </Text>
            </View>
            <ArrowRight size={18} color={Theme.colors.expired} />
          </TouchableOpacity>
        )}

        {/* Quick Launch Buttons */}
        <View style={styles.quickLaunchRow}>
          <TouchableOpacity
            style={styles.launchCardPrimary}
            onPress={() => router.push('/scan')}
            activeOpacity={0.8}
          >
            <View style={styles.launchIconCircle}>
              <Camera size={22} color="#fff" />
            </View>
            <View>
              <Text style={styles.launchTitlePrimary}>Scan Barcode</Text>
              <Text style={styles.launchSubtitlePrimary}>Quickly add groceries</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.launchCardSecondary}
            onPress={() => router.push('/(tabs)/inventory')}
            activeOpacity={0.8}
          >
            <View style={styles.launchIconCircleSec}>
              <Package size={22} color={Theme.colors.primary} />
            </View>
            <View>
              <Text style={styles.launchTitleSecondary}>Open Inventory</Text>
              <Text style={styles.launchSubtitleSecondary}>Search & filter items</Text>
            </View>
          </TouchableOpacity>
        </View>

        {/* Recent Items Section */}
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>RECENT ITEMS IN FRIDGE</Text>
          <TouchableOpacity onPress={() => router.push('/(tabs)/inventory')}>
            <Text style={styles.seeAllLink}>View All ({totalItems})</Text>
          </TouchableOpacity>
        </View>

        {isLoading ? (
          <ActivityIndicator color={Theme.colors.primary} style={{ marginVertical: 32 }} />
        ) : items.length === 0 ? (
          <View style={styles.emptyState}>
            <Package size={40} color={Theme.colors.surfaceBorder} />
            <Text style={styles.emptyTitle}>This fridge is empty</Text>
            <Text style={styles.emptyDesc}>
              Scan barcodes to populate your fridge with ingredients!
            </Text>
            <TouchableOpacity style={styles.emptyScanBtn} onPress={() => router.push('/scan')}>
              <Camera size={18} color="#fff" />
              <Text style={styles.emptyScanBtnText}>Scan First Item</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <View style={styles.recentItemsList}>
            {items.slice(0, 5).map((item) => (
              <FridgeItemCard key={item.id} item={item} />
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
    paddingBottom: 100, // accommodate bottom tab bar
  },
  metricsGrid: {
    flexDirection: 'row',
    gap: Theme.spacing.sm,
    marginBottom: Theme.spacing.md,
  },
  metricCard: {
    flex: 1,
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    alignItems: 'center',
    gap: 4,
  },
  metricCardExpiring: {
    borderColor: 'rgba(245, 158, 11, 0.4)',
  },
  metricCardExpired: {
    borderColor: 'rgba(239, 68, 68, 0.4)',
  },
  metricIconWrap: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: 'rgba(64, 0, 255, 0.1)',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 4,
  },
  metricVal: {
    fontSize: 22,
    fontWeight: '800',
    color: Theme.colors.text,
  },
  metricLabel: {
    fontSize: 11,
    color: Theme.colors.textSecondary,
    fontWeight: '600',
    textAlign: 'center',
  },
  alertBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    backgroundColor: Theme.colors.expiredBg,
    borderWidth: 1,
    borderColor: Theme.colors.expired,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    marginBottom: Theme.spacing.md,
  },
  alertBannerContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    flex: 1,
  },
  alertBannerText: {
    color: Theme.colors.expired,
    fontSize: 13,
    fontWeight: '600',
    flex: 1,
  },
  quickLaunchRow: {
    gap: Theme.spacing.sm,
    marginBottom: Theme.spacing.lg,
  },
  launchCardPrimary: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.primary,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    gap: Theme.spacing.md,
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.4,
    shadowRadius: 10,
    elevation: 6,
  },
  launchIconCircle: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(255, 255, 255, 0.2)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  launchTitlePrimary: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '700',
  },
  launchSubtitlePrimary: {
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: 13,
  },
  launchCardSecondary: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Theme.colors.surface,
    padding: Theme.spacing.md,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.md,
  },
  launchIconCircleSec: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(64, 0, 255, 0.12)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  launchTitleSecondary: {
    color: Theme.colors.text,
    fontSize: 16,
    fontWeight: '700',
  },
  launchSubtitleSecondary: {
    color: Theme.colors.textSecondary,
    fontSize: 13,
  },
  sectionHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginBottom: Theme.spacing.md,
    marginTop: Theme.spacing.xs,
  },
  sectionTitle: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.textSecondary,
    letterSpacing: 0.8,
  },
  seeAllLink: {
    fontSize: 13,
    fontWeight: '600',
    color: Theme.colors.primary,
  },
  recentItemsList: {
    gap: Theme.spacing.xs,
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    padding: Theme.spacing.xl,
    backgroundColor: Theme.colors.surface,
    borderRadius: Theme.radius.lg,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    gap: Theme.spacing.sm,
    marginTop: Theme.spacing.sm,
  },
  emptyTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: Theme.colors.text,
  },
  emptyDesc: {
    fontSize: 13,
    color: Theme.colors.textSecondary,
    textAlign: 'center',
    maxWidth: 240,
    lineHeight: 18,
  },
  emptyScanBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Theme.spacing.sm,
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.sm,
  },
  emptyScanBtnText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 14,
  },
});
