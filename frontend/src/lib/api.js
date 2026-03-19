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

export const getDishes = () => api.get('/dishes');
export const createDish = (data) => api.post('/dishes', data);

export const createOrder = (data) => api.post('/orders', data);
export const getOrders = () => api.get('/orders');

export default api;
