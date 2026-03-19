import { useState, useEffect, useCallback } from 'react';
import menuRepository from '../infrastructure/api/menuRepository';
import dishRepository from '../infrastructure/api/dishRepository';
import { showSuccessToast, showErrorToast, showConfirmDialog } from '../lib/alerts';

const initialMenuForm = { name: '', description: '' };
const initialDishForm = { name: '', description: '', price: '', menuId: '', imageUrl: '', dishType: 'Standard' };

/**
 * Caso de uso: Gestión de Menús y Platos.
 * Encapsula el estado de menús/platos, formularios y subida de imagen.
 */
export function useMenus() {
  const [menus, setMenus] = useState([]);
  const [activeMenuId, setActiveMenuId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [uploadingImage, setUploadingImage] = useState(false);
  const [saving, setSaving] = useState(false);

  // Menu modal
  const [isMenuModalOpen, setIsMenuModalOpen] = useState(false);
  const [editingMenuId, setEditingMenuId] = useState(null);
  const [menuFormData, setMenuFormData] = useState(initialMenuForm);

  // Dish modal
  const [isDishModalOpen, setIsDishModalOpen] = useState(false);
  const [editingDishId, setEditingDishId] = useState(null);
  const [dishFormData, setDishFormData] = useState(initialDishForm);

  const loadMenus = useCallback(async () => {
    try {
      setLoading(true);
      const res = await menuRepository.getAll();
      setMenus(res.data);
      if (res.data.length > 0 && !activeMenuId) {
        setActiveMenuId(res.data[0].id);
      }
    } catch (err) {
      console.error('Error fetching menus:', err);
      showErrorToast('Error cargando menús');
    } finally {
      setLoading(false);
    }
  }, [activeMenuId]);

  useEffect(() => {
    loadMenus();
  }, []);

  const handleMenuSubmit = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      if (editingMenuId) {
        await menuRepository.update(editingMenuId, menuFormData);
        showSuccessToast('Menú actualizado');
      } else {
        await menuRepository.create(menuFormData);
        showSuccessToast('Menú creado');
      }
      setIsMenuModalOpen(false);
      setMenuFormData(initialMenuForm);
      setEditingMenuId(null);
      loadMenus();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al guardar el menú.');
    } finally {
      setSaving(false);
    }
  };

  const handleImageChange = async (e) => {
    if (e.target.files && e.target.files[0]) {
      try {
        setUploadingImage(true);
        const res = await dishRepository.uploadImage(e.target.files[0]);
        setDishFormData(prev => ({ ...prev, imageUrl: res.data.imageUrl }));
        showSuccessToast('Foto lista');
      } catch (err) {
        console.error(err);
        showErrorToast('Error al procesar la imagen.');
      } finally {
        setUploadingImage(false);
      }
    }
  };

  const handleDishSubmit = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      const data = {
        name: dishFormData.name,
        description: dishFormData.description,
        price: parseFloat(dishFormData.price),
        menuId: parseInt(dishFormData.menuId),
        imageUrl: dishFormData.imageUrl,
        dishType: dishFormData.dishType,
      };
      if (editingDishId) {
        await dishRepository.update(editingDishId, data);
        showSuccessToast('Plato actualizado');
      } else {
        await dishRepository.create(data);
        showSuccessToast('Plato agregado');
      }
      setIsDishModalOpen(false);
      setDishFormData(initialDishForm);
      setEditingDishId(null);
      loadMenus();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al guardar el plato.');
    } finally {
      setSaving(false);
    }
  };

  const handleDeleteDish = async (dishId) => {
    const confirmed = await showConfirmDialog('¿Eliminar plato?', 'Esta acción no se puede deshacer.');
    if (!confirmed) return;
    try {
      await dishRepository.delete(dishId);
      showSuccessToast('Plato eliminado');
      loadMenus();
    } catch (err) {
      showErrorToast('Error al eliminar el plato');
    }
  };

  const openEditMenu = (menu) => {
    setEditingMenuId(menu.id);
    setMenuFormData({ name: menu.name, description: menu.description || '' });
    setIsMenuModalOpen(true);
  };

  const openEditDish = (dish) => {
    setEditingDishId(dish.id);
    setDishFormData({
      name: dish.name,
      description: dish.description || '',
      price: dish.price,
      menuId: dish.menuId || activeMenuId,
      imageUrl: dish.imageUrl || '',
      dishType: dish.dishType || 'Standard',
    });
    setIsDishModalOpen(true);
  };

  const activeMenu = menus.find(m => String(m.id) === String(activeMenuId)) || null;
  const filteredDishes = activeMenu?.dishes?.filter(d =>
    d.name.toLowerCase().includes(searchTerm.toLowerCase())
  ) || [];

  return {
    menus,
    activeMenuId, setActiveMenuId,
    activeMenu,
    filteredDishes,
    loading,
    searchTerm, setSearchTerm,
    saving, uploadingImage,
    isMenuModalOpen, setIsMenuModalOpen,
    isDishModalOpen, setIsDishModalOpen,
    editingMenuId, editingDishId,
    menuFormData, setMenuFormData,
    dishFormData, setDishFormData,
    handleMenuSubmit,
    handleDishSubmit,
    handleImageChange,
    handleDeleteDish,
    openEditMenu,
    openEditDish,
  };
}
