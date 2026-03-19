import { forwardRef } from 'react';

// Tamaño estándar de ticket térmico 80mm (approx 300px ancho)
const ReceiptTemplate = forwardRef(({ cart, subtotal, discount, total, clientName, orderDate }, ref) => {
  return (
    <div 
      ref={ref} 
      style={{ width: '300px' }} 
      className="p-4 bg-white text-black font-mono text-sm"
    >
        <div className="flex flex-col items-center mb-4">
          <h2 className="text-xl font-bold uppercase tracking-widest text-center">The Nocturnal</h2>
          <p className="text-[10px] uppercase font-bold tracking-widest text-black/60">Dinner Service</p>
          <div className="w-full border-t border-dashed border-black my-3"></div>
          <p className="text-xs">Ticket: #{new Date().getFullYear().toString()}-{Math.floor(Math.random() * 1000).toString().padStart(4, '0')}</p>
          <p className="text-xs">Fecha: {orderDate ? new Date(orderDate).toLocaleString() : new Date().toLocaleString()}</p>
          {clientName && <p className="text-xs mt-1">Cliente: {clientName}</p>}
        </div>

        <div className="w-full border-t border-dashed border-black mb-3"></div>

        <div className="flex justify-between text-xs font-bold mb-2 uppercase">
          <span className="w-8">Cant</span>
          <span className="flex-1">Desc</span>
          <span className="text-right">Total</span>
        </div>

        <div className="space-y-2 text-xs">
          {cart.map((item, i) => (
            <div key={i} className="flex justify-between items-start">
              <span className="w-8 font-bold">{item.quantity}</span>
              <span className="flex-1 pr-2 uppercase leading-tight">{item.dish.name}</span>
              <span className="text-right">${(item.dish.price * item.quantity).toLocaleString()}</span>
            </div>
          ))}
        </div>

        <div className="w-full border-t border-dashed border-black my-4"></div>

        {discount > 0 && (
          <div className="space-y-1 mb-2">
            <div className="flex justify-between text-xs">
              <span>Subtotal</span>
              <span>${subtotal.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
            </div>
            <div className="flex justify-between text-xs font-bold">
              <span>Descuento VIP (2.38%)</span>
              <span>-${discount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
            </div>
          </div>
        )}

        <div className="flex justify-between text-sm font-bold uppercase">
          <span>Total a Pagar</span>
          <span>${total.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
        </div>

        <div className="w-full border-t border-dashed border-black my-4"></div>

        <div className="text-center text-xs mt-4 space-y-1">
          <p className="font-bold">¡GRACIAS POR SU VISITA!</p>
          <p className="uppercase">Vuelva pronto</p>
        </div>
    </div>
  );
});

ReceiptTemplate.displayName = 'ReceiptTemplate';
export default ReceiptTemplate;
