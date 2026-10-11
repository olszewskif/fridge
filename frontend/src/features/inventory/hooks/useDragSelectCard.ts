import { useRef } from 'react';
import { PanResponder, GestureResponderEvent, PanResponderGestureState } from 'react-native';

interface UseDragSelectCardProps {
  itemId: string;
  index: number;
  isSelectionMode?: boolean;
  onPress: () => void;
  onDragStart?: (index: number) => void;
  onDragMove?: (index: number, dx: number, dy: number) => void;
  onDragEnd?: () => void;
}

export function useDragSelectCard({
  itemId,
  index,
  isSelectionMode,
  onPress,
  onDragStart,
  onDragMove,
  onDragEnd,
}: UseDragSelectCardProps) {
  const callbacksRef = useRef({
    itemId,
    index,
    isSelectionMode,
    onPress,
    onDragStart,
    onDragMove,
    onDragEnd,
  });
  callbacksRef.current = {
    itemId,
    index,
    isSelectionMode,
    onPress,
    onDragStart,
    onDragMove,
    onDragEnd,
  };

  const longPressTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const isDraggingRef = useRef(false);
  const hasMovedSignificantlyRef = useRef(false);

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onStartShouldSetPanResponderCapture: () => false,

      onMoveShouldSetPanResponder: () => isDraggingRef.current,
      onMoveShouldSetPanResponderCapture: () => isDraggingRef.current,

      // CRITICAL: Block FlatList / native ScrollView from cancelling the gesture once drag-selection starts
      onPanResponderTerminationRequest: () => !isDraggingRef.current,

      onPanResponderGrant: () => {
        isDraggingRef.current = false;
        hasMovedSignificantlyRef.current = false;

        // Long press threshold: 200ms
        longPressTimerRef.current = setTimeout(() => {
          if (!hasMovedSignificantlyRef.current) {
            isDraggingRef.current = true;
            callbacksRef.current.onDragStart?.(callbacksRef.current.index);
          }
        }, 200);
      },

      onPanResponderMove: (_: GestureResponderEvent, gestureState: PanResponderGestureState) => {
        const dist = Math.hypot(gestureState.dx, gestureState.dy);
        if (dist > 8) {
          hasMovedSignificantlyRef.current = true;
          if (!isDraggingRef.current && longPressTimerRef.current) {
            clearTimeout(longPressTimerRef.current);
            longPressTimerRef.current = null;
          }
        }

        if (isDraggingRef.current) {
          callbacksRef.current.onDragMove?.(
            callbacksRef.current.index,
            gestureState.dx,
            gestureState.dy
          );
        }
      },

      onPanResponderRelease: () => {
        if (longPressTimerRef.current) {
          clearTimeout(longPressTimerRef.current);
          longPressTimerRef.current = null;
        }

        if (isDraggingRef.current) {
          isDraggingRef.current = false;
          callbacksRef.current.onDragEnd?.();
        } else if (!hasMovedSignificantlyRef.current) {
          // Clean tap without drag
          callbacksRef.current.onPress();
        }
      },

      onPanResponderTerminate: () => {
        if (longPressTimerRef.current) {
          clearTimeout(longPressTimerRef.current);
          longPressTimerRef.current = null;
        }
        if (isDraggingRef.current) {
          isDraggingRef.current = false;
          callbacksRef.current.onDragEnd?.();
        }
      },
    })
  ).current;

  return panResponder;
}
