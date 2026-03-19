import { useState, useEffect } from 'react';
import { getOrders, getClients } from '../lib/api';

export default function Dashboard() {
  const [orders, setOrders] = useState([]);
  const [totalClients, setTotalClients] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const [ordersRes, clientsRes] = await Promise.all([
        getOrders(),
        getClients()
      ]);
      setOrders(ordersRes.data.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate)));
      setTotalClients(clientsRes.data.length);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const todayOrders = orders.filter(o => new Date(o.orderDate).toDateString() === new Date().toDateString());
  const todayRevenue = todayOrders.reduce((sum, order) => sum + order.totalPrice, 0);

  // Parse popular dish using simple frequency map
  const dishCounts = {};
  orders.forEach(order => {
    order.dishes.forEach(dish => {
      dishCounts[dish.name] = (dishCounts[dish.name] || 0) + 1;
    });
  });
  let popularDishName = "Cargando...";
  let maxCount = 0;
  Object.keys(dishCounts).forEach(name => {
    if (dishCounts[name] > maxCount) {
      maxCount = dishCounts[name];
      popularDishName = name;
    }
  });

  if (orders.length === 0) popularDishName = "N/A";

  const stats = [
    { label: 'Ventas de hoy', value: `$${todayRevenue.toLocaleString()}`, icon: 'payments' },
    { label: 'Órdenes de hoy', value: todayOrders.length.toString(), icon: 'receipt_long' },
    { label: 'Total Clientes', value: totalClients.toString(), icon: 'groups' },
    { label: 'Plato más vendido', value: popularDishName, icon: 'local_fire_department' },
  ];

  return (
    <div className="p-8 space-y-8 h-full bg-surface overflow-y-auto hide-scrollbar text-on-surface">
      <div>
        <h1 className="text-3xl font-headline font-semibold tracking-tight">Dashboard Overview</h1>
        <p className="text-on-surface-variant font-body mt-1">Operational status for the current dinner service.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, i) => (
          <div key={i} className="bg-surface-container-high rounded-md p-6 shadow-[0_4px_12px_rgba(0,0,0,0.1)] transition-transform hover:-translate-y-1">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-xs font-semibold uppercase tracking-widest text-on-surface-variant mb-2">{stat.label}</p>
                <h3 className="text-2xl font-headline font-bold truncate max-w-[150px]" title={stat.value}>{stat.value}</h3>
              </div>
              <div className="w-12 h-12 bg-primary-container/10 border border-primary-container/20 rounded-full flex items-center justify-center text-primary-container shrink-0">
                <span className="material-symbols-outlined">{stat.icon}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Orders Table */}
        <div className="lg:col-span-2 bg-surface-container-low rounded-md p-6">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-headline font-semibold">Órdenes Recientes</h3>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-on-surface-variant border-b border-outline-variant/20 uppercase tracking-widest text-[10px] font-bold">
                <tr>
                  <th className="pb-3 font-medium">Ticket #</th>
                  <th className="pb-3 font-medium">Guest</th>
                  <th className="pb-3 font-medium">Timestamp</th>
                  <th className="pb-3 font-medium">Status</th>
                  <th className="pb-3 font-medium text-right">Total</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-outline-variant/10 font-body">
                {loading ? (
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-outline">
                       <span className="material-symbols-outlined animate-spin text-2xl">sync</span>
                    </td>
                  </tr>
                ) : orders.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-on-surface-variant">No tickets generated yet.</td>
                  </tr>
                ) : (
                  orders.slice(0, 5).map((order) => {
                    const d = new Date(order.orderDate);
                    return (
                      <tr key={order.id} className="group hover:bg-surface-container-high transition-colors">
                        <td className="py-4 font-mono font-bold text-xs text-outline max-w-[80px] truncate">100{order.id}</td>
                        <td className="py-4 truncate max-w-[150px] font-medium">{order.client?.name} {order.client?.lastName}</td>
                        <td className="py-4 text-on-surface-variant flex items-center gap-2 text-xs">
                          {d.toLocaleDateString()} {d.getHours().toString().padStart(2, '0')}:{d.getMinutes().toString().padStart(2, '0')}
                        </td>
                        <td className="py-4">
                          <div className="flex items-center gap-2">
                             <div className="w-1.5 h-1.5 rounded-full bg-tertiary shadow-[0_0_8px_#8fd5ff]"></div>
                             <span className="text-[10px] uppercase tracking-tighter font-bold text-on-surface-variant">Completed</span>
                          </div>
                        </td>
                        <td className="py-4 text-right font-headline font-bold text-primary">
                          ${order.totalPrice?.toLocaleString()}
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-surface-container-low rounded-md p-6">
          <h3 className="text-lg font-headline font-semibold mb-6">Concierge Actions</h3>
          <div className="flex flex-col gap-3">
             <a href="/pos" className="w-full flex items-center justify-center gap-2 bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed shadow-xl hover:brightness-110 py-3 rounded-sm font-bold tracking-widest uppercase text-xs transition-all active:scale-95">
              <span className="material-symbols-outlined text-sm">receipt_long</span>
              New Order Desk
            </a>
            <a href="/menus" className="w-full flex items-center justify-start gap-3 bg-surface-container-highest text-on-surface hover:bg-surface-bright py-3 px-4 rounded-sm font-semibold text-sm transition-colors border border-outline-variant/10">
              <span className="material-symbols-outlined text-on-surface-variant text-sm">restaurant_menu</span>
              Check Menu & Availability
            </a>
            <a href="/clients" className="w-full flex items-center justify-start gap-3 bg-surface-container-highest text-on-surface hover:bg-surface-bright py-3 px-4 rounded-sm font-semibold text-sm transition-colors border border-outline-variant/10">
              <span className="material-symbols-outlined text-on-surface-variant text-sm">group_add</span>
              Register VIP
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
