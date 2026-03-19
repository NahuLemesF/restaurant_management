import { cn } from '../lib/utils';
import { useClients } from '../application/useClients';
import { frequentPercentage } from '../domain/Client';

export default function Clients() {
  const {
    clients,
    filteredClients,
    loading,
    searchTerm, setSearchTerm,
    isModalOpen, setIsModalOpen,
    formData, setFormData,
    saving,
    handleCreate,
    handleDelete,
  } = useClients();

  const allClientsCount = clients.length;
  const vipPercentageDisplay = frequentPercentage(clients);

  return (
    <div className="p-12 space-y-12 max-w-7xl mx-auto w-full animate-in fade-in duration-500 bg-surface min-h-[calc(100vh-5rem)]">
      {/* Page Header Actions */}
      <div className="flex justify-between items-end">
        <div className="space-y-1">
          <h2 className="text-4xl font-extrabold font-headline tracking-tighter text-on-surface">Client Database</h2>
          <p className="text-on-surface-variant font-body">Manage elite guest profiles and dining frequency.</p>
        </div>
        <div className="flex items-center gap-4">
          <div className="hidden sm:flex bg-surface-container-low p-1 rounded-sm border border-outline-variant/10 shadow-sm">
            <button className="px-4 py-2 text-xs font-bold bg-surface-container-high text-primary rounded-sm uppercase tracking-wider">All Clients</button>
            <button className="px-4 py-2 text-xs font-bold text-on-surface-variant hover:text-on-surface transition-all uppercase tracking-wider">Frequent</button>
          </div>
          <button onClick={() => setIsModalOpen(true)} className="bg-primary-container text-on-primary-fixed px-6 py-2.5 rounded-sm font-bold flex items-center gap-2 hover:brightness-110 transition-all shadow-lg shadow-primary/10">
            <span className="material-symbols-outlined text-lg">person_add</span>
            Add Client
          </button>
        </div>
      </div>

      {/* Quick Search */}
      <div className="relative group max-w-md">
        <span className="absolute left-3 top-1/2 -translate-y-1/2 material-symbols-outlined text-on-surface-variant text-lg">search</span>
        <input 
          type="text"
          placeholder="Search clients, emails..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="bg-surface-container-low border border-outline-variant/10 rounded-sm pl-10 pr-4 py-2.5 text-sm w-full focus:ring-1 focus:ring-primary-container focus:border-primary-container transition-all text-on-surface placeholder:text-on-surface-variant/50 shadow-sm"
        />
      </div>

      {/* Client Table Section */}
      <div className="bg-surface-container-low rounded-xl overflow-hidden border border-outline-variant/10 shadow-[0_8px_24px_rgba(0,0,0,0.2)]">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-surface-container border-b border-outline-variant/10">
                <th className="px-8 py-5 text-xs font-black text-on-surface-variant uppercase tracking-widest">ID</th>
                <th className="px-8 py-5 text-xs font-black text-on-surface-variant uppercase tracking-widest">First Name</th>
                <th className="px-8 py-5 text-xs font-black text-on-surface-variant uppercase tracking-widest">Last Name</th>
                <th className="px-8 py-5 text-xs font-black text-on-surface-variant uppercase tracking-widest">Email Address</th>
                <th className="px-8 py-5 text-xs font-black text-on-surface-variant uppercase tracking-widest text-right">Client Type</th>
                <th className="px-8 py-5"></th>
              </tr>
            </thead>
            <tbody className="font-body text-sm divide-y divide-outline-variant/5">
              {loading ? (
                <tr>
                  <td colSpan="6" className="px-8 py-12 text-center text-on-surface-variant">
                    <div className="flex flex-col items-center justify-center gap-2">
                       <span className="material-symbols-outlined animate-spin text-primary text-2xl">sync</span>
                       Loading directory...
                    </div>
                  </td>
                </tr>
              ) : filteredClients.length === 0 ? (
                <tr>
                  <td colSpan="6" className="px-8 py-12 text-center text-on-surface-variant">
                    No guests matched your criteria.
                  </td>
                </tr>
              ) : (
                filteredClients.map((client) => (
                  <tr key={client.id} className="hover:bg-surface-container-high transition-colors group">
                    <td className="px-8 py-6 text-on-surface-variant font-mono">#NC-{client.id}</td>
                    <td className="px-8 py-6 font-semibold text-on-surface">{client.name}</td>
                    <td className="px-8 py-6 font-semibold text-on-surface">{client.lastName}</td>
                    <td className="px-8 py-6 text-on-surface-variant">{client.email}</td>
                    <td className="px-8 py-6 text-right">
                      <span className={cn(
                        "inline-flex items-center gap-2 px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-tighter",
                        client.clientType === 'Frecuente' 
                          ? "bg-primary-container/10 border border-primary/20 text-primary" 
                          : "bg-secondary-container/30 border border-secondary/10 text-on-surface-variant"
                      )}>
                        {client.clientType === 'Frecuente' && <span className="w-1.5 h-1.5 rounded-full bg-primary animate-pulse"></span>}
                        {client.clientType === 'Comun' && <span className="w-1.5 h-1.5 rounded-full bg-on-surface-variant/40"></span>}
                        {client.clientType === 'Frecuente' ? 'FREQUENT' : 'NORMAL'}
                      </span>
                    </td>
                    <td className="px-8 py-6 text-right">
                      <button onClick={() => handleDelete(client.id)} className="opacity-0 group-hover:opacity-100 p-2 text-on-surface-variant hover:text-error transition-all rounded-sm hover:bg-error-container/20">
                        <span className="material-symbols-outlined text-sm">delete</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
        {/* Pagination / Table Footer */}
        <div className="px-8 py-5 bg-surface-container border-t border-outline-variant/10 flex justify-between items-center">
          <p className="text-xs text-on-surface-variant font-body">Showing <span className="font-bold text-on-surface">{filteredClients.length}</span> of <span className="font-bold text-on-surface">{allClientsCount}</span> premium clients</p>
          <div className="flex items-center gap-2 opacity-50 cursor-not-allowed">
            <button className="p-2 text-on-surface-variant"><span className="material-symbols-outlined text-lg">chevron_left</span></button>
            <div className="flex gap-1">
              <button className="w-8 h-8 rounded-sm bg-primary-container text-on-primary-fixed text-xs font-bold">1</button>
            </div>
            <button className="p-2 text-on-surface-variant"><span className="material-symbols-outlined text-lg">chevron_right</span></button>
          </div>
        </div>
      </div>

      {/* Quick Insight Bento Section */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-surface-container-low p-6 rounded-xl flex flex-col gap-4 shadow-sm border border-outline-variant/5 hover:-translate-y-1 transition-transform">
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-tertiary">star</span>
            <h3 className="text-xs font-black uppercase tracking-widest text-on-surface-variant">VIP Concentration</h3>
          </div>
          <div className="flex items-end justify-between">
            <span className="text-4xl font-headline font-extrabold">{vipPercentageDisplay}%</span>
            <span className="text-xs text-primary font-bold">Of total guest base</span>
          </div>
          <div className="w-full h-1.5 bg-surface-container-highest rounded-full overflow-hidden">
            <div className="h-full bg-tertiary transition-all" style={{ width: `${vipPercentageDisplay}%` }}></div>
          </div>
        </div>

        <div className="bg-surface-container-low p-6 rounded-xl flex flex-col gap-4 shadow-sm border border-outline-variant/5 hover:-translate-y-1 transition-transform">
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-primary">analytics</span>
            <h3 className="text-xs font-black uppercase tracking-widest text-on-surface-variant">Retention Rate</h3>
          </div>
          <div className="flex items-end justify-between">
            <span className="text-4xl font-headline font-extrabold">88.2%</span>
            <span className="text-xs text-tertiary font-bold">Peak Efficiency</span>
          </div>
          <div className="w-full h-1.5 bg-surface-container-highest rounded-full overflow-hidden">
            <div className="h-full bg-primary w-[88%]"></div>
          </div>
        </div>

        <div className="bg-surface-container-low p-6 rounded-xl flex flex-col gap-4 shadow-sm border border-outline-variant/5 hover:-translate-y-1 transition-transform">
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-on-surface">groups_2</span>
            <h3 className="text-xs font-black uppercase tracking-widest text-on-surface-variant">Total Database</h3>
          </div>
          <div className="flex items-end justify-between">
            <span className="text-4xl font-headline font-extrabold">{allClientsCount}</span>
            <span className="text-xs text-on-surface-variant">Verified profiles</span>
          </div>
          <div className="w-full h-1.5 bg-surface-container-highest rounded-full overflow-hidden flex">
            <div className="h-full bg-primary transition-all" style={{ width: `${allClientsCount > 0 ? 30 : 0}%` }}></div>
            <div className="h-full bg-tertiary transition-all" style={{ width: `${allClientsCount > 0 ? 20 : 0}%` }}></div>
          </div>
        </div>
      </div>

      {/* CREATE MODAL */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0e0e0e]/80 backdrop-blur-sm animate-in fade-in">
          <div className="bg-surface-container-highest w-full max-w-md rounded-xl shadow-[0_20px_40px_rgba(14,14,14,0.4)] border border-outline-variant/10 p-8 animate-in zoom-in-95">
            <div className="flex justify-between items-center mb-8 border-b border-outline-variant/10 pb-4">
              <h3 className="text-xl font-headline font-bold text-on-surface tracking-tight">Register Guest</h3>
              <button onClick={() => setIsModalOpen(false)} className="text-on-surface-variant hover:text-on-surface transition bg-surface-container-low p-2 rounded-full hover:bg-surface-container">
                <span className="material-symbols-outlined text-sm">close</span>
              </button>
            </div>
            
            <form onSubmit={handleCreate} className="space-y-6">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">First Name</label>
                  <input required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} type="text" className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all text-on-surface shadow-inner" placeholder="Alexander" />
                </div>
                <div className="space-y-2">
                  <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Last Name</label>
                  <input required value={formData.lastName} onChange={e => setFormData({...formData, lastName: e.target.value})} type="text" className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all text-on-surface shadow-inner" placeholder="Vance" />
                </div>
              </div>
              
              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Email Address</label>
                <input required value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} type="email" className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary focus:bg-surface-container transition-all text-on-surface shadow-inner" placeholder="a.vance@vanguard-corp.com" />
              </div>

              <div className="space-y-2">
                <label className="text-[10px] uppercase font-bold tracking-widest text-on-surface-variant">Client Tier</label>
                <select value={formData.clientType} onChange={e => setFormData({...formData, clientType: e.target.value})} className="w-full bg-surface-container-low border-none rounded-sm px-4 py-3 text-sm focus:ring-1 focus:ring-primary text-on-surface shadow-inner appearance-none">
                  <option value="Comun">Standard</option>
                  <option value="Frecuente">Frequent / VIP</option>
                </select>
              </div>

              <div className="pt-6 flex justify-end gap-3 mt-8">
                <button type="button" onClick={() => setIsModalOpen(false)} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest text-on-surface-variant hover:text-on-surface rounded-sm transition-colors border border-outline-variant/20 hover:bg-surface-container-low">
                  Cancelar
                </button>
                <button type="submit" disabled={saving} className="px-6 py-2.5 text-xs font-bold uppercase tracking-widest bg-gradient-to-br from-primary-container to-primary text-on-primary-fixed rounded-sm shadow-xl shadow-primary/20 hover:brightness-110 active:scale-95 transition-all disabled:opacity-50 flex items-center gap-2">
                  {saving && <span className="material-symbols-outlined animate-spin text-sm">sync</span>}
                  Registrar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
