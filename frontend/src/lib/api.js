import axios from 'axios';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getClients = () => api.get('/clients');
export const createClient = (data) => api.post('/clients', data);
export const deleteClient = (id) => api.delete(`/clients/${id}`);

export const getMenus = () => api.get('/menus');
export const createMenu = (data) => api.post('/menus', data);
export const updateMenu = (id, data) => api.put(`/menus/${id}`, data);

export const getDishes = () => api.get('/dishes');
export const createDish = (data) => api.post('/dishes', data);
export const updateDish = (id, data) => api.put(`/dishes/${id}`, data);
export const deleteDish = (id) => api.delete(`/dishes/${id}`);

export const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  const response = await api.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return response;
};

export const createOrder = (data) => api.post('/orders', data);
export const getOrders = () => api.get('/orders');

export default api;
