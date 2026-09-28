import "../../../main";
import { logout } from "../../../utils/auth";
// Importa el array con las categorias
import { obtenerCategorias, obtenerProductos } from "../../../utils/fetch";
import type { IProduct } from "../../../types/product";
// impora para indicar el tipo de una categoria. 
import type { ICategoria } from "../../../types/categoria";
import{ agregarProductoAlCarrito } from "../../../utils/cart";


const buttonLogout = document.getElementById(
  "logoutButton"
) as HTMLButtonElement;
buttonLogout?.addEventListener("click", () => {
  logout();
});

// Obtiene los elementos necesarios del DOM
const contenedorCategorias = document.getElementById("contenedorCategorias")!;
const contenedorProductos = document.getElementById("contenedorProductos" )!;
const buscador = document.getElementById("buscador" )as HTMLInputElement;
const botonMostrarTodos = document.getElementById("mostrarTodos") as HTMLButtonElement;
const mensajeProductos= document.getElementById ("mensajeProductos") as HTMLParagraphElement;


// Guarda la categoría seleccionada para aplicar el filtro
// Creás categoriaActiva con let porque cambia al hacer clic. 
// Puede guardar una ICategoria o null si todavía no hay ninguna seleccionada.
let categoria_Activa : ICategoria | null= null;
// Creamos array vacios de categoria y producto. 
let categorias: ICategoria[] = [];
let productos : IProduct[] = [];

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
    const coincide_Categoria = 
    categoria_Activa === null || 
      producto.categoriaId=== categoria_Activa.id;
   
    const coincideNombre = producto.nombre
    .toLocaleLowerCase()
    .includes(buscador.value.toLocaleLowerCase());

    return coincide_Categoria && coincideNombre;
  });

  /* Si se ingresa un texto se valida que si no está vacio y no hay coincidencias
   le avise. Caso contratio si las las hay arroja cuantas. */

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
        <button type="button" class="btn-agregar">Agregar</button>
        `;
    const botonAgregar = card.querySelector(".btn-agregar") as HTMLButtonElement;
    botonAgregar.addEventListener("click", () =>{
      agregarProductoAlCarrito(producto);
      alert(`${producto.nombre} se agregó exitosamente al carrito.`);
    });
    contenedorProductos.appendChild(card);  
  });
};

buscador.addEventListener("input",()=>{
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

iniciarPagina();
