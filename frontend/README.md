# Food Store - Evaluación de Programación III N.° 1


## Enlace al video. 

https://www.youtube.com/watch?v=Ah9fqsa5pT4


## Descripción

Food Store es una aplicación frontend desarrollada como evaluación de Programación III.

Permite visualizar un catálogo de productos, buscar productos por nombre, filtrarlos por categoría y agregarlos a un carrito de compras persistente.

El proyecto fue desarrollado con HTML5, CSS3 y TypeScript utilizando Vite. Conserva también el sistema de registro, inicio de sesión y protección de rutas realizado en el Trabajo Práctico Integrador de la unidad N° 4.

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
    ```
    pnpm dev
    ```
5. Abrir en el navegador la dirección indicada por Vite, generalmente: 
    ```
    http://localhost:5173
    ```

