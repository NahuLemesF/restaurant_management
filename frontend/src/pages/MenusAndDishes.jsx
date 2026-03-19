import { useState, useEffect } from 'react';
import { Plus, Search, MoreVertical, Edit2, Trash2, Utensils, Info } from 'lucide-react';
import { cn } from '../lib/utils';

export default function MenusAndDishes() {
  const [menus, setMenus] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  // Mock initial data
  useEffect(() => {
    setTimeout(() => {
      setMenus([
        {
          id: 1,
          name: 'Menú Ejecutivo',
          description: 'Almuerzo de Lunes a Viernes',
          dishes: [
            { id: 101, name: 'Milanesa c/ Puré', price: 6500, type: 'MAIN_COURSE' },
            { id: 102, name: 'Flan Casero', price: 2000, type: 'DESSERT' },
          ]
        },
        {
          id: 2,
          name: 'Carta Principal',
          description: 'Platos a la carta disponibles todo el día',
          dishes: [
            { id: 201, name: 'Bife de Chorizo', price: 12000, type: 'MAIN_COURSE' },
            { id: 202, name: 'Ensalada César', price: 5500, type: 'APPETIZER' },
            { id: 203, name: 'Tiramisú', price: 3500, type: 'DESSERT' },
            { id: 204, name: 'Vino Tinto Malbec', price: 8000, type: 'BEVERAGE' },
          ]
        }
      ]);
      setLoading(false);
    }, 500);
  }, []);

  const getTypeLabel = (type) => {
    const labels = {
      APPETIZER: { text: 'ENTRADA', color: 'bg-emerald-500/10 text-emerald-500 border-emerald-500/20' },
      MAIN_COURSE: { text: 'PRINCIPAL', color: 'bg-primary/10 text-primary border-primary/20' },
      DESSERT: { text: 'POSTRE', color: 'bg-pink-500/10 text-pink-500 border-pink-500/20' },
      BEVERAGE: { text: 'BEBIDA', color: 'bg-blue-500/10 text-blue-500 border-blue-500/20' }
    };
    return labels[type] || { text: type, color: 'bg-zinc-500/10 text-zinc-500 border-zinc-500/20' };
  };

  return (
    <div className="p-8 pb-20 space-y-6 animate-in fade-in duration-500">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Menús & Platos</h1>
          <p className="text-muted-foreground mt-1">Administra las cartas y los platos del restaurante.</p>
        </div>
        <div className="flex gap-2">
          <button className="flex items-center gap-2 bg-secondary text-secondary-foreground px-4 py-2.5 rounded-lg hover:bg-secondary/80 font-medium transition-colors shadow-sm">
            <Plus className="w-5 h-5" />
            Nuevo Menú
          </button>
          <button className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2.5 rounded-lg hover:bg-primary/90 font-medium transition-colors shadow-sm">
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
                <div className="flex gap-2">
                  <button className="text-muted-foreground hover:text-primary transition-colors p-1"><Edit2 className="w-4 h-4" /></button>
                  <button className="text-muted-foreground hover:text-destructive transition-colors p-1"><Trash2 className="w-4 h-4" /></button>
                </div>
              </div>

              {/* Dishes List */}
              <div className="p-0">
                <table className="w-full text-sm">
                  <thead className="bg-muted/10 text-xs uppercase font-medium text-muted-foreground">
                    <tr>
                      <th className="px-5 py-3 text-left">Plato</th>
                      <th className="px-5 py-3 text-left">Tipo</th>
                      <th className="px-5 py-3 text-right">Precio</th>
                      <th className="px-5 py-3 text-right"></th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-border">
                    {menu.dishes.length === 0 ? (
                      <tr>
                        <td colSpan="4" className="px-5 py-6 text-center text-muted-foreground text-sm">
                          Este menú no tiene platos.
                        </td>
                      </tr>
                    ) : (
                      menu.dishes.map(dish => {
                        const typeInfo = getTypeLabel(dish.type);
                        return (
                          <tr key={dish.id} className="group hover:bg-muted/30 transition-colors">
                            <td className="px-5 py-3 font-medium">{dish.name}</td>
                            <td className="px-5 py-3">
                              <span className={cn("px-2 py-0.5 rounded-full text-[10px] font-bold tracking-wider border", typeInfo.color)}>
                                {typeInfo.text}
                              </span>
                            </td>
                            <td className="px-5 py-3 text-right font-medium text-primary">
                              ${dish.price.toLocaleString()}
                            </td>
                            <td className="px-5 py-3 text-right">
                              <button className="opacity-0 group-hover:opacity-100 text-muted-foreground hover:text-primary transition-all">
                                <Edit2 className="w-3.5 h-3.5" />
                              </button>
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
    </div>
  );
}
