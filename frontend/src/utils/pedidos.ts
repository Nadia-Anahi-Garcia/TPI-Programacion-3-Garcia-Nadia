import type { IPedido, IDetallePedido, IUsuarioPedido } from "../types/pedido";
import type{ICartItem } from "../types/product";
import { obtenerPedidos } from "./fetch";

const PEDIDOS_LOCALES_KEY = "pedidosLocales";

export const obtenerPedidosLocales = (): IPedido[] => {
    const pedidosGuardaos = localStorage.getItem(PEDIDOS_LOCALES_KEY);

    if (pedidosGuardaos){
        return JSON.parse(pedidosGuardaos);
    }

    return[];

};

export const guardarPedidosLocales = (pedidos : IPedido[]): void =>{
    localStorage.setItem(PEDIDOS_LOCALES_KEY, JSON.stringify(pedidos));

}

export const crearPedido = async (
    carrito: ICartItem[],
    usuario: IUsuarioPedido,
    formaDePago: string,
    total: number,
    telefono: string
): Promise<IPedido> => {

    const pedidosBase = await obtenerPedidos();
    const pedidosLocales = obtenerPedidosLocales();

    const idsUsados = new Set(
        [...pedidosBase, ...pedidosLocales].map(pedido => pedido.id)
    );

    let nuevoId = 1;

    while (idsUsados.has(nuevoId)) {
        nuevoId++;
    }

    const detalles : IDetallePedido[] = carrito.map((item)=> {
        return{
            idProducto: item.producto.id,
            cantidad: item.cantidad,
            subtotal: item.producto.precio * item.cantidad,
        };
    });

    const nuevoPedido: IPedido = {
        id: nuevoId,
        fecha: new Date().toISOString(),
        estado: "PENDIENTE",
        total: total,
        formaPago: formaDePago,
        idUsuario: usuario.id,
        usuarioDto: {
            id: usuario.id,
            nombre: usuario.nombre,
            apellido: usuario.apellido,
            mail: usuario.mail,
            celular: usuario.celular,
            rol: usuario.rol,
        },
        detalles: detalles,
        telefono: telefono,
    };

    pedidosLocales.push(nuevoPedido);
    guardarPedidosLocales(pedidosLocales);

    return nuevoPedido;
}; 
