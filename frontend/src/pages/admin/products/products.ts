import "../../../main";
import { logout } from "../../../utils/auth";
import {obtenerCategorias, obtenerProductos} from "../../../utils/fetch";
import type { ICategoria } from "../../../types/categoria";
import type { IProduct } from "../../../types/product";


// OBTENEMOS LOS ELEMENTOS DEL DOM

const botonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const botonNuevoProducto = document.getElementById("botonNuevoProducto") as HTMLButtonElement;
const cuerpoTablaProductos = document.getElementById("cuerpoTablaProductos") as HTMLTableSectionElement;
const mensajeProductos = document.getElementById("mensajeProductos") as HTMLParagraphElement;
const contenedorFormularioProducto = document.getElementById("contenedorFormularioProducto") as HTMLElement;
const tituloFormularioProducto = document.getElementById("tituloFormularioProducto") as HTMLHeadingElement;
const formularioProducto = document.getElementById("formularioProducto") as HTMLFormElement;
const idProducto = document.getElementById("idProducto") as HTMLInputElement;
const nombreProducto = document.getElementById("nombreProducto") as HTMLInputElement;
const descripcionProducto = document.getElementById("descripcionProducto") as HTMLTextAreaElement;
const precioProducto = document.getElementById("precioProducto") as HTMLInputElement;
const stockProducto = document.getElementById("stockProducto") as HTMLInputElement;
const categoriaProducto = document.getElementById("categoriaProducto") as HTMLSelectElement;
const imagenProducto = document.getElementById("imagenProducto") as HTMLInputElement;
const disponibleProducto = document.getElementById("disponibleProducto") as HTMLInputElement;
const botonCancelarProducto = document.getElementById("botonCancelarProducto") as HTMLButtonElement;


// ESTADO DE LA PÁGINA
let productos: IProduct[] = [];
let categorias: ICategoria[] = [];

// EVENTO CERRAR SESIÓN
botonLogout.addEventListener("click", () => {
  logout();
});

// FUNCIONES DEL FORMULARIO

const cerrarFormularioProducto = (): void => {
  formularioProducto.reset();
  contenedorFormularioProducto.hidden = true;
};

const cargarOpcionesCategorias = (): void => {
  categoriaProducto.innerHTML =
    '<option value="">Seleccioná una categoría</option>';

  const categoriasActivas = categorias.filter((categoria) => {
    return !categoria.eliminado;
  });

  categoriasActivas.forEach((categoria) => {
    const opcion = document.createElement("option");

    opcion.value = categoria.id.toString();
    opcion.textContent = categoria.nombre;

    categoriaProducto.appendChild(opcion);
  });
};

const abrirFormularioNuevoProducto = (): void => {
  formularioProducto.reset();

  idProducto.value = "";
  disponibleProducto.checked = true;

  tituloFormularioProducto.textContent = "Nuevo producto";

  contenedorFormularioProducto.hidden = false;
};

const abrirFormularioEditarProducto = (
  producto: IProduct
): void => {
  idProducto.value = producto.id.toString();
  nombreProducto.value = producto.nombre;
  descripcionProducto.value = producto.descripcion;
  precioProducto.value = producto.precio.toString();
  stockProducto.value = producto.stock.toString();
  categoriaProducto.value = producto.categoriaId.toString();
  imagenProducto.value = producto.imagen;
  disponibleProducto.checked = producto.disponible;

  tituloFormularioProducto.textContent = "Editar producto";

  contenedorFormularioProducto.hidden = false;
};
// FUNCIÓN PARA ELIMINAR UN PRODUCTO

const eliminarProducto = (id: number): void => {
  const producto = productos.find((producto) => {
    return producto.id === id;
  });

  if (!producto) {
    return;
  }

  const confirmarEliminacion = confirm(
    `¿Querés eliminar el producto "${producto.nombre}"?`
  );

  if (!confirmarEliminacion) {
    return;
  }

  producto.eliminado = true;

  mostrarProductos();

  mensajeProductos.textContent =
    "Producto eliminado correctamente.";
};

// FUNCIÓN PARA MOSTRAR LOS PRODUCTOS EN LA TABLA

