import type { ICategoria } from "../types/categoria";
import type { IProduct } from "../types/product";
import type { IUsuario } from "../types/usuario";
import type { IPedido } from "../types/pedido";

// OBTENER CATEGORIAS

export async function obtenerCategorias (): Promise<ICategoria[]>{
    const respuesta = await fetch("/data/categorias.json");
    if(!respuesta.ok){
        throw new Error("No se pudieron cargar las categorías.");
    }

    const categorias : ICategoria[] = await respuesta.json();

    return categorias;
}

// OBTENER PRODUCTOS

export async function obtenerProductos(): Promise<IProduct[]> {
  const respuesta = await fetch("/data/productos.json");

  if (!respuesta.ok) {
    throw new Error("No se pudieron cargar los productos.");
  }

  const productos: IProduct[] = await respuesta.json();

  return productos;
}

// OBTENER USUARIOS

export async function obtenerUsuarios() : Promise<IUsuario[]> {
  const respuesta = await fetch("/data/usuarios.json");

  if (!respuesta.ok){
    throw new Error ("No se pudieron cargar los usuarios")
  }
  
  const usuarios: IUsuario[] = await respuesta.json();
  return usuarios;
}

// OBTENER PEDIDO

export async function obtenerPedidos() : Promise<IPedido[]> {
  const respuesta = await fetch ("/data/pedidos.json");

  if(!respuesta.ok){
    throw new Error ("No se pudieron cargar los pedidos.");
  }

  const pedidos: IPedido[]= await respuesta.json();

  return pedidos;
}
