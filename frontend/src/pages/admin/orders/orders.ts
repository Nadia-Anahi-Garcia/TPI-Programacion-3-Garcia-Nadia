import "../../../main";
import { logout } from "../../../utils/auth";
import {obtenerPedidos,obtenerProductos,obtenerUsuarios,} from "../../../utils/fetch";
import type { IPedido } from "../../../types/pedido";
import type { IProduct } from "../../../types/product";
import type { IUsuario } from "../../../types/usuario";
import { obtenerPedidosLocales } from "../../../utils/pedidos";

// OBTENEMOS LOS ELEMENTOS DEL DOM

const botonLogout = document.getElementById("logoutButton") as HTMLButtonElement;
const filtroCliente = document.getElementById("filtroCliente") as HTMLSelectElement;
const filtroEstado = document.getElementById(
    "filtroEstado"
) as HTMLSelectElement;
const mensajePedidosAdmin = document.getElementById("mensajePedidosAdmin") as HTMLParagraphElement;
const cuerpoTablaPedidos = document.getElementById("cuerpoTablaPedidos") as HTMLTableSectionElement;
const modalDetallePedido = document.getElementById("modalDetallePedido") as HTMLElement;
const detallePedidoAdmin = document.getElementById("detallePedidoAdmin") as HTMLElement;
const botonCerrarDetallePedido = document.getElementById("botonCerrarDetallePedido") as HTMLButtonElement;

// ESTADO DE LA PÁGINA

let pedidos: IPedido[] = [];
let usuarios: IUsuario[] = [];
let productos: IProduct[] = [];

// EVENTO CERRAR SESIÓN

botonLogout.addEventListener("click", () => {
  logout();
});

// FUNCIONES AUXILIARES

const obtenerIdClientePedido = (pedido: IPedido): number => {
  if (pedido.usuarioDto) {
    return pedido.usuarioDto.id;
  }

  return pedido.idUsuario || 0;
};

const obtenerNombreCliente = (pedido: IPedido): string => {
  if (pedido.usuarioDto) {
    return `${pedido.usuarioDto.nombre} ${pedido.usuarioDto.apellido}`;
  }

  const usuario = usuarios.find((usuario) => {
    return usuario.id === pedido.idUsuario;
  });

  if (!usuario) {
    return "Cliente no encontrado";
  }

  return `${usuario.nombre} ${usuario.apellido}`;
};

const obtenerEstadosPedidos = (): string[] => {
  return [
    "PENDIENTE",
    "CONFIRMADO",
    "TERMINADO",
    "CANCELADO",
  ];
};

const cargarFiltroClientes = (): void => {
  filtroCliente.innerHTML =
    '<option value="">Todos los clientes</option>';

  const clientes = usuarios.filter((usuario) => {
    return usuario.rol === "USUARIO";
  });

  clientes.forEach((cliente) => {
    const opcion = document.createElement("option");

    opcion.value = cliente.id.toString();
    opcion.textContent = `${cliente.nombre} ${cliente.apellido}`;

    filtroCliente.appendChild(opcion);
  });
};

// FUNCIÓN PARA MOSTRAR EL DETALLE

const mostrarDetallePedido = (pedido: IPedido): void => {
  const cliente = usuarios.find(
      usuario => usuario.id === obtenerIdClientePedido(pedido)
  );

  const subtotal = pedido.detalles.reduce(
      (acumulador, detalle) => acumulador + detalle.subtotal,
      0
  );

  const envio = pedido.total - subtotal;

  const detallesHtml = pedido.detalles.map(detalle => {
    const producto = detalle.producto || productos.find(
        producto => producto.id === detalle.idProducto
    );

    return `
      <li>
        ${producto?.nombre || "Producto no encontrado"} —
        Cantidad: ${detalle.cantidad} —
        Subtotal: $${detalle.subtotal}
      </li>
    `;
  }).join("");

  const opcionesEstado = obtenerEstadosPedidos().map(estado => `
    <option
      value="${estado}"
      ${estado === pedido.estado ? "selected" : ""}
    >
      ${estado}
    </option>
  `).join("");

  detallePedidoAdmin.innerHTML = `
    <h3>Pedido #${pedido.id}</h3>
    <p>Cliente: ${obtenerNombreCliente(pedido)}</p>
    <p>Fecha: ${new Date(pedido.fecha).toLocaleString("es-AR")}</p>
    <p class="estado-pedido estado-${pedido.estado}">
      Estado: ${pedido.estado}
    </p>
    <p>Forma de pago: ${pedido.formaPago}</p>
    <p>Teléfono: ${pedido.telefono || cliente?.celular || "Sin informar"}</p>

    <h4>Productos</h4>
    <ul>${detallesHtml}</ul>

    <p>Subtotal: $${subtotal}</p>
    <p>Envío: $${envio}</p>
    <p><strong>Total: $${pedido.total}</strong></p>

    <label for="estadoDetallePedido">Cambiar estado</label>
    <select id="estadoDetallePedido" class="selectEstadoPedido">
      ${opcionesEstado}
    </select>

    <button type="button" id="guardarEstadoPedido">
      Guardar estado
    </button>
  `;

  const selectorEstado = document.getElementById(
      "estadoDetallePedido"
  ) as HTMLSelectElement;

  const botonGuardarEstado = document.getElementById(
      "guardarEstadoPedido"
  ) as HTMLButtonElement;

  botonGuardarEstado.addEventListener("click", () => {
    pedido.estado = selectorEstado.value;
    mostrarPedidos();
    modalDetallePedido.hidden = true;

    mensajePedidosAdmin.textContent =
        "Estado del pedido actualizado correctamente.";
  });

  modalDetallePedido.hidden = false;
};

