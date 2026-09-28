
export interface IDetallePedido{

    idProducto: number;
    cantidad: number;
    subtotal: number;
}

export interface IPedido {
    id: number;
    fecha: string;
    estado: string;
    total: number;
    formaPago: string;
    idUsuario: number;
    detalles: IDetallePedido[];
}