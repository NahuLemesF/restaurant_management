import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

export default function Layout() {
  return (
    <div className="bg-surface text-on-surface font-body overflow-hidden h-screen flex">
      <Sidebar />
      
      {/* Topbar flotante estilo Stitch */}
      <header className="fixed top-0 right-0 left-64 flex justify-between items-center px-8 h-20 z-40 bg-[rgba(19,19,19,0.6)] backdrop-blur-xl font-headline font-semibold shadow-[0_20px_40px_rgba(14,14,14,0.4)]">
        <div className="flex items-center gap-6">
          <span className="text-lg font-black text-on-surface">Nocturnal Concierge</span>
        </div>
        <div className="flex items-center gap-6">
          <div className="flex flex-col items-end">
            <span className="text-xs uppercase tracking-widest text-on-surface-variant font-bold">Admin Station</span>
            <span className="text-[10px] text-primary">Connected</span>
          </div>
        </div>
      </header>

      <main className="ml-64 mt-20 flex w-full h-[calc(100vh-5rem)] overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
}
