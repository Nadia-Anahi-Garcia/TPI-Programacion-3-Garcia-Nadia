import type { IPedido, IDetallePedido } from "../types/pedido";
import type{ICartItem } from "../types/product";

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

export const crearPedido = (
    carrito : ICartItem[], 
    idUsuario : number,
    formaDePago : string,
    total: number
) : IPedido => {

    const detalles : IDetallePedido[] = carrito.map((item)=> {
        return{
            idProducto: item.producto.id,
            cantidad: item.cantidad,
            subtotal: item.producto.precio * item.cantidad,
        };
    });

    const nuevoPedido: IPedido = {
        id: Date.now(),
        fecha: new Date().toISOString().slice(0,15),
        estado: "PENDIENTE",
        total: total,
        formaPago: formaDePago,
        idUsuario: idUsuario,
        detalles: detalles,
    };
    const pedidosLocales = obtenerPedidosLocales();
    pedidosLocales.push(nuevoPedido);
    guardarPedidosLocales(pedidosLocales);

    return nuevoPedido;
}; 
