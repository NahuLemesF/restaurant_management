import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Clients from './pages/Clients';
import MenusAndDishes from './pages/MenusAndDishes';
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
          <Route path="clients" element={<Clients />} />
          <Route path="menus" element={<MenusAndDishes />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
