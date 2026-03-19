import { useState, useEffect } from 'react';
import { Users, UtensilsCrossed, ReceiptText, TrendingUp, DollarSign, Clock } from 'lucide-react';
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
    { label: 'Ventas de hoy', value: `$${todayRevenue.toLocaleString()}`, icon: DollarSign, trend: null },
    { label: 'Órdenes de hoy', value: todayOrders.length.toString(), icon: ReceiptText, trend: null },
    { label: 'Total Clientes', value: totalClients.toString(), icon: Users, trend: null },
    { label: 'Plato más vendido', value: popularDishName, icon: TrendingUp },
  ];

  return (
    <div className="p-8 space-y-8 animate-in fade-in duration-500">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <p className="text-muted-foreground mt-1">Resumen de la actividad del restaurante de hoy.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, i) => (
          <div key={i} className="bg-card border border-border rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground mb-1">{stat.label}</p>
                <h3 className="text-2xl font-bold truncate max-w-[150px]" title={stat.value}>{stat.value}</h3>
              </div>
              <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary shrink-0">
                <stat.icon className="w-6 h-6" />
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Orders Table */}
        <div className="lg:col-span-2 bg-card border border-border rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-bold">Órdenes Recientes</h3>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-muted-foreground border-b border-border">
                <tr>
                  <th className="pb-3 font-medium">Orden #</th>
                  <th className="pb-3 font-medium">Cliente</th>
                  <th className="pb-3 font-medium">Hora</th>
                  <th className="pb-3 font-medium">Estado</th>
                  <th className="pb-3 font-medium text-right">Total</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border">
                {loading ? (
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-muted-foreground">Cargando...</td>
                  </tr>
                ) : orders.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-muted-foreground">No hay órdenes registradas.</td>
                  </tr>
                ) : (
                  orders.slice(0, 5).map((order) => {
                    const d = new Date(order.orderDate);
                    return (
                      <tr key={order.id} className="group hover:bg-muted/50 transition-colors">
                        <td className="py-4 font-medium max-w-[80px] truncate">100{order.id}</td>
                        <td className="py-4 truncate max-w-[150px]">{order.client?.name} {order.client?.lastName}</td>
                        <td className="py-4 text-muted-foreground flex items-center gap-2">
                          <Clock className="w-4 h-4" /> 
                          {d.toLocaleDateString()} {d.getHours().toString().padStart(2, '0')}:{d.getMinutes().toString().padStart(2, '0')}
                        </td>
                        <td className="py-4">
                          <span className="px-2.5 py-1 rounded-full text-xs font-medium bg-emerald-500/10 text-emerald-500">
                            Completada
                          </span>
                        </td>
                        <td className="py-4 text-right font-medium text-primary">
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
        <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
          <h3 className="text-lg font-bold mb-6">Acciones Rápidas</h3>
          <div className="flex flex-col gap-3">
             <a href="/pos" className="w-full flex items-center justify-center gap-2 bg-primary text-primary-foreground hover:bg-primary/90 py-3 rounded-lg font-medium transition-colors">
              <ReceiptText className="w-5 h-5" />
              Nueva Orden (POS)
            </a>
            <a href="/menus" className="w-full flex items-center justify-start gap-3 bg-secondary text-secondary-foreground hover:bg-secondary/80 py-3 px-4 rounded-lg font-medium transition-colors">
              <UtensilsCrossed className="w-5 h-5 text-muted-foreground" />
              Ver Menú del Día
            </a>
            <a href="/clients" className="w-full flex items-center justify-start gap-3 bg-secondary text-secondary-foreground hover:bg-secondary/80 py-3 px-4 rounded-lg font-medium transition-colors">
              <Users className="w-5 h-5 text-muted-foreground" />
              Registrar Cliente
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
