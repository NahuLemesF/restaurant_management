/**
 * Entidad de dominio: Cliente.
 * Reglas de negocio puras — sin dependencias externas.
 */

/**
 * Determina si un cliente es frecuente.
 * @param {{clientType: string}} client
 * @returns {boolean}
 */
export function isFrecuente(client) {
  return client?.clientType === 'Frecuente';
}

/**
 * Retorna las iniciales del nombre y apellido del cliente.
 * @param {{name: string, lastName: string}} client
 * @returns {string}
 */
export function getInitials(client) {
  const first = client?.name?.charAt(0)?.toUpperCase() || '';
  const last = client?.lastName?.charAt(0)?.toUpperCase() || '';
  return `${first}${last}` || '?';
}

/**
 * Calcula el porcentaje de clientes frecuentes sobre el total.
 * @param {Array<{clientType: string}>} clients
 * @returns {string} Porcentaje con 1 decimal
 */
export function frequentPercentage(clients) {
  if (!clients.length) return '0.0';
  const count = clients.filter(isFrecuente).length;
  return ((count / clients.length) * 100).toFixed(1);
}
