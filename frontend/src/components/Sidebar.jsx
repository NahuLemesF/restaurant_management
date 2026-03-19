import { NavLink } from 'react-router-dom';
import { cn } from '../lib/utils';

export default function Sidebar() {
  const NAV_LINKS = [
    {
      category: 'Ventas',
      items: [
        { icon: 'dashboard', label: 'Resumen', path: '/dashboard' },
        { icon: 'point_of_sale', label: 'Punto de Venta', path: '/pos' },
      ]
    },
    {
      category: 'Gestión',
      items: [
        { icon: 'groups', label: 'Clientes', path: '/clients' },
        { icon: 'restaurant_menu', label: 'Menús y Platos', path: '/menus' },
      ]
    }
  ];

  return (
    <aside className="fixed left-0 top-0 flex flex-col h-screen w-64 border-r-0 bg-surface-container-low font-headline tracking-tight z-50">
      <div className="p-8 flex flex-col gap-8 h-full">
        {/* Brand Header */}
        <div className="text-xl font-bold text-on-surface uppercase tracking-widest leading-none">
          The Nocturnal
          <span className="block text-[10px] font-medium tracking-normal text-on-surface-variant normal-case mt-1 opacity-60">
            Dinner Service
          </span>
        </div>

        {/* Navigation Links */}
        <nav className="flex flex-col gap-4 flex-grow mt-4">
          {NAV_LINKS.map((category) => (
            <div key={category.category} className="flex flex-col gap-1">
              <span className="px-4 text-xs font-semibold uppercase tracking-wider text-on-surface-variant opacity-70">
                {category.category}
              </span>
              {category.items.map((item) => (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) => cn(
                    "flex items-center gap-4 px-4 py-3 transition-colors duration-200 ease-in-out",
                    isActive 
                      ? "text-primary-container bg-surface-container-high border-l-2 border-primary-container scale-[0.98] font-semibold" 
                      : "text-on-surface-variant hover:text-on-surface hover:bg-surface-container-high/50"
                  )}
                >
                  <span className="material-symbols-outlined">{item.icon}</span>
                  <span className="text-sm">{item.label}</span>
                </NavLink>
              ))}
            </div>
          ))}
        </nav>
      </div>
    </aside>
  );
}
