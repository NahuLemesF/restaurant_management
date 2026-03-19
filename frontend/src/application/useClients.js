import { useState, useEffect, useCallback } from 'react';
import clientRepository from '../infrastructure/api/clientRepository';
import { showSuccessToast, showErrorToast, showConfirmDialog } from '../lib/alerts';

const initialFormData = { name: '', lastName: '', email: '', clientType: 'Comun' };

/**
 * Caso de uso: Gestión de Clientes.
 * Encapsula todo el estado y lógica de negocio de la vista de Clientes.
 * La Page solo se suscribe a este hook — no realiza llamadas HTTP directas (DIP).
 */
export function useClients() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState(initialFormData);
  const [saving, setSaving] = useState(false);

  const loadClients = useCallback(async () => {
    try {
      setLoading(true);
      const res = await clientRepository.getAll();
      setClients(res.data.sort((a, b) => b.id - a.id));
    } catch (err) {
      console.error('Error fetching clients:', err);
      showErrorToast('Error cargando clientes');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadClients();
  }, [loadClients]);

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      await clientRepository.create(formData);
      setIsModalOpen(false);
      setFormData(initialFormData);
      showSuccessToast('Cliente registrado');
      loadClients();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al crear el cliente');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmed = await showConfirmDialog('¿Eliminar cliente?');
    if (!confirmed) return;
    try {
      await clientRepository.delete(id);
      showSuccessToast('Cliente eliminado');
      loadClients();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al eliminar');
    }
  };

  const filteredClients = clients.filter(client =>
    `${client.name} ${client.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (client.email || '').toLowerCase().includes(searchTerm.toLowerCase())
  );

  return {
    clients,
    filteredClients,
    loading,
    searchTerm,
    setSearchTerm,
    isModalOpen,
    setIsModalOpen,
    formData,
    setFormData,
    saving,
    handleCreate,
    handleDelete,
  };
}
