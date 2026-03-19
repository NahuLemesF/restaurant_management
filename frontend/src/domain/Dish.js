/**
 * Entidad de dominio: Plato.
 * Reglas de negocio puras — sin dependencias externas.
 */

/** Tipos de platos disponibles */
export const DISH_TYPES = {
  STANDARD: 'Standard',
  POPULAR: 'Popular',
};

/**
 * Retorna el label y clase de color para el tipo de plato.
 * @param {string} dishType
 * @returns {{ label: string, color: string }}
 */
export function getDishTypeInfo(dishType) {
  switch (dishType) {
    case DISH_TYPES.POPULAR:
      return { label: 'Popular', color: 'bg-error-container/20 text-error border-error/20' };
    default:
      return { label: 'Estándar', color: 'bg-surface-container text-on-surface-variant border-outline-variant/20' };
  }
}

/**
 * Formatea el precio de un plato con separador de miles.
 * @param {number} price
 * @returns {string}
 */
export function formatPrice(price) {
  return price?.toLocaleString('es-AR', { minimumFractionDigits: 2 }) ?? '0.00';
}
