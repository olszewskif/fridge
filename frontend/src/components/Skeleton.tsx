import React, { useEffect, useRef } from 'react';
import { Animated, ViewStyle, StyleSheet, StyleProp } from 'react-native';
import { Theme } from '../constants/theme';

interface SkeletonBoxProps {
  width?: number | `${number}%` | 'auto';
  height?: number | `${number}%`;
  borderRadius?: number;
  style?: StyleProp<ViewStyle>;
}

export function SkeletonBox({
  width = '100%',
  height = 16,
  borderRadius = Theme.radius.sm,
  style,
}: SkeletonBoxProps) {
  const opacityAnim = useRef(new Animated.Value(0.35)).current;

  useEffect(() => {
    const pulse = Animated.loop(
      Animated.sequence([
        Animated.timing(opacityAnim, {
          toValue: 0.85,
          duration: 750,
          useNativeDriver: true,
        }),
        Animated.timing(opacityAnim, {
          toValue: 0.35,
          duration: 750,
          useNativeDriver: true,
        }),
      ])
    );

    pulse.start();

    return () => pulse.stop();
  }, [opacityAnim]);

  return (
    <Animated.View
      style={[
        styles.skeleton,
        {
          width: width as any,
          height: height as any,
          borderRadius,
          opacity: opacityAnim,
        },
        style,
      ]}
    />
  );
}

const styles = StyleSheet.create({
  skeleton: {
    backgroundColor: Theme.colors.surfaceElevated,
    borderColor: Theme.colors.surfaceBorder,
    borderWidth: 1,
  },
});
