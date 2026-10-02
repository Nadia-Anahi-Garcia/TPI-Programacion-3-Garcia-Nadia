import type { Rol } from "./Rol";


export interface IProductoPedido {
  id: number;
  nombre: string;
  precio: number;
  descripcion: string;
  stock: number;
  imagen: string;
  disponible: boolean;
}
export interface IDetallePedido {
  idProducto?: number;
  producto?: IProductoPedido;
  cantidad: number;
  subtotal: number;
}

export interface IUsuarioPedido {
  id: number;
  nombre: string;
  apellido: string;
  mail: string;
  celular: string;
  rol: Rol;
}


export interface IPedido {
  id: number;
  fecha: string;
  estado: string;
  total: number;
  formaPago: string;
  idUsuario?: number;
  usuarioDto?: IUsuarioPedido;
  detalles: IDetallePedido[];
}