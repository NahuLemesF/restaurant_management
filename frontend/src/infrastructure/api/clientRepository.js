import httpClient from './httpClient';

/**
 * Repositorio de Clientes — adaptador de infraestructura HTTP.
 * Centraliza todas las operaciones CRUD de la entidad Client.
 */
const clientRepository = {
  getAll: () => httpClient.get('/clients'),
  getById: (id) => httpClient.get(`/clients/${id}`),
  create: (data) => httpClient.post('/clients', data),
  update: (id, data) => httpClient.put(`/clients/${id}`, data),
  delete: (id) => httpClient.delete(`/clients/${id}`),
};

export default clientRepository;
