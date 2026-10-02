import "../../../main";
import { logout } from "../../../utils/auth";
import { getUSer } from "../../../utils/localStorage";
import type { IUsuario } from "../../../types/usuario";
import { obtenerProductos } from "../../../utils/fetch";
import type { IProduct } from "../../../types/product";
import { agregarProductoAlCarrito, obtenerCarrito } from "../../../utils/cart";
import { navigate } from "../../../utils/navigate";

// Obtenemos los elementos del DOM

const detalleProducto = document.getElementById("detalleProducto") as HTMLElement;
const buttonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const nombreUsuario = document.getElementById("nombreUsuario") as HTMLSpanElement;
const enlaceMisPedidos = document.getElementById("enlaceMisPedidos") as HTMLAnchorElement;
const enlaceCarrito = document.getElementById("enlaceCarrito") as HTMLAnchorElement;


let esAdmin = false;


// Recuperar el id que se guarda en home.ts
const productoSeleccionado = localStorage.getItem("productoSeleccionado");

const dibujarDetalleProducto = (producto:IProduct): void =>{

  const itemEnCarrito = obtenerCarrito().find((item) => {
    return item.producto.id === producto.id;
  });

  const cantidadEnCarrito = itemEnCarrito
    ? itemEnCarrito.cantidad
    : 0;

  const stockRestante = producto.stock - cantidadEnCarrito;

  let estado : string;
  if(producto.disponible && producto.stock > 0){
    estado = "Disponible";
  }else{
    estado = "No disponible";
  }
  detalleProducto.innerHTML = `
    <section class="detalle-producto">
      <img src="${producto.imagen}" alt="${producto.nombre}">
      <h2>${producto.nombre}</h2>
      <p>${producto.descripcion}</p>
      <p>Precio: $${producto.precio}</p>
      <p>Stock disponible: ${producto.stock}</p>
      <p>Estado: ${estado}</p>
      <label id="labelCantidad" for="cantidadProducto">Cantidad:</label>
      <div id="controlesCantidad" class="controles-cantidad">   
        <button type="button" id="btnRestarCantidad">−</button>
        <span id="cantidadProducto">1</span>
        <button type="button" id="btnSumarCantidad">+</button>
      </div>
      <button type="button" id="btnAgregarCarrito">
        Agregar al carrito
      </button>

      <button type="button" id="btnVolverCatalogo">
        Volver al catálogo
      </button>  
    </section>
  `;

  const botonVolver = document.getElementById( "btnVolverCatalogo")as HTMLButtonElement;

  botonVolver.addEventListener("click", () =>{
    navigate ("/src/pages/store/home/home.html");
  });

  let cantidadSeleccionada: number = 1;

  const botonRestar = document.getElementById("btnRestarCantidad") as HTMLButtonElement;

  const botonSumar = document.getElementById("btnSumarCantidad") as HTMLButtonElement;

    const textoCantidad = document.getElementById("cantidadProducto") as HTMLSpanElement;

  botonSumar.addEventListener("click", () => {
  if (cantidadSeleccionada < stockRestante) {
    cantidadSeleccionada++;
    textoCantidad.textContent = `${cantidadSeleccionada}`;
  }
  });

  botonRestar.addEventListener("click", () => {
  if (cantidadSeleccionada > 1) {
    cantidadSeleccionada--;
    textoCantidad.textContent = `${cantidadSeleccionada}`;
  }
  });

  const botonAgregar = document.getElementById("btnAgregarCarrito") as HTMLButtonElement;
  const controlesCantidad = document.getElementById("controlesCantidad") as HTMLDivElement;
  const labelCantidad = document.getElementById("labelCantidad") as HTMLLabelElement;

  if (esAdmin) {
    botonAgregar.hidden = true;
    controlesCantidad.hidden = true;
    labelCantidad.hidden = true;
  }

  if (!producto.disponible || stockRestante === 0) {
    botonAgregar.disabled = true;
  }

  botonAgregar.addEventListener("click", () => {
    const seAgrego = agregarProductoAlCarrito(producto, cantidadSeleccionada);

    if (!seAgrego) {
      alert(
        `No podés agregar esa cantidad de ${producto.nombre} porque supera el stock disponible.`
      );
      return;
    }

    alert(
      `${cantidadSeleccionada} unidad(es) de ${producto.nombre} se agregaron al carrito.`
    );

    dibujarDetalleProducto(producto);
  });

};



if(productoSeleccionado === null){
  detalleProducto.textContent = "No se seleccionó ningún producto.";
} else{
   const idProducto: number = JSON.parse(productoSeleccionado)
   async function cargarDetalle(): Promise<void> {
    const productos = await obtenerProductos();
    const productoEncontrado = productos.find((producto) => producto.id === idProducto);
    
    if(productoEncontrado === undefined){
      detalleProducto.textContent = "No se encontró el producto.";
      return
    }
    dibujarDetalleProducto(productoEncontrado);
  }
   cargarDetalle();
}




// evento CERRAR SESION
buttonLogout.addEventListener("click", () => {
    // cierra sesión
  logout();
});

const usuarioGuardado = getUSer();

if (usuarioGuardado) {
  const usuario: IUsuario = JSON.parse(usuarioGuardado);
  nombreUsuario.textContent = `${usuario.nombre} ${usuario.apellido}`;

  if (usuario.rol === "ADMIN") {
    esAdmin = true;
    enlaceMisPedidos.hidden = true;
    enlaceCarrito.hidden = true;
  }
}