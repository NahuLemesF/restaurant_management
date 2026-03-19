/**
 * Entidad de dominio: Artículo del carrito.
 * Lógica pura de negocio — sin dependencias externas.
 */

/** Porcentaje de descuento aplicado a clientes frecuentes */
export const FREQUENT_DISCOUNT_RATE = 0.10;

/** Umbral de pedidos para ser considerado cliente frecuente */
export const FREQUENT_ORDER_THRESHOLD = 10;

/**
 * Calcula el subtotal del carrito sin descuento.
 * @param {Array<{price: number, quantity?: number}>} items
 * @returns {number}
 */
export function computeSubtotal(items) {
  return items.reduce((acc, item) => acc + item.price * (item.quantity || 1), 0);
}

/**
 * Calcula el descuento para un cliente frecuente.
 * @param {number} subtotal
 * @param {boolean} isFrecuente
 * @returns {number}
 */
export function computeDiscount(subtotal, isFrecuente) {
  return isFrecuente ? subtotal * FREQUENT_DISCOUNT_RATE : 0;
}

/**
 * Calcula el total final después del descuento.
 * @param {number} subtotal
 * @param {number} discount
 * @returns {number}
 */
export function computeTotal(subtotal, discount) {
  return subtotal - discount;
}

/**
 * Agrega un plato al carrito. Si ya existe, incrementa cantidad.
 * @param {Array} cart
 * @param {object} dish
 * @returns {Array}
 */
export function addToCart(cart, dish) {
  const existing = cart.find(item => item.id === dish.id);
  if (existing) {
    return cart.map(item =>
      item.id === dish.id
        ? { ...item, quantity: (item.quantity || 1) + 1 }
        : item
    );
  }
  return [...cart, { ...dish, quantity: 1 }];
}

/**
 * Elimina completamente un item del carrito por id.
 * @param {Array} cart
 * @param {number} dishId
 * @returns {Array}
 */
export function removeFromCart(cart, dishId) {
  return cart.filter(item => item.id !== dishId);
}
