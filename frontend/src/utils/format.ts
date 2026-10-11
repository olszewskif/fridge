export interface NetQuantityLike {
  amount: number;
  unit: string;
}

/**
 * Human-friendly formatter for product net quantities.
 * - Grams & Milliliters:
 *   - If >= 10: rounds to nearest whole number (e.g. 396.893g -> 397 g)
 *   - If < 10: keeps 1 decimal place (e.g. 2.5 g)
 * - Liters & Kilograms:
 *   - Keeps up to 2 decimal places, dropping trailing zeroes (e.g. 1.5 L, 0.75 L, 1 kg)
 */
export function formatQuantity(amount?: number | null, unit?: string | null): string {
  if (amount == null) return '';
  if (!unit) return String(amount);

  const u = unit.toLowerCase();

  // Grams & Milliliters
  if (u === 'grams' || u === 'g' || u === 'milliliters' || u === 'ml') {
    const isGrams = u.startsWith('g');
    const displayUnit = isGrams ? 'g' : 'ml';
    const val = amount >= 10 ? Math.round(amount) : Math.round(amount * 10) / 10;
    return `${val} ${displayUnit}`;
  }

  // Liters & Kilograms
  if (u === 'liters' || u === 'l' || u === 'kilograms' || u === 'kg') {
    const displayUnit = u.startsWith('k') ? 'kg' : 'L';
    const val = parseFloat(amount.toFixed(2));
    return `${val} ${displayUnit}`;
  }

  // Pieces or fallback
  if (u === 'pieces' || u === 'pcs') {
    const val = Math.round(amount);
    return `${val} ${val === 1 ? 'pc' : 'pcs'}`;
  }

  return `${amount} ${unit}`;
}

export function formatNetQuantity(netQuantity?: NetQuantityLike | null): string {
  if (!netQuantity || netQuantity.amount == null) return '';
  return formatQuantity(netQuantity.amount, netQuantity.unit);
}
