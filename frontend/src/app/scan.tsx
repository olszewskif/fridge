import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator } from 'react-native';
import { CameraView, useCameraPermissions } from 'expo-camera';
import { useRouter } from 'expo-router';
import { X, Zap, RefreshCw, Barcode as BarcodeIcon } from 'lucide-react-native';
import { Theme } from '../constants/theme';
import { useScanProduct } from '../features/catalog/hooks/useScanProduct';
import { ProductResponse } from '../features/catalog/types/catalog.types';
import { ConfirmAddItemSheet } from '../features/scanner/components/ConfirmAddItemSheet';

export default function ScanScreen() {
  const router = useRouter();
  const [permission, requestPermission] = useCameraPermissions();
  const scanProductMutation = useScanProduct();

  const [scannedProduct, setScannedProduct] = useState<ProductResponse | null>(null);
  const [confirmVisible, setConfirmVisible] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const [lastScannedBarcode, setLastScannedBarcode] = useState<string | null>(null);
  const [errorInfo, setErrorInfo] = useState<string | null>(null);

  if (!permission) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator color={Theme.colors.primary} />
      </View>
    );
  }

  if (!permission.granted) {
    return (
      <View style={styles.centerContainer}>
        <BarcodeIcon size={48} color={Theme.colors.primary} />
        <Text style={styles.permissionTitle}>Camera Access Required</Text>
        <Text style={styles.permissionDesc}>
          We need access to your camera to scan barcodes on food items.
        </Text>
        <TouchableOpacity style={styles.grantBtn} onPress={requestPermission}>
          <Text style={styles.grantBtnText}>Grant Permission</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.closeSimpleBtn} onPress={() => router.back()}>
          <Text style={styles.closeSimpleBtnText}>Cancel</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const handleBarcodeScanned = async ({ data }: { data: string }) => {
    if (isProcessing || confirmVisible || errorInfo || data === lastScannedBarcode) return;

    setIsProcessing(true);
    setLastScannedBarcode(data);
    setErrorInfo(null);

    try {
      const product = await scanProductMutation.mutateAsync(data);
      setScannedProduct(product);
      setConfirmVisible(true);
    } catch (err: any) {
      const msg = err.response?.status === 404
        ? `No product found for barcode: ${data}`
        : 'Network error: could not reach backend server';
      setErrorInfo(msg);
    } finally {
      setIsProcessing(false);
    }
  };

  const resetScanner = () => {
    setErrorInfo(null);
    setLastScannedBarcode(null);
  };

  const handleAddedSuccess = () => {
    setConfirmVisible(false);
    router.replace('/(tabs)/inventory');
  };

  return (
    <View style={styles.container}>
      <CameraView
        style={StyleSheet.absoluteFill}
        facing="back"
        barcodeScannerSettings={{
          barcodeTypes: ['ean13', 'ean8', 'upc_a', 'upc_e', 'qr'],
        }}
        onBarcodeScanned={
          isProcessing || confirmVisible || !!errorInfo ? undefined : handleBarcodeScanned
        }
      />

      {/* Top Controls Overlay */}
      <View style={styles.topOverlay}>
        <TouchableOpacity
          style={styles.circleBtn}
          onPress={() => router.back()}
          hitSlop={12}
        >
          <X size={22} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.topTitle}>Scan Barcode</Text>
        <View style={{ width: 44 }} />
      </View>

      {/* Viewfinder Target Graphic */}
      <View style={styles.targetContainer} pointerEvents="none">
        <View style={styles.targetBox}>
          <View style={[styles.corner, styles.cornerTL]} />
          <View style={[styles.corner, styles.cornerTR]} />
          <View style={[styles.corner, styles.cornerBL]} />
          <View style={[styles.corner, styles.cornerBR]} />

          {isProcessing && (
            <View style={styles.processingIndicator}>
              <ActivityIndicator color={Theme.colors.primary} size="large" />
              <Text style={styles.processingText}>Looking up product...</Text>
            </View>
          )}
        </View>
        <Text style={styles.helperText}>Point camera at barcode on food item</Text>
      </View>

      {/* Error Banner & Try Again */}
      {errorInfo && (
        <View style={styles.errorOverlay}>
          <Text style={styles.errorText}>{errorInfo}</Text>
          <TouchableOpacity style={styles.tryAgainBtn} onPress={resetScanner} activeOpacity={0.8}>
            <Text style={styles.tryAgainBtnText}>Tap to Scan Again</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* Confirmation & Details Sheet */}
      <ConfirmAddItemSheet
        visible={confirmVisible}
        product={scannedProduct}
        onClose={() => {
          setConfirmVisible(false);
          setLastScannedBarcode(null);
        }}
        onSuccess={handleAddedSuccess}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  centerContainer: {
    flex: 1,
    backgroundColor: Theme.colors.background,
    alignItems: 'center',
    justifyContent: 'center',
    padding: Theme.spacing.xl,
    gap: Theme.spacing.md,
  },
  permissionTitle: {
    fontSize: 20,
    fontWeight: '700',
    color: Theme.colors.text,
    textAlign: 'center',
  },
  permissionDesc: {
    fontSize: 14,
    color: Theme.colors.textSecondary,
    textAlign: 'center',
    lineHeight: 20,
  },
  grantBtn: {
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 24,
    paddingVertical: 14,
    borderRadius: Theme.radius.md,
    marginTop: Theme.spacing.md,
  },
  grantBtnText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 15,
  },
  closeSimpleBtn: {
    paddingVertical: 10,
  },
  closeSimpleBtnText: {
    color: Theme.colors.textMuted,
    fontSize: 14,
  },
  topOverlay: {
    position: 'absolute',
    top: 54,
    left: 0,
    right: 0,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: Theme.spacing.lg,
    zIndex: 10,
  },
  circleBtn: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: 'rgba(0, 0, 0, 0.6)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  topTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: '#fff',
  },
  targetContainer: {
    ...StyleSheet.absoluteFill,
    alignItems: 'center',
    justifyContent: 'center',
  },
  targetBox: {
    width: 270,
    height: 220,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: 'rgba(255, 255, 255, 0.2)',
    backgroundColor: 'rgba(0, 0, 0, 0.15)',
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
  },
  corner: {
    position: 'absolute',
    width: 24,
    height: 24,
    borderColor: Theme.colors.primary,
  },
  cornerTL: {
    top: -2,
    left: -2,
    borderTopWidth: 4,
    borderLeftWidth: 4,
    borderTopLeftRadius: 16,
  },
  cornerTR: {
    top: -2,
    right: -2,
    borderTopWidth: 4,
    borderRightWidth: 4,
    borderTopRightRadius: 16,
  },
  cornerBL: {
    bottom: -2,
    left: -2,
    borderBottomWidth: 4,
    borderLeftWidth: 4,
    borderBottomLeftRadius: 16,
  },
  cornerBR: {
    bottom: -2,
    right: -2,
    borderBottomWidth: 4,
    borderRightWidth: 4,
    borderBottomRightRadius: 16,
  },
  processingIndicator: {
    alignItems: 'center',
    gap: Theme.spacing.sm,
  },
  processingText: {
    color: '#fff',
    fontWeight: '600',
    fontSize: 14,
  },
  helperText: {
    marginTop: Theme.spacing.lg,
    color: 'rgba(255, 255, 255, 0.8)',
    fontSize: 14,
    fontWeight: '500',
    backgroundColor: 'rgba(0, 0, 0, 0.6)',
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: Theme.radius.full,
  },
  errorOverlay: {
    position: 'absolute',
    bottom: 50,
    left: 20,
    right: 20,
    backgroundColor: '#18181b',
    borderWidth: 1,
    borderColor: Theme.colors.expired,
    padding: 16,
    borderRadius: 16,
    alignItems: 'center',
    gap: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 10,
  },
  errorText: {
    color: '#f4f4f5',
    fontSize: 14,
    fontWeight: '600',
    textAlign: 'center',
  },
  tryAgainBtn: {
    backgroundColor: Theme.colors.primary,
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 10,
  },
  tryAgainBtnText: {
    color: '#fff',
    fontWeight: '700',
    fontSize: 14,
  },
});
