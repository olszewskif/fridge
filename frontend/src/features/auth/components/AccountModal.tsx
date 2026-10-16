import React from 'react';
import {
  Modal,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { X, LogOut, Shield, Mail, User as UserIcon } from 'lucide-react-native';
import { Theme } from '../../../constants/theme';
import { useAuthStore } from '../../../store/useAuthStore';
import { useLogout } from '../hooks/useAuth';

interface Props {
  visible: boolean;
  onClose: () => void;
}

export function AccountModal({ visible, onClose }: Props) {
  const { user } = useAuthStore();
  const logout = useLogout();

  const handleSignOut = () => {
    Alert.alert(
      'Sign Out',
      'Are you sure you want to sign out of your account?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Sign Out',
          style: 'destructive',
          onPress: async () => {
            onClose();
            await logout();
          },
        },
      ]
    );
  };

  const initial = (user?.displayName || user?.email || 'U')[0].toUpperCase();
  const roleName = user?.role?.replace('ROLE_', '') || 'USER';

  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <View style={styles.overlay}>
        <TouchableOpacity style={styles.backdrop} activeOpacity={1} onPress={onClose} />

        <View style={styles.card}>
          {/* Header */}
          <View style={styles.header}>
            <Text style={styles.headerTitle}>Account Profile</Text>
            <TouchableOpacity onPress={onClose} hitSlop={12} style={styles.closeBtn}>
              <X size={20} color={Theme.colors.textSecondary} />
            </TouchableOpacity>
          </View>

          {/* Profile Card */}
          <View style={styles.profileSection}>
            <View style={styles.avatarCircle}>
              <Text style={styles.avatarText}>{initial}</Text>
            </View>
            <Text style={styles.displayName}>{user?.displayName || 'User'}</Text>
            <View style={styles.roleBadge}>
              <Shield size={12} color={Theme.colors.primaryLight} />
              <Text style={styles.roleText}>{roleName}</Text>
            </View>
          </View>

          {/* Details */}
          <View style={styles.detailsSection}>
            <View style={styles.detailRow}>
              <View style={styles.detailIconWrapper}>
                <Mail size={16} color={Theme.colors.textSecondary} />
              </View>
              <View style={styles.detailInfo}>
                <Text style={styles.detailLabel}>Email</Text>
                <Text style={styles.detailValue}>{user?.email || 'Not available'}</Text>
              </View>
            </View>

            <View style={styles.detailRow}>
              <View style={styles.detailIconWrapper}>
                <UserIcon size={16} color={Theme.colors.textSecondary} />
              </View>
              <View style={styles.detailInfo}>
                <Text style={styles.detailLabel}>User ID</Text>
                <Text style={styles.detailValue} numberOfLines={1}>
                  {user?.id || 'Unknown'}
                </Text>
              </View>
            </View>
          </View>

          {/* Sign Out Button */}
          <TouchableOpacity style={styles.signOutBtn} onPress={handleSignOut} activeOpacity={0.8}>
            <LogOut size={18} color={Theme.colors.expired} />
            <Text style={styles.signOutText}>Sign Out</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: Theme.colors.overlay,
    paddingHorizontal: Theme.spacing.lg,
  },
  backdrop: {
    ...StyleSheet.absoluteFill,
  },
  card: {
    width: '100%',
    maxWidth: 380,
    backgroundColor: Theme.colors.surface,
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
    borderRadius: Theme.radius.lg,
    padding: Theme.spacing.lg,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.5,
    shadowRadius: 20,
    elevation: 10,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Theme.spacing.md,
  },
  headerTitle: {
    fontSize: 16,
    fontWeight: '700',
    color: Theme.colors.textSecondary,
    textTransform: 'uppercase',
    letterSpacing: 0.8,
  },
  closeBtn: {
    padding: 4,
  },
  profileSection: {
    alignItems: 'center',
    paddingVertical: Theme.spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: Theme.colors.surfaceBorder,
  },
  avatarCircle: {
    width: 64,
    height: 64,
    borderRadius: 32,
    backgroundColor: Theme.colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: Theme.spacing.sm,
    shadowColor: Theme.colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
  },
  avatarText: {
    fontSize: 26,
    fontWeight: '800',
    color: '#fff',
  },
  displayName: {
    fontSize: 20,
    fontWeight: '800',
    color: Theme.colors.text,
    letterSpacing: -0.3,
    marginBottom: 6,
  },
  roleBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 5,
    backgroundColor: 'rgba(64, 0, 255, 0.15)',
    borderWidth: 1,
    borderColor: 'rgba(89, 36, 255, 0.3)',
    borderRadius: Theme.radius.full,
    paddingHorizontal: 10,
    paddingVertical: 3,
  },
  roleText: {
    fontSize: 11,
    fontWeight: '700',
    color: Theme.colors.primaryLight,
    letterSpacing: 0.5,
  },
  detailsSection: {
    paddingVertical: Theme.spacing.md,
    gap: Theme.spacing.md,
  },
  detailRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  detailIconWrapper: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: Theme.colors.surfaceElevated,
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Theme.colors.surfaceBorder,
  },
  detailInfo: {
    flex: 1,
  },
  detailLabel: {
    fontSize: 11,
    fontWeight: '600',
    color: Theme.colors.textMuted,
    textTransform: 'uppercase',
  },
  detailValue: {
    fontSize: 14,
    fontWeight: '600',
    color: Theme.colors.text,
    marginTop: 1,
  },
  signOutBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    backgroundColor: Theme.colors.expiredBg,
    borderWidth: 1,
    borderColor: 'rgba(239, 68, 68, 0.3)',
    borderRadius: Theme.radius.md,
    height: 48,
    marginTop: Theme.spacing.sm,
  },
  signOutText: {
    fontSize: 15,
    fontWeight: '700',
    color: Theme.colors.expired,
  },
});
