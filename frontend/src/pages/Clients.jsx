import { useState, useEffect } from 'react';
import { cn } from '../lib/utils';
import { getClients, createClient, deleteClient } from '../lib/api';
import { showSuccessToast, showErrorToast, showConfirmDialog } from '../lib/alerts';

export default function Clients() {
  const [clients, setClients] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formData, setFormData] = useState({ name: '', lastName: '', email: '', clientType: 'Comun' });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadClients();
  }, []);

  const loadClients = async () => {
    try {
      setLoading(true);
      const res = await getClients();
      setClients(res.data);
    } catch (err) {
      console.error('Error fetching clients:', err);
      showErrorToast('Error cargando clientes');
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      await createClient(formData);
      setIsModalOpen(false);
      setFormData({ name: '', lastName: '', email: '', clientType: 'Comun' });
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
    if(!confirmed) return;
    try {
      await deleteClient(id);
      showSuccessToast('Cliente eliminado');
      loadClients();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al eliminar');
    }
  };

  const filteredClients = clients.filter(client => 
    `${client.name} ${client.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8 pb-20 space-y-6 animate-in fade-in duration-500 relative h-full bg-surface text-on-surface">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-headline font-bold tracking-tight">Clients</h1>
          <p className="text-on-surface-variant font-body mt-1">Directory of guests and frequent diners.</p>
        </div>
        <button 
          onClick={() => setIsModalOpen(true)}
          className="flex items-center gap-2 bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed font-bold px-6 py-3 rounded-sm hover:brightness-110 active:scale-95 transition-all text-xs tracking-widest uppercase shadow-xl"
        >
          <span className="material-symbols-outlined text-sm">person_add</span>
          Register Guest
        </button>
      </div>

      <div className="bg-surface-container-low rounded-md flex flex-col shadow-[0_4px_12px_rgba(0,0,0,0.1)]">
        <div className="p-4 border-b border-outline-variant/10 flex items-center justify-between">
          <div className="relative w-full max-w-md">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 material-symbols-outlined text-outline text-sm">search</span>
            <input 
              type="text"
              placeholder="Search by name or email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-surface-container-high border-none border-b border-outline-variant/20 rounded-sm text-sm focus:outline-none focus:ring-1 focus:ring-primary/50 transition-shadow text-on-surface placeholder:text-outline/70"
            />
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-sm text-left font-body">
            <thead className="bg-surface-container/50 text-on-surface-variant text-[10px] uppercase font-bold tracking-widest border-b border-outline-variant/10">
              <tr>
                <th className="px-6 py-4">Guest</th>
                <th className="px-6 py-4">Contact</th>
                <th className="px-6 py-4">Tier Status</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-outline-variant/5">
              {loading ? (
                <tr>
                  <td colSpan="4" className="px-6 py-12 text-center text-on-surface-variant">
                    <div className="flex flex-col items-center justify-center gap-2">
                       <span className="material-symbols-outlined animate-spin text-primary text-2xl">sync</span>
                       Loading directory...
                    </div>
                  </td>
                </tr>
              ) : filteredClients.length === 0 ? (
                <tr>
                  <td colSpan="4" className="px-6 py-12 text-center text-on-surface-variant">
                    No guests matched your criteria.
                  </td>
                </tr>
              ) : (
                filteredClients.map((client) => (
                  <tr key={client.id} className="group hover:bg-surface-container transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-4">
                        <div className="w-10 h-10 rounded-sm bg-surface-container-high flex items-center justify-center text-on-surface font-headline font-semibold uppercase border border-outline-variant/10">
                          {(client.name?.charAt(0) || '')}{(client.lastName?.charAt(0) || '')}
                        </div>
                        <div>
                          <p className="font-semibold text-on-surface text-base headline leading-tight">{client.name} {client.lastName}</p>
                          <p className="text-[10px] text-on-surface-variant flex items-center gap-1 mt-0.5 uppercase tracking-widest font-bold">
                            Guest ID: {client.id}
                          </p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2 text-on-surface-variant">
                        <span className="material-symbols-outlined text-sm">mail</span>
                        <span>{client.email}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      {client.clientType === 'Frecuente' ? (
                        <div className="flex items-center gap-2">
                          <div className="w-1.5 h-1.5 rounded-full bg-primary shadow-[0_0_8px_rgba(245,158,11,0.6)]"></div>
                          <span className="text-[10px] font-bold text-primary uppercase tracking-widest">VIP Frequent</span>
                        </div>
                      ) : (
                        <div className="flex items-center gap-2 opacity-60">
                           <div className="w-1.5 h-1.5 rounded-full bg-outline-variant"></div>
                           <span className="text-[10px] font-bold text-on-surface-variant uppercase tracking-widest">Standard Return</span>
                        </div>
                      )}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button onClick={() => handleDelete(client.id)} className="p-2 text-on-surface-variant hover:text-error transition-colors bg-surface-container-high hover:bg-error-container/20 rounded-sm">
                          <span className="material-symbols-outlined text-sm">delete</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* CREATE MODAL */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-md rounded-md shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/20 p-8 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-8">
              <h3 className="text-xl font-headline font-bold text-on-surface uppercase tracking-tight">Register Guest</h3>
              <button onClick={() => setIsModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition">
                <span className="material-symbols-outlined">close</span>
              </button>
            </div>
            
            <form onSubmit={handleCreate} className="space-y-6">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">First Name</label>
                  <input required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface" placeholder="John" />
                </div>
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Last Name</label>
                  <input required value={formData.lastName} onChange={e => setFormData({...formData, lastName: e.target.value})} type="text" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface" placeholder="Doe" />
                </div>
              </div>
              
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Email Contact</label>
                <input required value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} type="email" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface" placeholder="guest@example.com" />
              </div>

              <div className="pt-6 flex justify-end gap-3 border-t border-outline-variant/10">
                <button type="button" onClick={() => setIsModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">
                  Cancel
                </button>
                <button type="submit" disabled={saving} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl hover:brightness-110 active:scale-95 transition-all disabled:opacity-50">
                  {saving ? 'Processing...' : 'Register'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