const mostrarProductos = (): void => {
  cuerpoTablaProductos.innerHTML = "";

  const productosActivos = productos.filter((producto) => {
    return !producto.eliminado;
  });

  if (productosActivos.length === 0) {
    mensajeProductos.textContent = "No hay productos para mostrar.";
    return;
  }

  mensajeProductos.textContent = "";

  productosActivos.forEach((producto) => {
    const categoria = categorias.find((categoria) => {
      return categoria.id === producto.categoriaId;
    });

    const nombreCategoria = categoria
      ? categoria.nombre
      : "Categoría no encontrada";

    const estado = producto.disponible
      ? "Disponible"
      : "No disponible";

    const fila = document.createElement("tr");

    fila.innerHTML = `
      <td>${producto.id}</td>
      <td>
        <img
          src="${producto.imagen}"
          alt="${producto.nombre}"
          width="60"
        />
      </td>
      <td>${producto.nombre}</td>
      <td>${producto.descripcion}</td>
      <td>$${producto.precio}</td>
      <td>${nombreCategoria}</td>
      <td>${producto.stock}</td>
      <td>${estado}</td>
      <td>
        <button
          type="button"
          class="botonEditarProducto"
          aria-label="Editar producto"
        >
          <span class="material-symbols-outlined">edit</span>
          Editar
        </button>

        <button
          type="button"
          class="botonEliminarProducto"
          aria-label="Eliminar producto"
        >
          <span class="material-symbols-outlined">delete</span>
          Eliminar
        </button>
      </td>
    `;

    const botonEditarProducto = fila.querySelector(
      ".botonEditarProducto"
    ) as HTMLButtonElement;

    const botonEliminarProducto = fila.querySelector(
      ".botonEliminarProducto"
    ) as HTMLButtonElement;

    botonEditarProducto.addEventListener("click", () => {
      abrirFormularioEditarProducto(producto);
    });

    botonEliminarProducto.addEventListener("click", () => {
      eliminarProducto(producto.id);
    });

    cuerpoTablaProductos.appendChild(fila);
  });
};

// EVENTOS DEL FORMULARIO

botonNuevoProducto.addEventListener("click", () => {
  abrirFormularioNuevoProducto();
});

botonCancelarProducto.addEventListener("click", () => {
  cerrarFormularioProducto();
});

formularioProducto.addEventListener("submit", (event) => {
  event.preventDefault();

  const nombre = nombreProducto.value.trim();
  const descripcion = descripcionProducto.value.trim();
  const precio = Number(precioProducto.value);
  const stock = Number(stockProducto.value);
  const categoriaId = Number(categoriaProducto.value);
  const imagen = imagenProducto.value.trim();
  const disponible = disponibleProducto.checked;

  if (!nombre || !descripcion || !imagen || !categoriaId) {
    mensajeProductos.textContent =
      "Completá todos los campos obligatorios.";
    return;
  }

  if (precio <= 0) {
    mensajeProductos.textContent =
      "El precio debe ser mayor a 0.";
    return;
  }

  if (stock < 0 || !Number.isInteger(stock)) {
    mensajeProductos.textContent =
      "El stock debe ser un número entero mayor o igual a 0.";
    return;
  }

  const categoriaExiste = categorias.find((categoria) => {
    return categoria.id === categoriaId && !categoria.eliminado;
  });

  if (!categoriaExiste) {
    mensajeProductos.textContent =
      "Seleccioná una categoría válida.";
    return;
  }

  const id = Number(idProducto.value);

  const productoExistente = productos.find((producto) => {
    return producto.id === id;
  });

  if (productoExistente) {
    productoExistente.nombre = nombre;
    productoExistente.descripcion = descripcion;
    productoExistente.precio = precio;
    productoExistente.stock = stock;
    productoExistente.categoriaId = categoriaId;
    productoExistente.imagen = imagen;
    productoExistente.disponible = disponible;

    mostrarProductos();
    cerrarFormularioProducto();

    mensajeProductos.textContent =
      "Producto editado correctamente.";
  } else {
    const nuevoId =
      productos.length > 0
        ? Math.max(...productos.map((producto) => producto.id)) + 1
        : 1;

    const nuevoProducto: IProduct = {
      id: nuevoId,
      nombre: nombre,
      descripcion: descripcion,
      precio: precio,
      stock: stock,
      categoriaId: categoriaId,
      imagen: imagen,
      disponible: disponible,
      eliminado: false,
    };

    productos.push(nuevoProducto);

    mostrarProductos();
    cerrarFormularioProducto();

    mensajeProductos.textContent =
      "Producto creado correctamente.";
  }
});

// FUNCIÓN PARA OBTENER PRODUCTOS Y CATEGORÍAS

const cargarProductos = async (): Promise<void> => {
  try {
    const [productosObtenidos, categoriasObtenidas] =
      await Promise.all([
        obtenerProductos(),
        obtenerCategorias(),
      ]);

    productos = productosObtenidos;
    categorias = categoriasObtenidas;

    cargarOpcionesCategorias();
    mostrarProductos();
  } catch (error) {
    console.error(error);
    mensajeProductos.textContent =
      "No se pudieron cargar los productos.";
  }
};

cargarProductos();