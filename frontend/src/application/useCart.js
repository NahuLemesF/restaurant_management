import { useState, useEffect, useCallback } from 'react';
import clientRepository from '../infrastructure/api/clientRepository';
import menuRepository from '../infrastructure/api/menuRepository';
import orderRepository from '../infrastructure/api/orderRepository';
import {
  addToCart,
  removeFromCart,
  computeSubtotal,
  computeDiscount,
  computeTotal,
} from '../domain/Cart';
import { isFrecuente } from '../domain/Client';
import { showSuccessToast, showErrorToast } from '../lib/alerts';

/**
 * Caso de uso: Punto de Venta.
 * Encapsula datos del catálogo, clientes, carrito y cálculos de precio.
 */
export function useCart() {
  const [clients, setClients] = useState([]);
  const [menus, setMenus] = useState([]);
  const [cart, setCart] = useState([]);
  const [selectedClientId, setSelectedClientId] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [activeCategory, setActiveCategory] = useState('Todos');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const loadData = useCallback(async () => {
    try {
      setLoading(true);
      const [clientsRes, menusRes] = await Promise.all([
        clientRepository.getAll(),
        menuRepository.getAll(),
      ]);
      setClients(clientsRes.data);
      setMenus(menusRes.data);
    } catch (err) {
      console.error('Error loading POS data:', err);
      showErrorToast('Error cargando datos del sistema');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  // Derivados: todos los platos de todos los menus
  const allDishes = menus.flatMap(menu =>
    (menu.dishes || []).map(dish => ({ ...dish, menuName: menu.name }))
  );

  const categories = ['Todos', ...new Set(menus.map(m => m.name))];

  const filteredDishes = allDishes.filter(dish => {
    const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = activeCategory === 'Todos' || dish.menuName === activeCategory;
    return matchesSearch && matchesCategory;
  });

  // Cliente seleccionado
  const selectedClient = clients.find(c => String(c.id) === String(selectedClientId)) || null;
  const clientIsFrecuente = selectedClient ? isFrecuente(selectedClient) : false;

  // Cálculos de precio usando el dominio puro
  const subtotal = computeSubtotal(cart);
  const discount = computeDiscount(subtotal, clientIsFrecuente);
  const total = computeTotal(subtotal, discount);

  const handleAddToCart = (dish) => {
    setCart(prev => addToCart(prev, dish));
  };

  const handleRemoveFromCart = (dishId) => {
    setCart(prev => removeFromCart(prev, dishId));
  };

  const handleCheckout = async () => {
    if (!selectedClientId) {
      showErrorToast('Por favor, seleccione un cliente antes de confirmar.');
      return;
    }
    if (cart.length === 0) {
      showErrorToast('El carrito está vacío.');
      return;
    }
    try {
      setSaving(true);
      const dishIds = cart.flatMap(item =>
        Array.from({ length: item.quantity || 1 }, () => item.id)
      );
      await orderRepository.create({ clientId: selectedClientId, dishIds });
      showSuccessToast('Orden guardada y facturada correctamente.');
      setCart([]);
    } catch (err) {
      console.error(err);
      showErrorToast('Hubo un error al facturar la orden.');
    } finally {
      setSaving(false);
    }
  };

  return {
    clients,
    menus,
    cart,
    filteredDishes,
    categories,
    selectedClientId, setSelectedClientId,
    selectedClient,
    clientIsFrecuente,
    searchTerm, setSearchTerm,
    activeCategory, setActiveCategory,
    subtotal,
    discount,
    total,
    loading,
    saving,
    handleAddToCart,
    handleRemoveFromCart,
    handleCheckout,
  };
}
