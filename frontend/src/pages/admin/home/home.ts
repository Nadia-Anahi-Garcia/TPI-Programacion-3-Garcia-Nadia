import "../../../main";
import { logout } from "../../../utils/auth";
import { obtenerCategorias,obtenerPedidos,obtenerProductos,obtenerUsuarios,} from "../../../utils/fetch";

// OBTENEMOS LOS ELEMENTOS DEL DOM QUE VAMOS A UTILIZAR
const buttonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const totalCategorias = document.getElementById("totalCategorias") as HTMLParagraphElement;
const totalProductos = document.getElementById("totalProductos") as HTMLParagraphElement;
const totalPedidos = document.getElementById("totalPedidos") as HTMLParagraphElement;
const productosDisponibles = document.getElementById("productosDisponibles") as HTMLParagraphElement;
const productosActivos = document.getElementById("productosActivos") as HTMLSpanElement;
const productosInactivos = document.getElementById("productosInactivos") as HTMLSpanElement;
const pedidosPorEstado = document.getElementById("pedidosPorEstado") as HTMLUListElement;
const mensajeDashboard = document.getElementById("mensajeDashboard") as HTMLParagraphElement;

// EVENTO DE CIERRE DE SESIÓN
buttonLogout?.addEventListener("click", () => {
  logout();
});

// FUNCIÓN PARA CARGAR LOS DATOS DEL DASHBOARD, 
// que va a cargar los datos del panel y que trabaja de manera asíncrona
const cargarDashboard = async (): Promise<void> => {
  try {
    const [categorias, productos, pedidos] = await Promise.all([
      obtenerCategorias(),
      obtenerProductos(),
      obtenerPedidos(),
      obtenerUsuarios(),
    ]);
    
    // FILTRA Y CREA UN A LISTA NUEVA DE CATEGORIAS QUE NO ESTÉN ELIMINADAS
    // NO MODIFICA EL ARRAY ORIGINAL. 
    const categoriasActivas = categorias.filter((categoria) => {
      return !categoria.eliminado;
    });

    const productosActivosLista = productos.filter((producto) => {
      return !producto.eliminado;
    });

    const productosDisponiblesLista = productos.filter((producto) => {
      return !producto.eliminado && producto.disponible;
    });

    const productosNoDisponibles = productos.filter((producto) => {
      return !producto.eliminado && !producto.disponible;
    });

    totalCategorias.textContent = categoriasActivas.length.toString();
    totalProductos.textContent = productosActivosLista.length.toString();
    totalPedidos.textContent = pedidos.length.toString();
    productosDisponibles.textContent =
      productosDisponiblesLista.length.toString();

    productosActivos.textContent =
      productosDisponiblesLista.length.toString();

    productosInactivos.textContent =
      productosNoDisponibles.length.toString();

    const cantidadPorEstado = pedidos.reduce(
      (acumulador: Record<string, number>, pedido) => {
        const estado = pedido.estado;

        acumulador[estado] = (acumulador[estado] || 0) + 1;

        return acumulador;
      },
      {}
    );

    pedidosPorEstado.innerHTML = Object.entries(cantidadPorEstado)
      .map(([estado, cantidad]) => {
        return `<li>${estado}: ${cantidad}</li>`;
      })
      .join("");

    mensajeDashboard.textContent = "";
  } catch (error) {
    console.error(error);
    mensajeDashboard.textContent =
      "No se pudieron cargar los datos del Dashboard.";
  }
};

cargarDashboard();




