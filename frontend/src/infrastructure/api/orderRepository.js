import httpClient from './httpClient';

/**
 * Repositorio de Órdenes — adaptador de infraestructura HTTP.
 * Centraliza las operaciones de creación y consulta de órdenes.
 */
const orderRepository = {
  getAll: () => httpClient.get('/orders'),
  getById: (id) => httpClient.get(`/orders/${id}`),
  create: (data) => httpClient.post('/orders', data),
};

export default orderRepository;
