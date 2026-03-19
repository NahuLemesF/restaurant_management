import httpClient from './httpClient';

/**
 * Repositorio de Menús — adaptador de infraestructura HTTP.
 * Centraliza todas las operaciones CRUD de la entidad Menu.
 */
const menuRepository = {
  getAll: () => httpClient.get('/menus'),
  getById: (id) => httpClient.get(`/menus/${id}`),
  create: (data) => httpClient.post('/menus', data),
  update: (id, data) => httpClient.put(`/menus/${id}`, data),
  delete: (id) => httpClient.delete(`/menus/${id}`),
};

export default menuRepository;