// FUNCIÓN PARA MOSTRAR LOS PEDIDOS

const mostrarPedidos = (): void => {
  cuerpoTablaPedidos.innerHTML = "";

  const idClienteSeleccionado = Number(filtroCliente.value);

  const pedidosFiltrados = pedidos
      .filter((pedido) => {
        const coincideCliente =
            !idClienteSeleccionado ||
            obtenerIdClientePedido(pedido) === idClienteSeleccionado;

        const coincideEstado =
            filtroEstado.value === "" ||
            pedido.estado === filtroEstado.value;

        return coincideCliente && coincideEstado;
      })
    .sort((pedidoA, pedidoB) => {
      return (
        new Date(pedidoB.fecha).getTime() -
        new Date(pedidoA.fecha).getTime()
      );
    });

  if (pedidosFiltrados.length === 0) {
    mensajePedidosAdmin.textContent =
      "No hay pedidos para mostrar.";
    return;
  }

  mensajePedidosAdmin.textContent = "";

  const estados = obtenerEstadosPedidos();

  pedidosFiltrados.forEach((pedido) => {
    const cantidadProductos = pedido.detalles.reduce(
      (acumulador, detalle) => {
        return acumulador + detalle.cantidad;
      },
      0
    );

    const opcionesEstado = estados
      .map((estado) => {
        const seleccionado =
          estado === pedido.estado ? "selected" : "";

        return `
          <option value="${estado}" ${seleccionado}>
            ${estado}
          </option>
        `;
      })
      .join("");

    const fila = document.createElement("tr");

    fila.innerHTML = `
      <td>${pedido.id}</td>
      <td>${obtenerNombreCliente(pedido)}</td>
      <td>${new Date(pedido.fecha).toLocaleString("es-AR")}</td>
      <td>
        <select class="selectEstadoPedido">
          ${opcionesEstado}
        </select>
      </td>
      <td>${cantidadProductos}</td>
      <td>$${pedido.total}</td>
      <td>
        <button
          type="button"
          class="botonVerDetallePedido"
        >
          <span class="material-symbols-outlined">visibility</span>
          Ver detalle
        </button>
      </td>
    `;

    const selectEstadoPedido = fila.querySelector(
      ".selectEstadoPedido"
    ) as HTMLSelectElement;

    const botonVerDetallePedido = fila.querySelector(
      ".botonVerDetallePedido"
    ) as HTMLButtonElement;

    selectEstadoPedido.addEventListener("change", () => {
      pedido.estado = selectEstadoPedido.value;
      mostrarPedidos();
      mensajePedidosAdmin.textContent =
        "Estado del pedido actualizado correctamente.";
    });

    botonVerDetallePedido.addEventListener("click", () => {
      mostrarDetallePedido(pedido);
    });

    cuerpoTablaPedidos.appendChild(fila);
  });
};

// EVENTOS

filtroCliente.addEventListener("change", () => {
  mostrarPedidos();
});
filtroEstado.addEventListener("change", () => {
  mostrarPedidos();
});

botonCerrarDetallePedido.addEventListener("click", () => {
  modalDetallePedido.hidden = true;
});

// FUNCIÓN PARA OBTENER LOS DATOS

const cargarPedidos = async (): Promise<void> => {
  try {
    const [
      pedidosObtenidos,
      usuariosObtenidos,
      productosObtenidos,
    ] = await Promise.all([
      obtenerPedidos(),
      obtenerUsuarios(),
      obtenerProductos(),
    ]);

    pedidos = [
      ...pedidosObtenidos,
      ...obtenerPedidosLocales(),
    ];
    const usuariosGuardados = localStorage.getItem("users");

    const usuariosLocales: IUsuario[] = usuariosGuardados
        ? JSON.parse(usuariosGuardados)
        : [];

    usuarios = [
      ...usuariosObtenidos,
      ...usuariosLocales,
    ];
    productos = productosObtenidos;

    cargarFiltroClientes();
    mostrarPedidos();
  } catch (error) {
    console.error(error);
    mensajePedidosAdmin.textContent =
      "No se pudieron cargar los pedidos.";
  }
};

cargarPedidos();