


export interface IProduct{
    id: number;
    eliminado: boolean;
    nombre: string;
    precio: number;
    descripcion: string;
    stock: number;
    imagen: string;
    disponible: boolean;
    categoriaId: number;
}




export interface ICartItem{
    producto: IProduct;
    cantidad: number;
}