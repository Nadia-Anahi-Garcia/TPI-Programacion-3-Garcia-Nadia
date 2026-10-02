import "../../../main";
import { logout } from "../../../utils/auth";
import {getUSer} from "../../../utils/localStorage";
import type { IUsuario } from "../../../types/usuario";
// Importa el array con las categorias
import { obtenerCategorias, obtenerProductos } from "../../../utils/fetch";
import type { IProduct } from "../../../types/product";
// impora para indicar el tipo de una categoria. 
import type { ICategoria } from "../../../types/categoria";
import{ agregarProductoAlCarrito, obtenerCarrito } from "../../../utils/cart";


// Obtiene los elementos necesarios del DOM
const contenedorCategorias = document.getElementById("contenedorCategorias")!;
const contenedorProductos = document.getElementById("contenedorProductos" )!;
const buscador = document.getElementById("buscador" )as HTMLInputElement;
const ordenProductos = document.getElementById("ordenProductos") as HTMLSelectElement;
const botonMostrarTodos = document.getElementById("mostrarTodos") as HTMLButtonElement;
const mensajeProductos= document.getElementById ("mensajeProductos") as HTMLParagraphElement;
const contadorCarrito = document.getElementById("contadorCarrito") as HTMLSpanElement;
const buttonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const nombreUsuario = document.getElementById("nombreUsuario") as HTMLSpanElement;
const enlacePanelAdmin = document.getElementById("enlacePanelAdmin") as HTMLAnchorElement;
const enlaceMisPedidos = document.getElementById("enlaceMisPedidos") as HTMLAnchorElement;


buttonLogout?.addEventListener("click", () => {
  logout();
});

const usuarioGuardado = getUSer();

if (usuarioGuardado) {
  const usuario: IUsuario = JSON.parse(usuarioGuardado);
  nombreUsuario.textContent = `${usuario.nombre} ${usuario.apellido}`;
  if (usuario.rol === "ADMIN") {
    enlacePanelAdmin.hidden = false;
    enlaceMisPedidos.hidden = true;
  }
}



// Guarda la categoría seleccionada para aplicar el filtro
// Creás categoriaActiva con let porque cambia al hacer clic. 
// Puede guardar una ICategoria o null si todavía no hay ninguna seleccionada.
let categoria_Activa : ICategoria | null= null;
// Creamos array vacios de categoria y producto. 
let categorias: ICategoria[] = [];
let productos : IProduct[] = [];

const actualizarContadorCarrito = (): void => {
  const cantidadTotal = obtenerCarrito().reduce(
    (acumulador, item) => acumulador + item.cantidad,
    0
  );

  contadorCarrito.textContent = `(${cantidadTotal})`;
};


// Mostrar las categorías
const dibujarCategorias = () : void =>{
  // Limpiar el contenedor 
    contenedorCategorias.innerHTML = "";

    if (categoria_Activa === null) {
      botonMostrarTodos.className = "btn-activo";
    } else {
      botonMostrarTodos.className = "categoria-btn";
    }
    
    // Obtiene las categorías y las recorre con forEach.
    categorias.forEach((categoria) => {
        const button = document.createElement('button');
        button.textContent = categoria.nombre;

        //compara esa categoría con la activa:
        //si es la misma, le pone la clase activa;
        // si no, le pone la clase normal.
        button.className = 
          categoria === categoria_Activa 
          ? "btn-activo " 
          : "categoria-btn";

          // Guarda la categoria clickeada como activa y vuele a ejcutar la función 
          // para que el boton seleccionado se va activo. 
        button.addEventListener("click", () =>{
          categoria_Activa = categoria;
          dibujarCategorias();
          dibujarProductos();
        })

        // Agrega el boton ya configurado dentro del contenedor
        contenedorCategorias.appendChild(button);
    });

};

// Muestra las tarjetas de los productos 
const dibujarProductos = () : void => {
  contenedorProductos.innerHTML = "";

  const textoBuscado = buscador.value.trim();

  const productosFiltrados = productos.filter ((producto)=> {
    
    const estaActivo = producto.disponible === true && producto.eliminado === false;
    const coincide_Categoria = categoria_Activa === null || producto.categoriaId=== categoria_Activa.id;
   
    const coincideNombre = producto.nombre
    .toLocaleLowerCase()
    .includes(buscador.value.toLocaleLowerCase());

    return estaActivo && coincide_Categoria && coincideNombre;
  });

  /* Si se ingresa un texto se valida que si no está vacio y no hay coincidencias
   le avise. Caso contratio si las las hay arroja cuantas. */

  if (ordenProductos.value === "nombre-asc") {
      productosFiltrados.sort((productoA, productoB) =>
          productoA.nombre.localeCompare(productoB.nombre)
      );
  }

  if (ordenProductos.value === "precio-asc") {
      productosFiltrados.sort(
        (productoA, productoB) => productoA.precio - productoB.precio
      );
    }

  if (ordenProductos.value === "precio-desc") {
      productosFiltrados.sort(
        (productoA, productoB) => productoB.precio - productoA.precio
    );
  }

  if (textoBuscado !== "") { 
    if (productosFiltrados.length === 0){
        mensajeProductos.textContent = `No hay coincidencias para  "${textoBuscado}".`;
    } else{
        mensajeProductos.textContent = `Búsqueda: "${textoBuscado}" - ${productosFiltrados.length} resultado(s)`; 
    }
  } else{
    mensajeProductos.textContent = "";
  };

  productosFiltrados.forEach((producto)=> {
    const card = document.createElement("div");
    card.className = "card-producto";
    card.innerHTML = ` 
        <img src="${producto.imagen}" alt="${producto.nombre}">
        <h3>${producto.nombre}</h3>
        <p>${producto.descripcion}</p>
        <p>$${producto.precio} </p>
        <a href="../productDetail/productDetail.html" class="btn-detalle">
          Ver detalle
        </a>
        <button type="button" class="btn-agregar">Agregar</button>
        `;
    const enlaceDetalle = card.querySelector(".btn-detalle") as HTMLAnchorElement;
    enlaceDetalle.addEventListener("click", () => {
        localStorage.setItem("productoSeleccionado", JSON.stringify(producto.id)
      );
    });
    const botonAgregar = card.querySelector(".btn-agregar") as HTMLButtonElement;
    botonAgregar.addEventListener("click", () => {
      const seAgrego = agregarProductoAlCarrito(producto);

      if (!seAgrego) {
        alert(
          `No podés agregar más unidades de ${producto.nombre} porque supera el stock disponible.`
        );
      return;
      }
      actualizarContadorCarrito();
      alert(`${producto.nombre} se agregó exitosamente al carrito.`);
    });
    
    contenedorProductos.appendChild(card);  
  });
};

buscador.addEventListener("input",()=>{
  dibujarProductos();
});

ordenProductos.addEventListener("change", () => {
  dibujarProductos();
});

botonMostrarTodos.addEventListener("click", () =>{
  categoria_Activa = null;
  dibujarCategorias();
  dibujarProductos();
})


async function iniciarPagina(): Promise<void> {
  try {
    categorias = await obtenerCategorias();
    productos = await obtenerProductos();

    dibujarCategorias();
    dibujarProductos();
  } catch (error) {
    console.error(error);
    mensajeProductos.textContent = "No se pudieron cargar los datos.";
  }
}
actualizarContadorCarrito();
iniciarPagina();
