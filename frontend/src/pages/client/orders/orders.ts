import "../../../main";
import { logout } from "../../../utils/auth";
import { getUSer } from "../../../utils/localStorage";
import type { IUsuario } from "../../../types/usuario";
import type { IPedido } from "../../../types/pedido";
import { obtenerPedidos, obtenerProductos } from "../../../utils/fetch";
import type { IProduct } from "../../../types/product";
import { obtenerPedidosLocales } from "../../../utils/pedidos";
import { obtenerCarrito } from "../../../utils/cart";

// OBTENEMOS LOS ELEMENTOS DEL DOM
const contenedorPedidos = document.getElementById("contenedorPedidos") as HTMLElement;
const mensajePedidos = document.getElementById("mensajePedidos") as HTMLParagraphElement;
const nombreUsuario = document.getElementById("nombreUsuario") as HTMLSpanElement;
const botonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const modalPedido = document.getElementById("modalPedido") as HTMLElement;
const detallePedido = document.getElementById("detallePedido") as HTMLElement;
const botonCerrarModal = document.getElementById("cerrarModalPedido") as HTMLButtonElement;
const contadorCarrito = document.getElementById("contadorCarrito") as HTMLSpanElement;


const cantidadTotalCarrito = obtenerCarrito().reduce(
  (acumulador, item) => acumulador + item.cantidad,
  0
);

contadorCarrito.textContent = `(${cantidadTotalCarrito})`;

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
        const detallesHtml = pedido.detalles.map(detalle => {
            const producto = detalle.producto || productos.find(
                producto => producto.id === detalle.idProducto
            );

            const precioUnitario = detalle.cantidad > 0
                ? detalle.subtotal / detalle.cantidad
                : 0;

            return `
                <li class="pedido-producto">
                    <div class="pedido-producto-info">
                        <strong>${producto?.nombre || "Producto no encontrado"}</strong>
                        <span>
                            Cantidad: ${detalle.cantidad} × $${precioUnitario.toFixed(2)}
                        </span>
                    </div>

                    <strong class="pedido-producto-subtotal">
                        $${detalle.subtotal.toFixed(2)}
                    </strong>
                </li>
            `;
        }).join("");

            const subtotal = pedido.detalles.reduce(
                (acumulador, detalle) => acumulador + detalle.subtotal,
                0
            );

            const envio = pedido.total - subtotal;


        detallePedido.innerHTML = `
                <h3>Detalle del pedido #${pedido.id}</h3>

                <div class="pedido-datos-cliente">
                    <p><strong>Cliente:</strong> ${usuario.nombre} ${usuario.apellido}</p>
                    <p><strong>Fecha:</strong> ${new Date(pedido.fecha).toLocaleString("es-AR")}</p>
                    <p><strong>Teléfono:</strong> ${pedido.telefono || usuario.celular || "Sin informar"}</p>
                    <p><strong>Método de pago:</strong> ${pedido.formaPago}</p>
                    <span class="estado-pedido estado-${pedido.estado}">
                        ${pedido.estado}
                    </span>
                </div>

                <section class="pedido-seccion-productos">
                    <h4>Productos</h4>
                    <ul class="pedido-lista-productos">
                        ${detallesHtml}
                    </ul>
                </section>

                <div class="pedido-resumen-totales">
                    <p>
                        <span>Subtotal:</span>
                        <span>$${subtotal.toFixed(2)}</span>
                    </p>
                    <p>
                        <span>Envío:</span>
                        <span>$${envio.toFixed(2)}</span>
                    </p>
                    <p class="pedido-total">
                        <strong>Total:</strong>
                        <strong>$${pedido.total.toFixed(2)}</strong>
                    </p>
                </div>
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
            const resumenProductos = pedido.detalles
                .slice(0, 3)
                .map((detalle) => {
                    const producto = productos.find(
                        (producto) => producto.id === detalle.idProducto
                    );

                    return `${detalle.cantidad} × ${
                        producto?.nombre ?? "Producto no encontrado"
                    }`;
                })
                .join(", ");

            card.innerHTML = `
                <h3>Pedido #${pedido.id}</h3>
                <p>Fecha: ${new Date(pedido.fecha).toLocaleString("es-AR")}</p>
                <p class="estado-pedido estado-${pedido.estado}">
                        Estado: ${pedido.estado}
                </p>
                <p>Productos: ${resumenProductos}</p>
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