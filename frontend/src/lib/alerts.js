import Swal from 'sweetalert2'
import withReactContent from 'sweetalert2-react-content'

const MySwal = withReactContent(Swal)

// Toast configuration for non-blocking notifications
const Toast = MySwal.mixin({
  toast: true,
  position: 'bottom-end',
  showConfirmButton: false,
  timer: 1500,
  timerProgressBar: true,
  customClass: {
    popup: 'bg-zinc-900 text-zinc-50 border border-zinc-800 rounded-xl shadow-lg',
  },
  didOpen: (toast) => {
    toast.onmouseenter = Swal.stopTimer;
    toast.onmouseleave = Swal.resumeTimer;
  }
});

/**
 * Muestra un Toast de éxito en la esquina inferior derecha
 */
export const showSuccessToast = (title) => {
  return Toast.fire({
    icon: 'success',
    title
  });
};

/**
 * Muestra un Toast de error en la esquina inferior derecha
 */
export const showErrorToast = (title) => {
  return Toast.fire({
    icon: 'error',
    title
  });
};

/**
 * Muestra un Modal bloqueante de Error
 */
export const showErrorModal = (title, text = '') => {
  return MySwal.fire({
    title,
    text,
    icon: 'error',
    background: '#18181b', // bg-zinc-900 (Tailwind)
    color: '#fafafa',
    confirmButtonColor: '#f59e0b', // amber-500
    customClass: {
      popup: 'border border-zinc-800 rounded-xl',
      confirmButton: 'px-6 py-2 rounded-lg font-medium'
    }
  });
};

/**
 * Muestra un diálogo de confirmación (ideal para borrar)
 * Retorna true si el usuario confirmó, false si canceló.
 */
export const showConfirmDialog = async (title, text = 'Esta acción no se puede deshacer.') => {
  const result = await MySwal.fire({
    title,
    text,
    icon: 'warning',
    showCancelButton: true,
    background: '#18181b',
    color: '#fafafa',
    confirmButtonColor: '#ef4444', 
    cancelButtonColor: '#27272a',
    confirmButtonText: 'Sí, confirmar',
    cancelButtonText: 'Cancelar',
    customClass: {
      popup: 'border border-zinc-800 rounded-xl',
      confirmButton: 'px-4 py-2 rounded-lg font-medium',
      cancelButton: 'px-4 py-2 rounded-lg font-medium'
    }
  });
  return result.isConfirmed;
};
