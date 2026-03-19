import axios from 'axios';

/**
 * Instancia centralizada de Axios — única fuente de verdad para la configuración HTTP.
 * Todos los repositorios consumen este cliente (DIP sobre infraestructura).
 */
const httpClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default httpClient;
