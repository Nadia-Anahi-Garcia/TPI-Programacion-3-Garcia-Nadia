import "../../../main";
import { logout } from "../../../utils/auth";
import { obtenerCategorias } from "../../../utils/fetch";
import type { ICategoria } from "../../../types/categoria";

// OBTENEMOS LOS ELEMENTOS DEL DOM
const botonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const cuerpoTablaCategorias = document.getElementById("cuerpoTablaCategorias") as HTMLTableSectionElement;
const mensajeCategorias = document.getElementById("mensajeCategorias") as HTMLParagraphElement;
const botonNuevaCategoria = document.getElementById("botonNuevaCategoria") as HTMLButtonElement;
const contenedorFormularioCategoria = document.getElementById("contenedorFormularioCategoria") as HTMLElement;
const tituloFormularioCategoria = document.getElementById("tituloFormularioCategoria") as HTMLHeadingElement;
const formularioCategoria = document.getElementById("formularioCategoria") as HTMLFormElement;
const botonCancelarCategoria = document.getElementById("botonCancelarCategoria") as HTMLButtonElement;
const nombreCategoria = document.getElementById(  "nombreCategoria") as HTMLInputElement;
const descripcionCategoria = document.getElementById("descripcionCategoria") as HTMLTextAreaElement;
const imagenCategoria = document.getElementById("imagenCategoria") as HTMLInputElement;
const idCategoria = document.getElementById("idCategoria") as HTMLInputElement;


// ESTADO DE LAS CATEGORÍAS EN ESTA PÁGINA
let categorias: ICategoria[] = [];

// EVENTO CERRAR SESIÓN

botonLogout.addEventListener("click", () => {
  logout();
});


// FUNCIONES DEL FORMULARIO

const cerrarFormularioCategoria = (): void => {
  formularioCategoria.reset();
  contenedorFormularioCategoria.hidden = true;
};

const abrirFormularioNuevaCategoria = (): void => {
  formularioCategoria.reset();

  tituloFormularioCategoria.textContent = "Nueva categoría";

  contenedorFormularioCategoria.hidden = false;
};

const abrirFormularioEditarCategoria = (
  categoria: ICategoria
): void => {
  idCategoria.value = categoria.id.toString();
  nombreCategoria.value = categoria.nombre;
  descripcionCategoria.value = categoria.descripcion;
  imagenCategoria.value = categoria.imagen;

  tituloFormularioCategoria.textContent = "Editar categoría";

  contenedorFormularioCategoria.hidden = false;
};


// FUNCIÓN PARA ELIMINAR UNA CATEGORÍA

const eliminarCategoria = (id: number): void => {
  const categoria = categorias.find((categoria) => {
    return categoria.id === id;
  });

  if (!categoria) {
    return;
  }

  const confirmarEliminacion = confirm(
    `¿Querés eliminar la categoría "${categoria.nombre}"?`
  );

  if (!confirmarEliminacion) {
    return;
  }

  categoria.eliminado = true;

  mostrarCategorias();

  mensajeCategorias.textContent =
    "Categoría eliminada correctamente.";
};

// FUNCIÓN PARA MOSTRAR LAS CATEGORÍAS EN LA TABLA

const mostrarCategorias = (): void => {
  cuerpoTablaCategorias.innerHTML = "";

  const categoriasActivas = categorias.filter((categoria) => {
    return !categoria.eliminado;
  });

  if (categoriasActivas.length === 0) {
    mensajeCategorias.textContent = "No hay categorías para mostrar.";
    return;
  }

  mensajeCategorias.textContent = "";

  categoriasActivas.forEach((categoria) => {
    const fila = document.createElement("tr");

    fila.innerHTML = `
      <td>${categoria.id}</td>
      <td>
        <img
          src="${categoria.imagen}"
          alt="${categoria.nombre}"
          width="60"
        />
      </td>
      <td>${categoria.nombre}</td>
      <td>${categoria.descripcion}</td>
      <td>
        <button
          type="button"
          class="botonEditarCategoria"
          aria-label="Editar categoría"
        >
          <span class="material-symbols-outlined">edit</span>
          Editar
        </button>

        <button
          type="button"
          class="botonEliminarCategoria"
          aria-label="Eliminar categoría"
        >
          <span class="material-symbols-outlined">delete</span>
          Eliminar
        </button>
      </td>
    `;

    const botonEditarCategoria = fila.querySelector(
      ".botonEditarCategoria"
    ) as HTMLButtonElement;

    const botonEliminarCategoria = fila.querySelector(
      ".botonEliminarCategoria"
    ) as HTMLButtonElement;

    botonEditarCategoria.addEventListener("click", () => {
      abrirFormularioEditarCategoria(categoria);
    });

    botonEliminarCategoria.addEventListener("click", () => {
      eliminarCategoria(categoria.id);
    });

    cuerpoTablaCategorias.appendChild(fila);
  });
};

// EVENTOS DEL FORMULARIO

botonNuevaCategoria.addEventListener("click", () => {
  abrirFormularioNuevaCategoria();
});

botonCancelarCategoria.addEventListener("click", () => {
  cerrarFormularioCategoria();
});

formularioCategoria.addEventListener("submit", (event) => {
  event.preventDefault();

  const nombre = nombreCategoria.value.trim();
  const descripcion = descripcionCategoria.value.trim();
  const imagen = imagenCategoria.value.trim();

  if (!nombre || !descripcion || !imagen) {
    mensajeCategorias.textContent =
      "Completá todos los campos de la categoría.";
    return;
  }

  const id = Number(idCategoria.value);

  const categoriaExistente = categorias.find((categoria) => {
    return categoria.id === id;
  });

  if (categoriaExistente) {
    categoriaExistente.nombre = nombre;
    categoriaExistente.descripcion = descripcion;
    categoriaExistente.imagen = imagen;

    mensajeCategorias.textContent =
      "Categoría editada correctamente.";
  } else {
    const nuevoId =
      categorias.length > 0
        ? Math.max(...categorias.map((categoria) => categoria.id)) + 1
        : 1;

    const nuevaCategoria: ICategoria = {
      id: nuevoId,
      nombre: nombre,
      descripcion: descripcion,
      imagen: imagen,
      eliminado: false,
    };

    categorias.push(nuevaCategoria);

    mensajeCategorias.textContent =
      "Categoría creada correctamente.";
  }

  mostrarCategorias();

  cerrarFormularioCategoria();
});


// FUNCIÓN PARA OBTENER LAS CATEGORÍAS DEL JSON

const cargarCategorias = async (): Promise<void> => {
  try {
    categorias = await obtenerCategorias();

    mostrarCategorias();
  } catch (error) {
    console.error(error);
    mensajeCategorias.textContent =
      "No se pudieron cargar las categorías.";
  }
};

cargarCategorias();