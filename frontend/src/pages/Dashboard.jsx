import { useState } from 'react';
import { Users, UtensilsCrossed, ReceiptText, TrendingUp, DollarSign, Clock } from 'lucide-react';

export default function Dashboard() {
  const stats = [
    { label: 'Ventas de hoy', value: '$12,450', icon: DollarSign, trend: '+14%', trendUp: true },
    { label: 'Órdenes', value: '48', icon: ReceiptText, trend: '+5%', trendUp: true },
    { label: 'Nuevos Clientes', value: '12', icon: Users, trend: '-2%', trendUp: false },
    { label: 'Plato más popular', value: 'Milanesa c/ Fritas', icon: TrendingUp },
  ];

  const recentOrders = [
    { id: '1024', time: '12:45', total: '$1,200', status: 'Completada', client: 'Juan Pérez' },
    { id: '1025', time: '13:10', total: '$3,400', status: 'En preparación', client: 'María Gómez' },
    { id: '1026', time: '13:15', total: '$850', status: 'Recibida', client: 'Carlos López' },
  ];

  return (
    <div className="p-8 space-y-8 animate-in fade-in duration-500">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
        <p className="text-muted-foreground mt-1">Resumen de la actividad del restaurante de hoy.</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, i) => (
          <div key={i} className="bg-card border border-border rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground mb-1">{stat.label}</p>
                <h3 className="text-2xl font-bold">{stat.value}</h3>
              </div>
              <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary">
                <stat.icon className="w-6 h-6" />
              </div>
            </div>
            {stat.trend && (
              <div className="mt-4 flex items-center text-sm">
                <span className={stat.trendUp ? "text-emerald-500 font-medium" : "text-destructive font-medium"}>
                  {stat.trend}
                </span>
                <span className="text-muted-foreground ml-2">vs ayer</span>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Orders & Shortcuts */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Orders Table */}
        <div className="lg:col-span-2 bg-card border border-border rounded-xl p-6 shadow-sm">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-lg font-bold">Órdenes Recientes</h3>
            <button className="text-sm text-primary hover:underline font-medium">Ver todas</button>
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
                {recentOrders.map((order) => (
                  <tr key={order.id} className="group hover:bg-muted/50 transition-colors">
                    <td className="py-4 font-medium">{order.id}</td>
                    <td className="py-4">{order.client}</td>
                    <td className="py-4 text-muted-foreground flex items-center gap-2">
                      <Clock className="w-4 h-4" /> {order.time}
                    </td>
                    <td className="py-4">
                      <span className={`px-2.5 py-1 rounded-full text-xs font-medium 
                        ${order.status === 'Completada' ? 'bg-emerald-500/10 text-emerald-500' : 
                          order.status === 'En preparación' ? 'bg-amber-500/10 text-amber-500' : 
                          'bg-blue-500/10 text-blue-500'}`}>
                        {order.status}
                      </span>
                    </td>
                    <td className="py-4 text-right font-medium">{order.total}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
          <h3 className="text-lg font-bold mb-6">Acciones Rápidas</h3>
          <div className="flex flex-col gap-3">
            <button className="w-full flex items-center justify-center gap-2 bg-primary text-primary-foreground hover:bg-primary/90 py-3 rounded-lg font-medium transition-colors">
              <ReceiptText className="w-5 h-5" />
              Nueva Orden (POS)
            </button>
            <button className="w-full flex items-center justify-start gap-3 bg-secondary text-secondary-foreground hover:bg-secondary/80 py-3 px-4 rounded-lg font-medium transition-colors">
              <UtensilsCrossed className="w-5 h-5 text-muted-foreground" />
              Ver Menú del Día
            </button>
            <button className="w-full flex items-center justify-start gap-3 bg-secondary text-secondary-foreground hover:bg-secondary/80 py-3 px-4 rounded-lg font-medium transition-colors">
              <Users className="w-5 h-5 text-muted-foreground" />
              Registrar Cliente
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
