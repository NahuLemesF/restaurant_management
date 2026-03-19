import { useState, useEffect } from 'react';
import axios from 'axios';
import { Plus, Search, MoreVertical, Edit2, Trash2, Mail, Hash } from 'lucide-react';
import { cn } from '../lib/utils';

export default function Clients() {
  const [clients, setClients] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  // Mock initial data until backend is fully connected
  useEffect(() => {
    setTimeout(() => {
      setClients([
        { id: 1, name: 'Juan', lastName: 'Pérez', email: 'juan.perez@email.com', clientType: 'FREQUENT', orderCount: 15 },
        { id: 2, name: 'María', lastName: 'Gómez', email: 'maria.g@email.com', clientType: 'PARTICULAR', orderCount: 3 },
        { id: 3, name: 'Carlos', lastName: 'López', email: 'carlos.l@email.com', clientType: 'FREQUENT', orderCount: 22 },
        { id: 4, name: 'Ana', lastName: 'Martínez', email: 'ana.m@email.com', clientType: 'PARTICULAR', orderCount: 1 },
      ]);
      setLoading(false);
    }, 500);
  }, []);

  const filteredClients = clients.filter(client => 
    `${client.name} ${client.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
    client.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-8 pb-20 space-y-6 animate-in fade-in duration-500">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Clientes</h1>
          <p className="text-muted-foreground mt-1">Administra la base de clientes del restaurante.</p>
        </div>
        <button className="flex items-center gap-2 bg-primary text-primary-foreground px-4 py-2.5 rounded-lg hover:bg-primary/90 font-medium transition-colors shadow-sm">
          <Plus className="w-5 h-5" />
          Nuevo Cliente
        </button>
      </div>

      <div className="bg-card border border-border rounded-xl shadow-sm flex flex-col">
        {/* Toolbar */}
        <div className="p-4 border-b border-border flex items-center justify-between">
          <div className="relative w-full max-w-sm">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
            <input 
              type="text"
              placeholder="Buscar por nombre o email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-9 pr-4 py-2 bg-background border border-border rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-primary/50 transition-shadow"
            />
          </div>
        </div>

        {/* Table */}
        <div className="overflow-x-auto">
          <table className="w-full text-sm text-left">
            <thead className="bg-muted/50 text-muted-foreground text-xs uppercase font-medium">
              <tr>
                <th className="px-6 py-4">Cliente</th>
                <th className="px-6 py-4">Contacto</th>
                <th className="px-6 py-4">Estado</th>
                <th className="px-6 py-4">Órdenes</th>
                <th className="px-6 py-4 text-right">Acciones</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {loading ? (
                <tr>
                  <td colSpan="5" className="px-6 py-12 text-center text-muted-foreground">
                    <div className="flex flex-col items-center justify-center gap-2">
                       <span className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></span>
                       Cargando clientes...
                    </div>
                  </td>
                </tr>
              ) : filteredClients.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-6 py-8 text-center text-muted-foreground">
                    No se encontraron clientes con esos parámetros.
                  </td>
                </tr>
              ) : (
                filteredClients.map((client) => (
                  <tr key={client.id} className="group hover:bg-muted/30 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-full bg-secondary flex items-center justify-center text-secondary-foreground font-semibold">
                          {client.name.charAt(0)}{client.lastName.charAt(0)}
                        </div>
                        <div>
                          <p className="font-medium text-foreground">{client.name} {client.lastName}</p>
                          <p className="text-xs text-muted-foreground flex items-center gap-1 mt-0.5">
                            <Hash className="w-3 h-3" /> ID: {client.id}
                          </p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors">
                        <Mail className="w-4 h-4" />
                        <span>{client.email}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn(
                        "px-2.5 py-1 rounded-full text-xs font-semibold tracking-wide",
                        client.clientType === 'FREQUENT' 
                          ? "bg-amber-500/10 text-amber-500 border border-amber-500/20" 
                          : "bg-zinc-500/10 text-zinc-500 border border-zinc-500/20"
                      )}>
                        {client.clientType === 'FREQUENT' ? 'FRECUENTE 👑' : 'REGULAR'}
                      </span>
                    </td>
                    <td className="px-6 py-4 font-medium">
                      {client.orderCount}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button className="p-2 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-lg transition-colors">
                          <Edit2 className="w-4 h-4" />
                        </button>
                        <button className="p-2 text-muted-foreground hover:text-destructive hover:bg-destructive/10 rounded-lg transition-colors">
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
