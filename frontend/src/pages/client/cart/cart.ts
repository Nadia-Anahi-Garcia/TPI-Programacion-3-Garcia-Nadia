

// Carga los estilos y aplica la protección de rutas
import "../../../main";
import { logout } from "../../../utils/auth";

// carga las funciones que aplican al carrito
import { obtenerCarrito, calcularTotalCarrito, actualizarCantidad, eliminarProductoCarrito, vaciarCarrito } from "../../../utils/cart";

// Obtenemos los elementos del DOM.
const contenedorCarrito = document.getElementById("contenedorCarrito")!;
const mensajeCarrito = document.getElementById("mensajeCarrito")!;
const subtotalCarrito = document.getElementById("subtotalCarrito")!;
const totalCarrito = document.getElementById("totalCarrito")!;
const botonVaciarCarrito = document.getElementById("vaciarCarrito") as HTMLButtonElement;


// Evento para cerrar sesión
const buttonLogout = document.getElementById(
  "logoutButton"
) as HTMLButtonElement;

buttonLogout?.addEventListener("click", () => {
  logout();
});

// Función para mostrar carrito, obtener items guardados

const dibujarCarrito = () :void => {
    const carrito = obtenerCarrito();

    // vaciamos el contenedor para no duplicar
    contenedorCarrito.innerHTML = "";

    // si está vacio mostramos 
    if (carrito.length === 0){
        mensajeCarrito.textContent = "Tu carrito está vacio.";
        subtotalCarrito.textContent = "$0";
        totalCarrito.textContent = "$0";
        return;
    };
    
    // vacio el mensaje
    mensajeCarrito.textContent = "";

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
            actualizarCantidad(item.producto.id, item.cantidad +1);
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

    const total = calcularTotalCarrito();

    subtotalCarrito.textContent = `$${total}`;
    totalCarrito.textContent = `$${total}`;

};

botonVaciarCarrito.addEventListener("click", () =>{
    vaciarCarrito();
    dibujarCarrito();
});

dibujarCarrito();



   

