import { cn } from '../lib/utils';
import { useMenus } from '../application/useMenus';

export default function MenusAndDishes() {
  const {
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
  } = useMenus();

  return (

    <div className="p-12 space-y-12 max-w-7xl mx-auto w-full min-h-[calc(100vh-5rem)] bg-surface text-on-surface animate-in fade-in duration-500">
      
      {/* SECTION: ACTIVE MENUS */}
      <section className="space-y-6">
        <div className="flex items-end justify-between border-b border-outline-variant/10 pb-4">
          <div>
            <h1 className="font-headline text-3xl font-extrabold tracking-tight text-on-surface">Active Menus</h1>
            <p className="text-on-surface-variant mt-1 text-sm font-body">Manage seasonal collections and specialized dining experiences.</p>
          </div>
          <button 
            onClick={() => { setIsMenuModalOpen(true); setEditingMenuId(null); setMenuFormData({name:'', description:''}); }}
            className="text-primary text-xs font-bold uppercase tracking-widest flex items-center gap-2 hover:bg-primary/10 px-4 py-2 transition-colors rounded-sm"
          >
            <span className="material-symbols-outlined text-lg">add_circle</span>
            Create Collection
          </button>
        </div>

        {/* Bento Grid for Menus */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {menus.map((menu, idx) => (
            <div 
               key={menu.id} 
               onClick={() => setActiveMenuId(menu.id)}
               className={cn(
                 "p-8 rounded-xl group relative overflow-hidden transition-all cursor-pointer border hover:-translate-y-1 shadow-sm",
                 activeMenuId === menu.id 
                   ? "bg-surface-container-highest border-primary/30" 
                   : "bg-surface-container-low border-outline-variant/10 hover:border-primary/20"
               )}
            >
              <div className={cn("absolute top-0 left-0 w-1 h-full bg-primary transform transition-transform", activeMenuId === menu.id ? "translate-x-0" : "-translate-x-full group-hover:translate-x-0")}></div>
              <div className="flex justify-between items-start mb-6">
                <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-on-surface-variant opacity-60">
                  {idx === 0 ? 'MAIN SELECTION' : 'SPECIALTY'}
                </span>
                <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button onClick={(e) => { e.stopPropagation(); openEditMenu(menu); }} className="material-symbols-outlined text-sm cursor-pointer hover:text-primary transition-colors">edit</button>
                </div>
              </div>
              <h3 className="font-headline text-2xl font-bold mb-2 truncate" title={menu.name}>{menu.name}</h3>
              <p className="text-on-surface-variant text-sm leading-relaxed mb-8 line-clamp-2">{menu.description || 'No description provided.'}</p>
              
              <div className="flex items-center justify-between mt-auto pt-6 border-t border-outline-variant/10">
                <span className="text-xs font-mono text-primary">{menu.dishes?.length || 0} ITEMS</span>
                <button className={cn("px-4 py-2 text-[10px] font-bold uppercase tracking-widest transition-all rounded-sm", activeMenuId === menu.id ? "bg-primary text-on-primary" : "bg-primary/10 text-primary hover:bg-primary hover:text-on-primary")}>
                  Manage Dishes
                </button>
              </div>
            </div>
          ))}

          {/* Add Archive / Drafts card placeholder */}
          <div className="bg-surface-container-lowest border border-outline-variant/10 p-8 rounded-xl group flex flex-col items-center justify-center border-dashed min-h-[240px] hover:border-primary/40 transition-all cursor-not-allowed opacity-50">
             <span className="material-symbols-outlined text-3xl text-on-surface-variant mb-3">auto_awesome_motion</span>
             <span className="text-xs font-bold uppercase tracking-widest text-on-surface-variant">Archive / Drafts</span>
          </div>
        </div>
      </section>

      {/* SECTION: DISH MANAGEMENT */}
      <section className="space-y-6 pt-6">
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between border-b border-outline-variant/10 pb-4 gap-4">
          <div className="flex items-center gap-4">
            <h2 className="font-headline text-xl font-bold text-on-surface whitespace-nowrap">
              Dish Inventory: <span className="text-primary">{activeMenu?.name || '...'}</span>
            </h2>
            <div className="hidden md:block h-4 w-[1px] bg-outline-variant/20"></div>
          </div>
          
          <div className="flex w-full md:w-auto gap-4 justify-between md:justify-end">
            <div className="relative group flex-1 md:w-64">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 material-symbols-outlined text-on-surface-variant text-sm">search</span>
              <input 
                type="text"
                placeholder="Search dishes..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="bg-surface-container-low border border-outline-variant/10 rounded-sm pl-9 pr-4 py-2 text-sm focus:ring-1 focus:ring-primary w-full text-on-surface transition-shadow"
              />
            </div>
            {activeMenuId && (
              <button 
                onClick={() => { setIsDishModalOpen(true); setEditingDishId(null); setDishFormData({ name: '', description: '', price: '', menuId: activeMenuId, imageUrl: '', dishType: 'Standard' }); }}
                className="bg-primary-container text-on-primary-fixed text-xs font-bold uppercase tracking-widest flex items-center gap-2 px-6 py-2 shadow-lg shadow-primary/10 hover:shadow-primary/20 transition-all rounded-sm whitespace-nowrap shrink-0 hover:brightness-110 active:scale-95"
              >
                <span className="material-symbols-outlined text-lg">add</span>
                Add Dish
              </button>
            )}
          </div>
        </div>

        {/* Dishes Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {filteredDishes.map((dish) => (
             <div key={dish.id} className="bg-surface-container-low rounded-xl overflow-hidden flex flex-col group border border-transparent hover:border-outline-variant/20 shadow-sm transition-all hover:shadow-[0_8px_24px_rgba(0,0,0,0.2)]">
                {/* Image Section */}
                <div className="aspect-[4/3] relative overflow-hidden bg-surface-container-highest">
                  {dish.imageUrl ? (
                    <img src={dish.imageUrl} alt={dish.name} className="w-full h-full object-cover opacity-80 group-hover:scale-110 transition-transform duration-700" />
                  ) : (
                    <div className="w-full h-full flex items-center justify-center text-on-surface-variant text-4xl opacity-50 group-hover:scale-110 transition-transform duration-700">🍽️</div>
                  )}
                  
                  <div className="absolute top-3 left-3">
                    <span className="bg-surface-container-highest/90 backdrop-blur-md text-[9px] font-bold tracking-widest text-primary px-2 py-1 uppercase rounded-sm border border-primary/20 shadow-sm">
                      {dish.dishType.toUpperCase() || 'STANDARD'}
                    </span>
                  </div>
                  <div className="absolute inset-0 bg-gradient-to-t from-background to-transparent opacity-60"></div>
                  <div className="absolute bottom-3 right-3 text-primary font-headline font-bold text-lg drop-shadow-md">
                    ${dish.price?.toLocaleString()}
                  </div>
                </div>

                {/* Content Section */}
                <div className="p-5 flex-1 flex flex-col">
                  <h4 className="font-headline font-bold text-lg text-on-surface mb-1 line-clamp-1" title={dish.name}>{dish.name}</h4>
                  <p className="text-on-surface-variant text-xs line-clamp-2 leading-relaxed mb-6" title={dish.description}>{dish.description || 'No description provided.'}</p>
                  
                  <div className="flex items-center justify-between mt-auto border-t border-outline-variant/10 pt-4">
                    <div className="flex items-center gap-2">
                       <div className={cn("w-1.5 h-1.5 rounded-full", dish.dishType === 'Popular' ? 'bg-primary shadow-[0_0_8px_rgba(245,158,11,0.6)]' : 'bg-tertiary-container shadow-[0_0_8px_rgba(26,189,255,0.6)]')}></div>
                       <span className="text-[9px] font-bold uppercase text-on-surface-variant tracking-wider">Available</span>
                    </div>
                    <div className="flex gap-1">
                      <button onClick={() => openEditDish(dish)} className="p-1.5 hover:bg-surface-container-highest text-on-surface-variant hover:text-primary transition-colors rounded-sm">
                        <span className="material-symbols-outlined text-sm">edit</span>
                      </button>
                      <button onClick={() => handleDeleteDish(dish.id)} className="p-1.5 hover:bg-error-container/20 text-on-surface-variant hover:text-error transition-colors rounded-sm">
                        <span className="material-symbols-outlined text-sm">delete</span>
                      </button>
                    </div>
                  </div>
                </div>
             </div>
          ))}

          {/* Add New Dish Placeholder block */}
          {activeMenuId && (
            <div onClick={() => { setIsDishModalOpen(true); setEditingDishId(null); setDishFormData({ name: '', description: '', price: '', menuId: activeMenuId, imageUrl: '', dishType: 'Standard' }); }} className="bg-surface-container-lowest border-2 border-dashed border-outline-variant/10 rounded-xl flex flex-col items-center justify-center p-8 group hover:border-primary/20 transition-all cursor-pointer min-h-[300px]">
               <div className="w-12 h-12 rounded-full border border-outline-variant/20 flex items-center justify-center mb-4 group-hover:bg-primary/10 group-hover:border-primary transition-all">
                  <span className="material-symbols-outlined text-on-surface-variant group-hover:text-primary transition-colors">add</span>
               </div>
               <span className="text-[10px] font-bold uppercase tracking-widest text-on-surface-variant text-center">Add New Item to<br/>{activeMenu?.name}</span>
            </div>
          )}
        </div>
      </section>

      {/* --- MODALS --- (Identical internal logic, visually refactored to M3) */}
      
      {/* Menu Modal */}
      {isMenuModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-md rounded-xl shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/20 p-8 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-xl font-headline font-bold text-on-surface tracking-tight uppercase">
                {editingMenuId ? 'Edit Menu Section' : 'Add New Menu'}
              </h2>
              <button type="button" onClick={() => setIsMenuModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition bg-surface-container-low p-2 rounded-full hover:bg-surface-container">
                <span className="material-symbols-outlined text-sm">close</span>
              </button>
            </div>
            
            <form onSubmit={handleMenuSubmit} className="space-y-6">
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Menu Title</label>
                <input required value={menuFormData.name} onChange={e => setMenuFormData({...menuFormData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface shadow-inner" placeholder="e.g. Dessert Selection" />
              </div>
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Gastronomic Description</label>
                <textarea required value={menuFormData.description} onChange={e => setMenuFormData({...menuFormData, description: e.target.value})} className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface resize-none shadow-inner" rows="3" placeholder="A brief narrative about this collection." />
              </div>

              <div className="pt-6 flex justify-end gap-3 border-t border-outline-variant/10 mt-8">
                <button type="button" onClick={() => setIsMenuModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">Cancel</button>
                <button type="submit" disabled={saving} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl shadow-primary/20 hover:brightness-110 active:scale-95 transition-all disabled:opacity-50 flex items-center gap-2">
                  {saving && <span className="material-symbols-outlined animate-spin text-sm">sync</span>}
                  {saving ? 'Saving...' : (editingMenuId ? 'Save Changes' : 'Create Menu')}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Dish Modal */}
      {isDishModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-lg rounded-xl shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/20 p-8 animate-in zoom-in-95 max-h-[90vh] overflow-y-auto hide-scrollbar">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-xl font-headline font-bold text-on-surface uppercase tracking-tight">{editingDishId ? 'Edit Culinary Item' : 'New Culinary Item'}</h3>
              <button onClick={() => setIsDishModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition bg-surface-container-low p-2 rounded-full hover:bg-surface-container">
                <span className="material-symbols-outlined text-sm">close</span>
              </button>
            </div>
            
            <form onSubmit={handleDishSubmit} className="space-y-6">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2 col-span-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Dish Nomenclature</label>
                  <input required value={dishFormData.name} onChange={e => setDishFormData({...dishFormData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface shadow-inner" placeholder="Wagyu Carpaccio" />
                </div>
                
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Price (USD)</label>
                  <div className="relative">
                    <span className="absolute left-3 top-1/2 -translate-y-1/2 text-on-surface-variant font-bold">$</span>
                    <input required min="0" step="0.01" value={dishFormData.price} onChange={e => setDishFormData({...dishFormData, price: e.target.value})} type="number" className="w-full bg-surface-container-low border-none rounded-sm pl-8 pr-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface shadow-inner" placeholder="24.00" />
                  </div>
                </div>

                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Target Menu</label>
                  <select required value={dishFormData.menuId} onChange={e => setDishFormData({...dishFormData, menuId: e.target.value})} className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface shadow-inner appearance-none">
                    <option value="" disabled className="text-background">Select menu...</option>
                    {menus.map(m => (
                      <option key={m.id} value={m.id} className="bg-surface-container-high">{m.name}</option>
                    ))}
                  </select>
                </div>
              </div>
              
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Detailed Ingredients / Description</label>
                <textarea required value={dishFormData.description} onChange={e => setDishFormData({...dishFormData, description: e.target.value})} className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface resize-none shadow-inner" rows="3" placeholder="Paper-thin wagyu, truffle oil, micro-arugula..." />
              </div>

              <div className="grid grid-cols-2 gap-4 items-end">
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Label / Type</label>
                  <select value={dishFormData.dishType} onChange={e => setDishFormData({...dishFormData, dishType: e.target.value})} className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all outline-none text-on-surface shadow-inner appearance-none">
                    <option value="Standard" className="bg-surface-container-high">Standard</option>
                    <option value="Popular" className="bg-surface-container-high">Signature / Popular</option>
                  </select>
                </div>

                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Plate Photography</label>
                  <div className="flex flex-col gap-2">
                    <input type="file" accept="image/*" onChange={handleImageChange} className="text-xs text-on-surface-variant file:mr-4 file:py-2 file:px-4 file:rounded-sm file:border-0 file:text-xs file:font-semibold file:bg-primary/10 file:text-primary hover:file:bg-primary/20 file:transition-colors file:cursor-pointer cursor-pointer border border-outline-variant/20 rounded-sm w-full bg-surface-container-low" />
                  </div>
                </div>
              </div>

               {/* Full Image Preview rendering inline in the form for dramatic premium effect */}
               {dishFormData.imageUrl && (
                  <div className="w-full aspect-video rounded-md overflow-hidden border border-outline-variant/10 shadow-inner mt-4 relative group">
                    <img src={dishFormData.imageUrl} alt="Dish Preview" className="w-full h-full object-cover" />
                    {uploadingImage && (
                      <div className="absolute inset-0 bg-background/50 flex items-center justify-center backdrop-blur-sm">
                        <span className="material-symbols-outlined animate-spin text-primary text-3xl">sync</span>
                      </div>
                    )}
                  </div>
                )}
                {!dishFormData.imageUrl && uploadingImage && (
                  <div className="w-full p-4 bg-surface-container-low border border-dashed border-outline-variant/20 text-center rounded-sm text-primary text-xs flex items-center justify-center gap-2">
                    <span className="material-symbols-outlined animate-spin text-sm">sync</span> Uploading to Nocturnal Storage...
                  </div>
                )}

              <div className="pt-6 flex justify-end gap-3 mt-8">
                <button type="button" onClick={() => setIsDishModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">Cancelar</button>
                <button type="submit" disabled={saving || uploadingImage} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl shadow-primary/20 hover:brightness-110 active:scale-95 transition-all disabled:opacity-50 flex items-center gap-2">
                  {(saving) && <span className="material-symbols-outlined animate-spin text-sm">sync</span>}
                  {saving ? 'Processing...' : (editingDishId ? 'Save Edits' : 'Catalog Dish')}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
