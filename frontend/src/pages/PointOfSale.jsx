import { useState, useEffect, useRef } from 'react';
import { useReactToPrint } from 'react-to-print';
import ReceiptTemplate from '../components/ReceiptTemplate';
import { cn } from '../lib/utils';
import { getDishes, getClients, createOrder } from '../lib/api';
import { showSuccessToast, showErrorToast, showErrorModal } from '../lib/alerts';

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

  const receiptRef = useRef();
  
  const handlePrint = useReactToPrint({
    contentRef: receiptRef,
    documentTitle: `Ticket_${new Date().getTime()}`,
  });

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

      const menus = [...new Set(dishesRes.data.map(d => d.menuName))];
      setCategories(['Todos', ...menus]);
      
      if (clientsRes.data.length > 0) {
        setSelectedClientId(clientsRes.data[0].id);
      }
    } catch (err) {
      console.error(err);
      showErrorModal('Faltan datos iniciales', 'Asegúrate de haber cargado menús, platos y clientes.');
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
      showErrorToast("Debes seleccionar un cliente primero.");
      return;
    }
    
    const dishIds = [];
    cart.forEach(item => {
      for (let i = 0; i < item.quantity; i++) {
        dishIds.push(item.dish.id);
      }
    });

    try {
      setSaving(true);
      await createOrder({ clientId: selectedClientId, dishIds });
      showSuccessToast('Orden guardada y facturada correctamente.');
      setCart([]);
    } catch (err) {
      console.error(err);
      showErrorToast('Hubo un error al facturar la orden.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="flex w-full h-full bg-surface">
      
      {/* LEFT PANEL: Menu & Dishes */}
      <section className="w-[70%] h-full flex flex-col p-8 overflow-y-auto hide-scrollbar bg-surface border-r border-outline-variant/10">
        
        {/* Category Filter Bar */}
        <div className="flex flex-wrap items-center gap-4 mb-8">
          <div className="relative mr-4">
            <span className="absolute left-3 top-1/2 -translate-y-1/2 material-symbols-outlined text-outline text-sm">search</span>
            <input 
              type="text"
              placeholder="Buscar plato..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-9 pr-4 py-2 bg-surface-container-high border border-outline-variant/20 rounded-sm text-sm focus:outline-none focus:border-primary transition-all text-on-surface placeholder:text-outline"
            />
          </div>

          <div className="flex gap-4 overflow-x-auto hide-scrollbar">
            {categories.map(cat => (
              <button
                key={cat}
                onClick={() => setActiveCategory(cat)}
                className={cn(
                  "px-6 py-2 rounded-full text-xs font-semibold tracking-wide uppercase transition-colors whitespace-nowrap",
                  activeCategory === cat 
                    ? "border border-primary text-primary bg-primary/5" 
                    : "border border-outline-variant/20 text-on-surface-variant hover:bg-surface-container-high"
                )}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {/* Dishes Grid */}
        <div className="grid grid-cols-1 xl:grid-cols-2 2xl:grid-cols-3 gap-6">
          {loading ? (
             <div className="col-span-full flex justify-center py-20">
               <span className="material-symbols-outlined animate-spin text-primary text-3xl">sync</span>
             </div>
          ) : filteredDishes.length === 0 ? (
            <div className="col-span-full flex justify-center py-20 text-on-surface-variant font-body">
              No hay platos en el menú.
            </div>
          ) : (
            filteredDishes.map(dish => (
              <div
                key={dish.id}
                onClick={() => addToCart(dish)}
                className="group relative bg-surface-container-high p-4 rounded-md transition-all hover:translate-y-[-4px] cursor-pointer"
              >
                <div className="flex justify-between items-start mb-2">
                  <h3 className="font-headline font-semibold text-lg leading-tight text-on-surface pr-2">{dish.name}</h3>
                  <span className="text-primary font-headline font-bold shrink-0">${dish.price?.toLocaleString()}</span>
                </div>
                
                <p className="text-xs text-on-surface-variant line-clamp-2 font-body leading-relaxed mb-4">
                  {dish.description || 'Sin descripción'}
                </p>
                
                <div className="flex justify-between items-center mt-auto">
                  <div className="flex gap-1">
                    {dish.dishType === 'Popular' && (
                       <span className="px-2 py-0.5 bg-error-container/20 text-error text-[10px] rounded-sm font-bold uppercase tracking-tighter">
                         Popular 🔥
                       </span>
                    )}
                  </div>
                  <span className="material-symbols-outlined text-outline opacity-0 group-hover:opacity-100 transition-opacity">add_circle</span>
                </div>
              </div>
            ))
          )}
        </div>
      </section>

      {/* RIGHT PANEL: Cart / Ticket */}
      <section className="w-[30%] h-full bg-surface-container-low flex flex-col relative">
        
        {/* Client Selection */}
        <div className="p-6 pb-4">
          <div className="flex items-center justify-between mb-4">
            <span className="text-xs uppercase tracking-widest font-bold text-on-surface-variant">Ticket Actual</span>
            <span className="text-[10px] font-mono text-outline">#{new Date().getFullYear().toString()}-01</span>
          </div>
          <div className="flex items-center gap-3 p-3 bg-surface-container-lowest rounded-sm group hover:ring-1 hover:ring-outline-variant/30 transition-all border border-outline-variant/10 relative">
            <span className="material-symbols-outlined text-outline">account_circle</span>
            <select 
              value={selectedClientId} 
              onChange={e => setSelectedClientId(e.target.value)}
              className="flex-grow bg-transparent focus:outline-none appearance-none font-semibold text-sm text-on-surface"
            >
              <option value="" disabled className="text-background">Seleccionar cliente...</option>
              {clients.map(c => (
                <option key={c.id} value={c.id} className="bg-surface-container-high text-on-surface">
                  {c.name} {c.lastName} {c.clientType === 'Frecuente' ? '👑' : ''}
                </option>
              ))}
            </select>
            <span className="material-symbols-outlined text-on-surface-variant text-sm pointer-events-none absolute right-3">unfold_more</span>
          </div>
        </div>

        {/* Ticket Items List */}
        <div className="flex-grow overflow-y-auto px-6 hide-scrollbar flex flex-col gap-6 py-4">
          {cart.length === 0 ? (
            <div className="h-full flex flex-col items-center justify-center text-outline opacity-60">
              <span className="material-symbols-outlined text-4xl mb-4">receipt_long</span>
              <p className="text-sm font-body">La comanda está vacía</p>
            </div>
          ) : (
            cart.map(item => (
              <div key={item.dish.id} className="flex gap-4 relative pl-3 group">
                <div className="absolute left-0 top-0 w-[2px] h-full bg-primary/70 shadow-[0_0_8px_rgba(245,158,11,0.4)]"></div>
                <div className="flex-grow">
                  <div className="flex justify-between items-start">
                    <h4 className="text-sm font-semibold text-on-surface leading-tight pr-2">{item.dish.name}</h4>
                    <span className="text-sm font-mono text-on-surface">${(item.dish.price * item.quantity).toLocaleString()}</span>
                  </div>
                  
                  <div className="flex items-center justify-between mt-3">
                    <div className="flex items-center bg-surface-container-high rounded-sm px-1 shadow-sm border border-outline-variant/10">
                      <button onClick={() => updateQuantity(item.dish.id, -1)} className="p-1 text-on-surface-variant hover:text-primary transition-colors">
                        <span className="material-symbols-outlined text-sm leading-none">remove</span>
                      </button>
                      <span className="px-3 text-xs font-mono font-bold text-on-surface">{item.quantity}</span>
                      <button onClick={() => updateQuantity(item.dish.id, 1)} className="p-1 text-on-surface-variant hover:text-primary transition-colors">
                        <span className="material-symbols-outlined text-sm leading-none">add</span>
                      </button>
                    </div>
                    <button onClick={() => updateQuantity(item.dish.id, -item.quantity)} className="text-error/60 hover:text-error transition-colors focus:outline-none">
                      <span className="material-symbols-outlined text-sm">delete</span>
                    </button>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {/* Footer: Total & Actions */}
        <div className="p-6 bg-surface-container-lowest shadow-[0_-20px_40px_rgba(0,0,0,0.4)] z-10 border-t border-outline-variant/5">
          <div className="space-y-2 mb-6">
            <div className="flex justify-between items-center mb-4">
              <span className="font-headline font-black text-lg uppercase tracking-tight text-on-surface">Total</span>
              <span className="font-headline font-black text-2xl text-primary">${cartTotal.toLocaleString()}</span>
            </div>
            {clients.find(c => String(c.id) === String(selectedClientId))?.clientType === 'Frecuente' && (
              <p className="text-[10px] text-tertiary font-bold tracking-wide uppercase text-right mt-1 w-full">* Descuento aplicado (Frecuente)</p>
            )}
          </div>
          
          <div className="grid grid-cols-2 gap-3">
            <button 
              onClick={() => handlePrint()}
              disabled={cart.length === 0 || saving}
              className="py-4 rounded-sm border border-outline-variant/20 text-xs text-on-surface font-bold uppercase tracking-widest hover:bg-surface-container-high transition-all disabled:opacity-30"
            >
              Comanda
            </button>
            <button 
              onClick={handleCheckout}
              disabled={cart.length === 0 || saving}
              className="py-4 bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed font-black text-xs uppercase tracking-widest rounded-sm shadow-xl hover:brightness-110 active:scale-95 transition-all disabled:opacity-50"
            >
              {saving ? '...' : 'Facturar'}
            </button>
          </div>
          
          <div className="mt-4 flex items-center justify-center gap-2">
             <div className="w-1.5 h-1.5 rounded-full bg-tertiary shadow-[0_0_8px_#8fd5ff]"></div>
             <span className="text-[10px] uppercase font-bold text-on-surface-variant tracking-tighter">Kitchen: Ready to Receive</span>
          </div>
        </div>

      </section>

      {/* COMPONENTE OCULTO PARA IMPRESIÓN */}
      <div style={{ display: 'none' }}>
        <ReceiptTemplate 
          ref={receiptRef}
          cart={cart}
          total={cartTotal}
          clientName={
            selectedClientId 
              ? `${clients.find(c => String(c.id) === String(selectedClientId))?.name} ${clients.find(c => String(c.id) === String(selectedClientId))?.lastName}` 
              : 'Consumidor Final'
          }
        />
      </div>

    </div>
  );
}
