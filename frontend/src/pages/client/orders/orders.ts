import "../../../main";
import { logout } from "../../../utils/auth";
import { getUSer } from "../../../utils/localStorage";
import type { IUsuario } from "../../../types/usuario";
import type { IPedido } from "../../../types/pedido";
import { obtenerPedidos, obtenerProductos } from "../../../utils/fetch";
import type { IProduct } from "../../../types/product";
import { obtenerPedidosLocales } from "../../../utils/pedidos";


// OBTENEMOS LOS ELEMENTOS DEL DOM
const contenedorPedidos = document.getElementById("contenedorPedidos") as HTMLElement;
const mensajePedidos = document.getElementById("mensajePedidos") as HTMLParagraphElement;
const nombreUsuario = document.getElementById("nombreUsuario") as HTMLSpanElement;
const botonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const modalPedido = document.getElementById("modalPedido") as HTMLElement;
const detallePedido = document.getElementById("detallePedido") as HTMLElement;
const botonCerrarModal = document.getElementById("cerrarModalPedido") as HTMLButtonElement;


botonLogout.addEventListener("click", () =>{
    logout();
});

botonCerrarModal.addEventListener("click", () => {
  modalPedido.hidden = true;
});

const usuarioGuardado = getUSer();

if(!usuarioGuardado){
    mensajePedidos.textContent = "No se encontró una sesión activa."
}else{
    const usuario : IUsuario = JSON.parse(usuarioGuardado);
    nombreUsuario.textContent = `${usuario.nombre} ${usuario.apellido}`;
    const mostrarDetallePedido = (pedido : IPedido, productos : IProduct[]): void =>{
        const detallesHtml = pedido.detalles
            .map((detalle)=>{
                const producto = productos.find((producto)=>{
                    return producto.id === detalle.idProducto;
                });

                const nombreProducto = producto
                    ?producto.nombre
                    :"Producto no encontrado";
                
                return `
                    <li>
                        ${nombreProducto} —
                        Cantidad: ${detalle.cantidad} —
                        Subtotal: $${detalle.subtotal}
                    </li>
                `;
            })
            .join("");

            detallePedido.innerHTML = `
                <h3>Pedido #${pedido.id}</h3>
                <p>Fecha: ${pedido.fecha}</p>
                <p>Estado: ${pedido.estado}</p>
                <p>Forma de pago: ${pedido.formaPago}</p>
                <h4>Productos</h4>
                <ul>
                    ${detallesHtml}
                </ul>
                <p><strong>Total: $${pedido.total}</strong></p>
            `;
            modalPedido.hidden = false;
            };
    
    const dibujarPedidos = (pedidos : IPedido[], productos: IProduct[]): void =>{
        contenedorPedidos.innerHTML = "";

        if(pedidos.length === 0){
           mensajePedidos.textContent = "Todavía no tenés pedidos realizados.";
            return;  
        }
        mensajePedidos.textContent = "";

        pedidos.forEach((pedido)=>{
            const card = document.createElement("article");
            card.className = "card-pedido";

            card.innerHTML = `
                <h3>Pedido #${pedido.id}</h3>
                <p>Fecha: ${pedido.fecha}</p>
                <p class="estado-pedido">Estado: ${pedido.estado}</p>
                <p>Total: $${pedido.total}</p>

                <button type="button" class="btn-ver-detalle">
                    Ver detalle
                </button>
            `;
            const botonVerDetalle = card.querySelector(".btn-ver-detalle") as HTMLButtonElement;

            botonVerDetalle.addEventListener("click", () => {
                 mostrarDetallePedido(pedido, productos);
            });

            contenedorPedidos.appendChild(card);

        });
    };
    const cargarPedidos = async(): Promise<void> =>{
        try{
            const pedidosBase = await obtenerPedidos();
            const productos = await obtenerProductos();
            const pedidosLocales = obtenerPedidosLocales();

            const pedidosUsuario = [
                ...pedidosBase,
                ...pedidosLocales,
            ]
            .filter((pedido)=>pedido.idUsuario === usuario.id)
            .sort((pedidoA, pedidoB) => {
                return(
                    new Date(pedidoB.fecha).getTime() - 
                    new Date(pedidoA.fecha).getTime()
                );
            });

            dibujarPedidos(pedidosUsuario, productos);

        } catch(error){
            console.error(error);
            mensajePedidos.textContent="No se pudieron cargar los pedidos.";
        }
    };
    cargarPedidos();
}