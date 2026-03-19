import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import { useEffect } from 'react';

function App() {
  // Inicializamos dark mode por defecto
  useEffect(() => {
    document.documentElement.classList.add('dark');
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="pos" element={<div className="p-8">Punto de Venta (Próximamente)</div>} />
          <Route path="clients" element={<div className="p-8">Clientes (Próximamente)</div>} />
          <Route path="menus" element={<div className="p-8">Menús y Platos (Próximamente)</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
