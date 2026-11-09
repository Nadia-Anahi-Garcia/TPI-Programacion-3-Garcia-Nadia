# Food Store - Trabajo Práctico Integrador - Programación III


## Enlace al video. 


## Descripción

Food Store es una aplicación frontend desarrollada con HTML5,
CSS3 y TypeScript utilizando Vite.

Permite consultar el catálogo, buscar y filtrar productos,
gestionar un carrito persistente, finalizar compras y consultar
los pedidos realizados.

Incluye registro e inicio de sesión, protección de rutas según
el rol y un panel administrador para gestionar categorías,
productos y pedidos.

## Funcionalidades Implementadas
- **Catálogo Dinámico**:  Catálogo de productos cargado dinámicamente.
- **Filtros - Búsquedas**: 
    - Filtro de productos por categoría.
    - Búsqueda de productos por nombre.
    - Mensaje con la cantidad de resultados encontrados.
    - Aviso cuando no existen coincidencias.
- **Carrito de Compras**: 
    - Agregar productos al carrito.
    - Persistencia del carrito mediante `localStorage` con la clave `"cart"`.
    - Actualización de cantidades, eliminación de productos y vaciado del carrito.
    - Cálculo automático del total de la compra.
- **Protección de rutas y Login**:
    - Registro, inicio y cierre de sesión.
- **Finalizar compra**:
    - Confirmación de los datos del comprador y forma de pago.
    - Generación del pedido y actualización del stock.
- **Mis pedidos**:
    - Listado de pedidos del cliente.
    - Consulta del detalle de cada pedido.
- **Panel administrador**:
    - Dashboard.
    - Gestión de categorías y productos.
    - Consulta de pedidos con filtros por cliente y estado.
    - Actualización del estado de los pedidos.

## Tecnologías utilizadas

- HTML5
- CSS3
- TypeScript
- Vite
- pnpm
- localStorage

## Instalación y ejecución

1. Clonar o descargar el repositorio.

2. Abrir una terminal en la carpeta del proyecto.

3. Instalar las dependencias:

    ```bash
    pnpm install
    ```
4. Ejecutar el proyecto: 
     ```bash
    pnpm  dev
    ```
5. Abrir en el navegador la dirección que muestra la terminal.
6. 
## Credenciales de prueba

- Administrador: admin@admin.com — contraseña: 123456
- Cliente: cliente@food.com — contraseña: cliente123

## Costo de envío

El envío es gratuito: COSTO_ENVIO = 0.
La constante está definida en src/pages/store/cart/cart.ts.
El total del pedido se calcula como subtotal + envío.

## Registro de clientes

El registro inicia sesión automáticamente. Los datos de sesión
se guardan en userData, sin contraseña. No se mantiene una lista
permanente de usuarios registrados.

Al cerrar sesión, el cliente registrado no puede volver a ingresar
con esa contraseña. El login utiliza las cuentas de usuarios.json.

Los pedidos se conservan en pedidosLocales e incluyen los datos
del comprador en usuarioDto, sin contraseña.
