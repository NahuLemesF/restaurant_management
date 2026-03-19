import { useState, useEffect } from 'react';
import { ShoppingBag, Search, Plus, Minus, Trash2, Printer, CreditCard, UtensilsCrossed, User } from 'lucide-react';
import { cn } from '../lib/utils';
import { getDishes, getClients, createOrder } from '../lib/api';

export default function PointOfSale() {
  const [categories, setCategories] = useState(['Todos']);
  const [activeCategory, setActiveCategory] = useState('Todos');
  const [searchTerm, setSearchTerm] = useState('');
  
  const [cart, setCart] = useState([]);
  const [dishes, setDishes] = useState([]);
  const [clients, setClients] = useState([]);
  const [selectedClientId, setSelectedClientId] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [dishesRes, clientsRes] = await Promise.all([
        getDishes(),
        getClients()
      ]);
      setDishes(dishesRes.data);
      setClients(clientsRes.data);

      // Extract unique menu names to act as categories
      const menus = [...new Set(dishesRes.data.map(d => d.menuName))];
      setCategories(['Todos', ...menus]);
      
      if (clientsRes.data.length > 0) {
        setSelectedClientId(clientsRes.data[0].id);
      }
    } catch (err) {
      console.error(err);
      alert('Error cargando inicial. Asegúrate que hayas cargado menús, platos y clientes previamente.');
    } finally {
      setLoading(false);
    }
  };

  const filteredDishes = dishes.filter(dish => {
    const matchesCategory = activeCategory === 'Todos' || dish.menuName === activeCategory;
    const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const addToCart = (dish) => {
    setCart(prev => {
      const existing = prev.find(item => item.dish.id === dish.id);
      if (existing) {
        return prev.map(item => item.dish.id === dish.id ? { ...item, quantity: item.quantity + 1 } : item);
      }
      return [...prev, { dish, quantity: 1, notes: '' }];
    });
  };

  const updateQuantity = (dishId, delta) => {
    setCart(prev => prev.map(item => {
      if (item.dish.id === dishId) {
        const newQuantity = Math.max(0, item.quantity + delta);
        return { ...item, quantity: newQuantity };
      }
      return item;
    }).filter(item => item.quantity > 0));
  };

  const cartTotal = cart.reduce((sum, item) => sum + (item.dish.price * item.quantity), 0);

  const handleCheckout = async () => {
    if (!selectedClientId) {
      alert("Debes seleccionar un cliente primero.");
      return;
    }
    
    // Flat map of dish IDs reflecting quantities
    const dishIds = [];
    cart.forEach(item => {
      for (let i = 0; i < item.quantity; i++) {
        dishIds.push(item.dish.id);
      }
    });

    try {
      setSaving(true);
      await createOrder({ clientId: selectedClientId, dishIds });
      alert('Orden guardada y facturada correctamente.');
      setCart([]); // Clear cart
    } catch (err) {
      console.error(err);
      alert('Hubo un error al facturar la orden.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="flex h-[calc(100vh-0px)] overflow-hidden bg-background">
      
      {/* LEFT PANEL: Menu & Dishes */}
      <div className="flex-1 flex flex-col h-full border-r border-border bg-background">
        <div className="p-6 border-b border-border bg-card">
          <h1 className="text-2xl font-bold tracking-tight mb-4">Punto de Venta</h1>
          
          <div className="flex items-center gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input 
                type="text"
                placeholder="Buscar plato por nombre..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-9 pr-4 py-2.5 bg-background border border-border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary/50"
              />
            </div>
          </div>

          <div className="flex overflow-x-auto gap-2 mt-4 pb-2 snap-x scrollbar-hide">
            {categories.map(cat => (
              <button
                key={cat}
                onClick={() => setActiveCategory(cat)}
                className={cn(
                  "px-4 py-1.5 rounded-full text-sm font-medium whitespace-nowrap transition-colors snap-start",
                  activeCategory === cat 
                    ? "bg-primary text-primary-foreground" 
                    : "bg-secondary text-secondary-foreground hover:bg-secondary/80"
                )}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-6 bg-secondary/20">
          {loading ? (
             <div className="flex justify-center items-center h-full text-muted-foreground">
               <span className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></span>
             </div>
          ) : filteredDishes.length === 0 ? (
            <div className="flex flex-col justify-center items-center h-full text-muted-foreground">
              Aún no hay platos registrados o que coincidan con la búsqueda.
            </div>
          ) : (
            <div className="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
              {filteredDishes.map(dish => (
                <button
                  key={dish.id}
                  onClick={() => addToCart(dish)}
                  className="bg-card border border-border rounded-xl p-4 flex flex-col items-center justify-center text-center gap-3 hover:border-primary/50 hover:shadow-md transition-all group active:scale-95"
                >
                  {dish.dishType === 'Popular' && (
                    <span className="absolute top-2 right-2 bg-amber-500 text-amber-950 text-[10px] font-bold px-2 py-0.5 rounded-full">🔥</span>
                  )}
                  <div className="w-16 h-16 text-4xl bg-secondary/50 rounded-full flex items-center justify-center group-hover:scale-110 transition-transform">
                    🍛
                  </div>
                  <div>
                    <h3 className="font-semibold text-sm line-clamp-2 leading-tight">{dish.name}</h3>
                    <p className="text-primary font-bold mt-1">${dish.price?.toLocaleString()}</p>
                  </div>
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* RIGHT PANEL: Cart / Ticket */}
      <div className="w-96 flex flex-col h-full bg-card shadow-[-4px_0_24px_-10px_rgba(0,0,0,0.1)] z-10">
        
        <div className="p-6 border-b border-border bg-muted/10">
          <div className="flex items-center gap-3 text-foreground mb-3">
            <ShoppingBag className="w-5 h-5 text-primary" />
            <h2 className="text-xl font-bold">Orden Actual</h2>
          </div>
          <div className="flex items-center gap-2 text-sm text-foreground bg-background p-2 pr-3 rounded-lg border border-border relative">
            <User className="w-4 h-4 text-muted-foreground ml-1" />
            <select 
              value={selectedClientId} 
              onChange={e => setSelectedClientId(e.target.value)}
              className="w-full bg-transparent focus:outline-none appearance-none font-medium text-sm"
            >
              <option value="" disabled>Seleccionar un cliente...</option>
              {clients.map(c => (
                <option key={c.id} value={c.id}>
                  {c.name} {c.lastName} {c.clientType === 'FREQUENT' ? '(Frecuente)' : ''}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-4 space-y-3">
          {cart.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-muted-foreground opacity-60">
              <UtensilsCrossed className="w-12 h-12 mb-4" />
              <p>La orden está vacía</p>
              <p className="text-sm text-center mt-2 max-w-[200px]">Selecciona platos del menú para armar el ticket.</p>
            </div>
          ) : (
            cart.map(item => (
              <div key={item.dish.id} className="bg-background border border-border p-3 rounded-lg flex gap-3 animate-in slide-in-from-right-2 duration-200">
                <div className="flex flex-col items-center justify-center gap-1 bg-secondary rounded-md p-1">
                  <button onClick={() => updateQuantity(item.dish.id, 1)} className="p-1 hover:text-primary transition-colors"><Plus className="w-3 h-3" /></button>
                  <span className="font-bold text-sm min-w-[1.5rem] text-center">{item.quantity}</span>
                  <button onClick={() => updateQuantity(item.dish.id, -1)} className="p-1 hover:text-destructive transition-colors"><Minus className="w-3 h-3" /></button>
                </div>
                
                <div className="flex-1 min-w-0 flex flex-col justify-center">
                  <h4 className="font-medium text-sm truncate pr-2">{item.dish.name}</h4>
                  <p className="text-primary font-semibold text-sm">${(item.dish.price * item.quantity).toLocaleString()}</p>
                </div>

                <div className="flex items-center justify-center">
                  <button onClick={() => updateQuantity(item.dish.id, -item.quantity)} className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-md transition-colors">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="p-6 bg-muted/30 border-t border-border mt-auto">
          <div className="space-y-2 mb-4 text-sm">
            <div className="flex justify-between text-muted-foreground">
              <span>Subtotal</span>
              <span>${cartTotal.toLocaleString()}</span>
            </div>
            <div className="flex justify-between text-xl font-bold pt-2 border-t border-border text-foreground">
              <span>Total</span>
              <span className="text-primary">${cartTotal.toLocaleString()}</span>
            </div>
            {clients.find(c => String(c.id) === String(selectedClientId))?.clientType === 'FREQUENT' && (
               <p className="text-[11px] text-amber-500 font-medium text-right mt-1">* Se aplicará un descuento al facturar por ser cliente frecuente.</p>
            )}
          </div>

          <div className="grid grid-cols-2 gap-3 mt-4">
            <button 
              disabled={cart.length === 0 || saving}
              className="flex items-center justify-center gap-2 py-3 rounded-lg bg-secondary text-secondary-foreground hover:bg-secondary/80 font-medium transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <Printer className="w-4 h-4" /> Comanda
            </button>
            <button 
              onClick={handleCheckout}
              disabled={cart.length === 0 || saving}
              className="flex items-center justify-center gap-2 py-3 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 font-bold shadow-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <CreditCard className="w-4 h-4" /> {saving ? '...' : 'Cobrar'}
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}
