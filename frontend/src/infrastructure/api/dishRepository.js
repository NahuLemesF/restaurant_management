import httpClient from './httpClient';

/**
 * Repositorio de Platos — adaptador de infraestructura HTTP.
 * Centraliza todas las operaciones CRUD de la entidad Dish,
 * incluyendo la subida de imagen (multipart).
 */
const dishRepository = {
  getAll: () => httpClient.get('/dishes'),
  getById: (id) => httpClient.get(`/dishes/${id}`),
  create: (data) => httpClient.post('/dishes', data),
  update: (id, data) => httpClient.put(`/dishes/${id}`, data),
  delete: (id) => httpClient.delete(`/dishes/${id}`),

  uploadImage: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return httpClient.post('/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};

export default dishRepository;
