import { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Users,  UtensilsCrossed, ReceiptText, Moon, Sun, ChevronLeft, ChevronRight, Menu as MenuIcon } from 'lucide-react';
import { cn } from '../lib/utils';

export default function Sidebar() {
  const [collapsed, setCollapsed] = useState(false);
  const [isDark, setIsDark] = useState(true);

  const toggleTheme = () => {
    setIsDark(!isDark);
    document.documentElement.classList.toggle('dark');
  };

  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/dashboard' },
    { icon: ReceiptText, label: 'Punto de Venta', path: '/pos' },
    { icon: Users, label: 'Clientes', path: '/clients' },
    { icon: UtensilsCrossed, label: 'Menús & Platos', path: '/menus' },
  ];

  return (
    <aside 
      className={cn(
        "bg-card border-r border-border flex flex-col transition-all duration-300 relative",
        collapsed ? "w-20" : "w-64"
      )}
    >
      {/* Brand Header */}
      <div className="h-16 flex items-center justify-between px-4 border-b border-border">
        {!collapsed && (
          <div className="flex items-center gap-2 overflow-hidden">
            <div className="w-8 h-8 rounded-lg bg-primary flex items-center justify-center text-primary-foreground font-bold">
              R
            </div>
            <span className="font-semibold text-lg whitespace-nowrap">RestoManager</span>
          </div>
        )}
        {collapsed && (
          <div className="w-full flex justify-center">
            <div className="w-8 h-8 rounded-lg bg-primary flex items-center justify-center text-primary-foreground font-bold">
              R
            </div>
          </div>
        )}
      </div>

      {/* Navigation Links */}
      <nav className="flex-1 py-6 px-3 space-y-2 overflow-y-auto">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) => cn(
              "flex items-center gap-3 px-3 py-2.5 rounded-lg transition-colors group relative",
              isActive 
                ? "bg-primary/10 text-primary font-medium" 
                : "text-muted-foreground hover:bg-secondary hover:text-foreground"
            )}
          >
            <item.icon className="w-5 h-5 shrink-0" />
            {!collapsed && <span>{item.label}</span>}
            
            {/* Tooltip for collapsed state */}
            {collapsed && (
              <div className="absolute left-full ml-4 px-2 py-1 bg-popover text-popover-foreground text-sm rounded-md opacity-0 pointer-events-none group-hover:opacity-100 transition-opacity whitespace-nowrap z-50 shadow-md border border-border">
                {item.label}
              </div>
            )}
          </NavLink>
        ))}
      </nav>

      {/* Footer Actions */}
      <div className="p-4 border-t border-border flex flex-col gap-3">
        <button
          onClick={toggleTheme}
          className={cn(
            "flex items-center gap-3 px-3 py-2 rounded-lg transition-colors text-muted-foreground hover:bg-secondary hover:text-foreground",
            collapsed && "justify-center"
          )}
        >
          {isDark ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
          {!collapsed && <span>{isDark ? 'Modo Claro' : 'Modo Oscuro'}</span>}
        </button>

        <button
          onClick={() => setCollapsed(!collapsed)}
          className="flex items-center justify-center w-full py-2 rounded-lg text-muted-foreground hover:bg-secondary transition-colors"
        >
          {collapsed ? <ChevronRight className="w-5 h-5" /> : <ChevronLeft className="w-5 h-5" />}
        </button>
      </div>
    </aside>
  );
}
