import { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, Utensils, X, Info } from 'lucide-react';
import { cn } from '../lib/utils';
import { getMenus, createMenu, createDish } from '../lib/api';
import { showSuccessToast, showErrorToast } from '../lib/alerts';

export default function MenusAndDishes() {
  const [menus, setMenus] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  // Modals state
  const [isMenuModalOpen, setIsMenuModalOpen] = useState(false);
  const [isDishModalOpen, setIsDishModalOpen] = useState(false);
  
  const [menuFormData, setMenuFormData] = useState({ name: '', description: '' });
  const [dishFormData, setDishFormData] = useState({ name: '', description: '', price: '', menuId: '' });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadMenus();
  }, []);

  const loadMenus = async () => {
    try {
      setLoading(true);
      const res = await getMenus();
      setMenus(res.data);
    } catch (err) {
      console.error('Error fetching menus:', err);
      showErrorToast('Error cargando menús');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateMenu = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      await createMenu(menuFormData);
      setIsMenuModalOpen(false);
      setMenuFormData({ name: '', description: '' });
      showSuccessToast('Menú creado correctamente');
      loadMenus();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al crear el menú');
    } finally {
      setSaving(false);
    }
  };

  const handleCreateDish = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      const data = {
        ...dishFormData,
        price: parseFloat(dishFormData.price)
      };
      await createDish(data);
      setIsDishModalOpen(false);
      setDishFormData({ name: '', description: '', price: '', menuId: '' });
      showSuccessToast('Plato agregado');
      loadMenus();
    } catch (err) {
      console.error(err);
      showErrorToast('Error al crear el plato');
    } finally {
      setSaving(false);
    }
  };

  const getTypeLabel = (type) => {
    if (type === 'Popular') {
      return { text: 'POPULAR 🔥', color: 'bg-amber-500/10 text-amber-500 border-amber-500/20' };
    }
    return { text: 'REGULAR', color: 'bg-zinc-500/10 text-zinc-500 border-zinc-500/20' };
  };

  return (
    <div className="p-8 pb-20 space-y-6 animate-in fade-in duration-500 relative">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Menús & Platos</h1>
          <p className="text-muted-foreground mt-1">Administra las cartas y los platos del restaurante.</p>
        </div>
        <div className="flex gap-2">
          <button 
            onClick={() => setIsMenuModalOpen(true)}
            className="flex items-center gap-2 bg-secondary text-secondary-foreground px-4 py-2.5 rounded-lg hover:bg-secondary/80 font-medium transition-colors shadow-sm"
          >
            <Plus className="w-5 h-5" />
            Nuevo Menú
          </button>
          <button 
            onClick={() => setIsDishModalOpen(true)}
            className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2.5 rounded-lg hover:bg-primary/90 font-medium transition-colors shadow-sm"
          >
            <Utensils className="w-5 h-5" />
            Agregar Plato
          </button>
        </div>
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
        <input 
          type="text"
          placeholder="Filtrar menús..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full pl-9 pr-4 py-2 bg-card border border-border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary/50 transition-shadow"
        />
      </div>

      {loading ? (
        <div className="flex flex-col items-center justify-center p-12">
          <span className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></span>
          <p className="mt-4 text-muted-foreground">Cargando menús...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
          {menus.filter(m => m.name.toLowerCase().includes(searchTerm.toLowerCase())).map((menu) => (
            <div key={menu.id} className="bg-card border border-border rounded-xl shadow-sm flex flex-col overflow-hidden">
              {/* Menu Header */}
              <div className="p-5 border-b border-border bg-muted/20 flex items-start justify-between">
                <div>
                  <h2 className="text-xl font-bold flex items-center gap-2">
                    {menu.name}
                    <span className="text-xs font-semibold px-2 py-0.5 rounded-full bg-primary/20 text-primary">ID: {menu.id}</span>
                  </h2>
                  <p className="text-muted-foreground text-sm mt-1">{menu.description}</p>
                </div>
              </div>

              {/* Dishes List */}
              <div className="p-0">
                <table className="w-full text-sm">
                  <thead className="bg-muted/10 text-xs uppercase font-medium text-muted-foreground">
                    <tr>
                      <th className="px-5 py-3 text-left">Plato</th>
                      <th className="px-5 py-3 text-left hidden sm:table-cell">Desc.</th>
                      <th className="px-5 py-3 text-left">Tipo</th>
                      <th className="px-5 py-3 text-right">Precio</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-border">
                    {!menu.dishes || menu.dishes.length === 0 ? (
                      <tr>
                        <td colSpan="4" className="px-5 py-6 text-center text-muted-foreground text-sm">
                          Este menú no tiene platos aún.
                        </td>
                      </tr>
                    ) : (
                      menu.dishes.map(dish => {
                        const typeInfo = getTypeLabel(dish.dishType);
                        return (
                          <tr key={dish.id} className="group hover:bg-muted/30 transition-colors">
                            <td className="px-5 py-3 font-medium">{dish.name}</td>
                            <td className="px-5 py-3 text-muted-foreground truncate max-w-[150px] hidden sm:table-cell" title={dish.description}>{dish.description}</td>
                            <td className="px-5 py-3">
                              <span className={cn("px-2 py-0.5 rounded-full text-[10px] font-bold tracking-wider border", typeInfo.color)}>
                                {typeInfo.text}
                              </span>
                            </td>
                            <td className="px-5 py-3 text-right font-medium text-primary">
                              ${dish.price?.toLocaleString()}
                            </td>
                          </tr>
                        );
                      })
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* MODAL NUEVO MENÚ */}
      {isMenuModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-background/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-card w-full max-w-sm rounded-xl border border-border shadow-lg p-6 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-xl font-bold">Nuevo Menú</h3>
              <button onClick={() => setIsMenuModalOpen(false)} className="text-muted-foreground hover:text-foreground transition">
                <X className="w-5 h-5"/>
              </button>
            </div>
            <form onSubmit={handleCreateMenu} className="space-y-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">Nombre</label>
                <input required value={menuFormData.name} onChange={e => setMenuFormData({...menuFormData, name: e.target.value})} type="text" className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none" />
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium">Descripción</label>
                <textarea required value={menuFormData.description} onChange={e => setMenuFormData({...menuFormData, description: e.target.value})} className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none resize-none" rows="3" />
              </div>
              <div className="pt-4 flex justify-end gap-3">
                <button type="button" onClick={() => setIsMenuModalOpen(false)} className="px-4 py-2 text-sm font-medium bg-secondary text-secondary-foreground rounded-lg hover:bg-secondary/80">Cancelar</button>
                <button type="submit" disabled={saving} className="px-4 py-2 text-sm font-medium bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 disabled:opacity-50">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL NUEVO PLATO */}
      {isDishModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-background/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-card w-full max-w-md rounded-xl border border-border shadow-lg p-6 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-xl font-bold">Agregar Plato</h3>
              <button onClick={() => setIsDishModalOpen(false)} className="text-muted-foreground hover:text-foreground transition">
                <X className="w-5 h-5"/>
              </button>
            </div>
            <form onSubmit={handleCreateDish} className="space-y-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">Menú al que pertenece</label>
                <select required value={dishFormData.menuId} onChange={e => setDishFormData({...dishFormData, menuId: e.target.value})} className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none">
                  <option value="" disabled>Seleccione un menú</option>
                  {menus.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium">Nombre del Plato</label>
                  <input required value={dishFormData.name} onChange={e => setDishFormData({...dishFormData, name: e.target.value})} type="text" className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none" />
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">Precio ($)</label>
                  <input required min="1" step="0.01" value={dishFormData.price} onChange={e => setDishFormData({...dishFormData, price: e.target.value})} type="number" className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none" />
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium">Descripción</label>
                <textarea required value={dishFormData.description} onChange={e => setDishFormData({...dishFormData, description: e.target.value})} className="w-full bg-background border border-border rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-primary/50 outline-none resize-none" rows="2" />
              </div>
              <div className="bg-blue-500/10 text-blue-500 p-3 rounded-lg flex gap-3 text-sm border border-blue-500/20">
                <Info className="w-5 h-5 shrink-0" />
                <p>El sistema asignará el estado <b>POPULAR</b> automáticamente a medida que el plato reciba más órdenes.</p>
              </div>
              <div className="pt-4 flex justify-end gap-3">
                <button type="button" onClick={() => setIsDishModalOpen(false)} className="px-4 py-2 text-sm font-medium bg-secondary text-secondary-foreground rounded-lg hover:bg-secondary/80">Cancelar</button>
                <button type="submit" disabled={saving || !dishFormData.menuId} className="px-4 py-2 text-sm font-medium bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 disabled:opacity-50">Guardar Plato</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
