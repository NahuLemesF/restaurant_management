import { useState, useEffect } from 'react';
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
      return { text: 'POPULAR 🔥', color: 'bg-error-container/20 text-error border-error-container/30' };
    }
    return { text: 'STANDARD', color: 'bg-outline-variant/10 text-on-surface-variant border-outline-variant/20' };
  };

  return (
    <div className="p-8 pb-20 space-y-6 animate-in fade-in duration-500 relative h-full bg-surface text-on-surface overflow-y-auto hide-scrollbar">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-headline font-bold tracking-tight">Menus & Dishes</h1>
          <p className="text-on-surface-variant font-body mt-1">Configure your offerings and kitchen availability.</p>
        </div>
        <div className="flex gap-3">
          <button 
            onClick={() => setIsMenuModalOpen(true)}
            className="flex items-center gap-2 bg-surface-container-high text-on-surface px-6 py-3 rounded-sm hover:bg-surface-bright font-bold text-xs uppercase tracking-widest border border-outline-variant/10 shadow-sm transition-colors"
          >
            <span className="material-symbols-outlined text-sm">menu_book</span>
            New Menu
          </button>
          <button 
            onClick={() => setIsDishModalOpen(true)}
            className="flex items-center gap-2 bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed px-6 py-3 rounded-sm hover:brightness-110 active:scale-95 font-bold text-xs uppercase tracking-widest shadow-xl transition-all"
          >
            <span className="material-symbols-outlined text-sm">restaurant</span>
            Add Dish
          </button>
        </div>
      </div>

      <div className="relative w-full max-w-md">
        <span className="absolute left-3 top-1/2 -translate-y-1/2 material-symbols-outlined text-outline text-sm">search</span>
        <input 
          type="text"
          placeholder="Filter menus by name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full pl-10 pr-4 py-2 bg-surface-container-high border-none border-b border-outline-variant/20 rounded-sm text-sm focus:outline-none focus:ring-1 focus:ring-primary/50 transition-shadow text-on-surface placeholder:text-outline"
        />
      </div>

      {loading ? (
        <div className="flex flex-col items-center justify-center py-20">
          <span className="material-symbols-outlined animate-spin text-primary text-3xl">sync</span>
          <p className="mt-4 text-on-surface-variant font-body text-sm uppercase tracking-widest font-bold">Loading Menus...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-6 pb-12">
          {menus.filter(m => m.name.toLowerCase().includes(searchTerm.toLowerCase())).map((menu) => (
            <div key={menu.id} className="bg-surface-container-low rounded-md shadow-[0_4px_12px_rgba(0,0,0,0.1)] flex flex-col overflow-hidden">
              
              {/* Menu Header */}
              <div className="p-6 border-b border-outline-variant/10 bg-surface-container/50 flex flex-col items-start gap-1">
                <div className="flex justify-between items-center w-full">
                  <h2 className="text-xl font-headline font-bold text-on-surface uppercase tracking-tight">
                    {menu.name}
                  </h2>
                  <span className="text-[10px] font-bold uppercase tracking-widest text-on-surface-variant px-2 py-0.5 rounded-sm bg-surface-container-high border border-outline-variant/20">
                    ID: {menu.id}
                  </span>
                </div>
                <p className="text-on-surface-variant text-sm font-body mt-1">{menu.description}</p>
              </div>

              {/* Dishes List */}
              <div className="p-0">
                <table className="w-full text-sm font-body">
                  <thead className="bg-surface-container-lowest text-on-surface-variant text-[10px] uppercase font-bold tracking-widest border-b border-outline-variant/10">
                    <tr>
                      <th className="px-6 py-3 text-left">Dish</th>
                      <th className="px-6 py-3 text-left hidden sm:table-cell">Desc</th>
                      <th className="px-6 py-3 text-left">Classification</th>
                      <th className="px-6 py-3 text-right">Price</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-outline-variant/5">
                    {!menu.dishes || menu.dishes.length === 0 ? (
                      <tr>
                        <td colSpan="4" className="px-6 py-8 text-center text-on-surface-variant text-sm border-t border-transparent">
                          No dishes registered for this menu segment.
                        </td>
                      </tr>
                    ) : (
                      menu.dishes.map(dish => {
                        const typeInfo = getTypeLabel(dish.dishType);
                        return (
                          <tr key={dish.id} className="group hover:bg-surface-container-high transition-colors">
                            <td className="px-6 py-4 font-semibold text-on-surface">{dish.name}</td>
                            <td className="px-6 py-4 text-on-surface-variant truncate max-w-[150px] hidden sm:table-cell text-xs" title={dish.description}>{dish.description}</td>
                            <td className="px-6 py-4">
                              <span className={cn("px-2 py-0.5 rounded-sm text-[10px] font-bold tracking-wider uppercase border", typeInfo.color)}>
                                {typeInfo.text}
                              </span>
                            </td>
                            <td className="px-6 py-4 text-right font-headline font-bold text-primary">
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
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-sm rounded-md shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/20 p-8 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-8">
              <h3 className="text-xl font-headline font-bold text-on-surface uppercase tracking-tight">New Menu Category</h3>
              <button onClick={() => setIsMenuModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition">
                <span className="material-symbols-outlined">close</span>
              </button>
            </div>
            <form onSubmit={handleCreateMenu} className="space-y-6">
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Menu Name</label>
                <input required value={menuFormData.name} onChange={e => setMenuFormData({...menuFormData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface" placeholder="e.g. Starters" />
              </div>
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Description</label>
                <textarea required value={menuFormData.description} onChange={e => setMenuFormData({...menuFormData, description: e.target.value})} className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface resize-none" rows="3" placeholder="A brief description of this section." />
              </div>
              <div className="pt-6 flex justify-end gap-3 border-t border-outline-variant/10">
                <button type="button" onClick={() => setIsMenuModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">Cancel</button>
                <button type="submit" disabled={saving} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl hover:brightness-110 active:scale-95 transition-all disabled:opacity-50">Create Menu</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* MODAL NUEVO PLATO */}
      {isDishModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-md rounded-md shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/20 p-8 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-8">
              <h3 className="text-xl font-headline font-bold text-on-surface uppercase tracking-tight">Add Dish</h3>
              <button onClick={() => setIsDishModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition">
                <span className="material-symbols-outlined">close</span>
              </button>
            </div>
            <form onSubmit={handleCreateDish} className="space-y-6">
              <div className="space-y-2 relative">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Assign to Menu</label>
                <select required value={dishFormData.menuId} onChange={e => setDishFormData({...dishFormData, menuId: e.target.value})} className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface appearance-none">
                  <option value="" disabled className="text-on-surface-variant/50">Select target menu...</option>
                  {menus.map(m => <option key={m.id} value={m.id}>{m.name}</option>)}
                </select>
                <span className="absolute right-4 bottom-3 pointer-events-none material-symbols-outlined text-sm text-outline">expand_more</span>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Dish Name</label>
                  <input required value={dishFormData.name} onChange={e => setDishFormData({...dishFormData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface" placeholder="Wagyu Carpaccio" />
                </div>
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Base Price</label>
                  <div className="relative">
                     <span className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant font-headline font-bold">$</span>
                     <input required min="1" step="0.01" value={dishFormData.price} onChange={e => setDishFormData({...dishFormData, price: e.target.value})} type="number" className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm pl-8 pr-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface font-mono" placeholder="24.00" />
                  </div>
                </div>
              </div>
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Description & Ingredients</label>
                <textarea required value={dishFormData.description} onChange={e => setDishFormData({...dishFormData, description: e.target.value})} className="w-full bg-surface-container-low border-b-2 border-outline-variant/20 rounded-t-sm px-4 py-3 text-sm focus:border-primary focus:bg-surface-container transition-all outline-none text-on-surface resize-none" rows="2" placeholder="Truffle oil, micro-arugula..." />
              </div>
              
              <div className="bg-tertiary-container/10 p-4 rounded-sm flex gap-3 text-xs border border-tertiary-container/20 text-on-surface-variant">
                <span className="material-symbols-outlined text-tertiary text-lg shrink-0">info</span>
                <p className="leading-relaxed"><span className="text-tertiary font-bold">Popoularity engine:</span> The system autonomously upgrades standard dishes to <span className="text-error font-bold tracking-widest text-[10px]">POPULAR</span> based on POS order velocity thresholds.</p>
              </div>

              <div className="pt-6 flex justify-end gap-3 border-t border-outline-variant/10">
                <button type="button" onClick={() => setIsDishModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">Cancel</button>
                <button type="submit" disabled={saving || !dishFormData.menuId} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl hover:brightness-110 active:scale-95 transition-all disabled:opacity-50">Publish Dish</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
