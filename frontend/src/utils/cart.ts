
import type{ICartItem, IProduct } from "../types/product";


const CART_KEY = "cart";

// Función que obtiene los ítems guardados del carrito 
export const obtenerCarrito = ():  ICartItem[] => {
    const carritoGuardado = localStorage.getItem(CART_KEY);

    if (carritoGuardado){
        return JSON.parse(carritoGuardado);

    } else {
       return [];
    }

}

// Función que guarde los ítems del carrito
export const guardarCarrito = (itemsCarrito :ICartItem[]) : void => {
    localStorage.setItem(CART_KEY, JSON.stringify(itemsCarrito));
}


// Función para agregar un producto al carrito

export const agregarProductoAlCarrito = ( producto : IProduct) : void =>{
    const carrito = obtenerCarrito();
    const productoEncontrado = carrito.find((item) => {
        return item.producto.id === producto.id
    });

    if (productoEncontrado){
        productoEncontrado.cantidad ++;
    }else{
        carrito.push({
            producto : producto,
            cantidad: 1
        });
    }
    guardarCarrito(carrito);
}

// Función para calcular el total
export const calcularTotalCarrito = () : number =>{
    const carrito = obtenerCarrito();
    const totalIncial : number = 0;
    const total = carrito.reduce((acumulador, item) => {
        return acumulador + item.producto.precio * item.cantidad;
    }, totalIncial)
    return total;
}

// Función para actualizar la cantidad. 

export const actualizarCantidad = (idProducto : number, nuevaCantidad: number) : void => {
    
    if(nuevaCantidad <= 0){
        eliminarProductoCarrito(idProducto);
        return;
    }
    const carrito = obtenerCarrito();
    const itemEncontrado = carrito.find((item) => {
        return item.producto.id === idProducto
    });
    if(itemEncontrado){
        itemEncontrado.cantidad = nuevaCantidad;
        guardarCarrito(carrito);
    }
}
    

// Función para eliminar un producto del carrito

export const eliminarProductoCarrito = (idProducto : number): void =>{
    const carrito = obtenerCarrito();

    const carritoActualizado  = carrito.filter ((item)=> {
      
         return item.producto.id !== idProducto;   
      });

    guardarCarrito(carritoActualizado);
      
};

// Función para vaciar carrito

export const vaciarCarrito = (): void =>{
    guardarCarrito([]);
};