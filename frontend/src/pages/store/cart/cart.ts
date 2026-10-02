

// cARGA LOS ESTILOS Y LA PROTECCIÓN DE RUTAS
import "../../../main";
import { logout } from "../../../utils/auth";
import type { IUsuario } from "../../../types/usuario";
import {getUSer} from "../../../utils/localStorage";
import { crearPedido } from "../../../utils/pedidos";
import { navigate } from "../../../utils/navigate";

// CARGA LAS FUNCIONES QUE APLICAN AL CARRITO
import { obtenerCarrito, calcularTotalCarrito, actualizarCantidad, eliminarProductoCarrito, vaciarCarrito } from "../../../utils/cart";

// CONSTANTE CON EL COSTO DEL ENVIO;
const COSTO_ENVIO= 0;

// OBTENEMOS LOS ELEMENTOS DEL DOM
const formCheckout = document.getElementById("formCheckout") as HTMLFormElement;
const contenedorCarrito = document.getElementById("contenedorCarrito")!;
const mensajeCarrito = document.getElementById("mensajeCarrito")!;
const volverAComprar = document.getElementById("volverAComprar") as HTMLAnchorElement;
const subtotalCarrito = document.getElementById("subtotalCarrito")!;
const envioCarrito = document.getElementById("envioCarrito")!;
const totalCarrito = document.getElementById("totalCarrito")!;
const botonVaciarCarrito = document.getElementById("vaciarCarrito") as HTMLButtonElement;
const nombreUsuario = document.getElementById("nombreUsuario") as HTMLSpanElement;
const botonFinalizarCompra = document.getElementById("finalizarCompra") as HTMLButtonElement;
const aviso = document.getElementById("aviso")!;
const seccionCheckout = document.getElementById("seccionCheckout") as HTMLElement;
const botonCancelarCheckout = document.getElementById("cancelarCheckout") as HTMLButtonElement;



// EVENTO PARA CERRAR SESIÓN
const buttonLogout = document.getElementById(
  "logoutButton"
) as HTMLButtonElement;

buttonLogout?.addEventListener("click", () => {
  logout();
});

// mOSTRAR EL NOMBRE DEL USUARIO
const usuarioGuardado = getUSer();

if (usuarioGuardado) {
  const usuario: IUsuario = JSON.parse(usuarioGuardado);
  nombreUsuario.textContent = `${usuario.nombre} ${usuario.apellido}`;
}

// FUNCIÓN PARA MOSTRAR EL CARRITO Y OBTENER ITEMS GUARDADOS

const dibujarCarrito = () :void => {
    const carrito = obtenerCarrito();

    // vaciamos el contenedor para no duplicar
    contenedorCarrito.innerHTML = "";

    // si está vacio mostramos 
    if (carrito.length === 0){
        mensajeCarrito.textContent = "Tu carrito está vacio.";
        subtotalCarrito.textContent = "$0";
        envioCarrito.textContent = `$${COSTO_ENVIO}`;
        totalCarrito.textContent = "$0";
        botonFinalizarCompra.disabled = true;
        aviso.textContent = "Tu carrito está vacío.";
        volverAComprar.hidden = false;
        return;
    };
    
    // vacio el mensaje 
    mensajeCarrito.textContent = "";
    volverAComprar.hidden = true;
    botonFinalizarCompra.disabled = false;
    aviso.textContent = "";

    // si tiene ítems → recorrer, crear tarjetas y calcular total
    carrito.forEach((item) => {
        const card = document.createElement('article');
        card.className = "item-carrito";

        const subtotalItem = item.producto.precio * item.cantidad;
        card.innerHTML = ` 
            <img src="${item.producto.imagen}" alt="${item.producto.nombre}">
            <div class="informacion-item">
                <h3>${item.producto.nombre}</h3>
                <p>Precio: $${item.producto.precio}</p>
                 <p class= "subtotal-item">Subtotal: $${subtotalItem}</p>
            </div>
            <div class="controles-cantidad">
                <button type="button" class="btn-restar">−</button>
                <span>${item.cantidad}</span>
                <button type="button" class="btn-sumar">+</button>
            </div>
            <button type="button" class="btn-eliminar">Eliminar</button>
        `;
        const botonRestar = card.querySelector(".btn-restar") as HTMLButtonElement;
        const botonSumar = card.querySelector(".btn-sumar") as HTMLButtonElement;
        const botonEliminar = card.querySelector(".btn-eliminar") as HTMLButtonElement;

        botonSumar.addEventListener("click", () => {
            const seActualizo = actualizarCantidad(
                item.producto.id,
                item.cantidad + 1
            );

            if (!seActualizo) {
                alert(
                    `No podés agregar más unidades de ${item.producto.nombre}. Stock disponible: ${item.producto.stock}.`
            );
            return;
            }

            dibujarCarrito();
        });

        botonRestar.addEventListener("click", () => {
                if (item.cantidad > 1){
                    actualizarCantidad(item.producto.id, item.cantidad -1);
                    dibujarCarrito();
                }
        });

        botonEliminar.addEventListener("click", () => {
            eliminarProductoCarrito(item.producto.id);
            dibujarCarrito();
        });

        contenedorCarrito.appendChild(card);

    });

    const subtotal = calcularTotalCarrito();
    const totalFinal = subtotal + COSTO_ENVIO;

    subtotalCarrito.textContent = `$${subtotal}`;   
    envioCarrito.textContent = `$${COSTO_ENVIO}`;
    totalCarrito.textContent = `$${totalFinal}`;

};

botonVaciarCarrito.addEventListener("click", () =>{
    vaciarCarrito();
    dibujarCarrito();
});

dibujarCarrito();

botonFinalizarCompra.addEventListener("click", () => {
  seccionCheckout.hidden = false;

  seccionCheckout.scrollIntoView({
    behavior: "smooth",
  });
});

botonCancelarCheckout.addEventListener("click", () => {
  seccionCheckout.hidden = true;
});

formCheckout.addEventListener("submit", (event) => {
  event.preventDefault();

  const carrito = obtenerCarrito();

  if (carrito.length === 0) {
    alert("No podés confirmar un pedido con el carrito vacío.");
    return;
  }

  if (!usuarioGuardado) {
    alert("No se encontró un usuario con sesión iniciada.");
    return;
  }

  const usuario: IUsuario = JSON.parse(usuarioGuardado);

  const formData = new FormData(formCheckout);
  const formaPago = formData.get("formaPago");

  if (typeof formaPago !== "string" || formaPago === "") {
    alert("Seleccioná una forma de pago.");
    return;
  }

  const subtotal = calcularTotalCarrito();
  const totalFinal = subtotal + COSTO_ENVIO;

  const pedido = crearPedido(
    carrito,
    usuario.id,
    formaPago,
    totalFinal
  );

  vaciarCarrito();
  formCheckout.reset();
  seccionCheckout.hidden = true;
  dibujarCarrito();

  alert(`Pedido #${pedido.id} confirmado correctamente.`);
  navigate("/src/pages/client/orders/orders.html");
});


   

