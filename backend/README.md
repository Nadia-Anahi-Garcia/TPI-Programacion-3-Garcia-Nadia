#  TPI de Programación III — Food Store Backend

**Autora** Nadia García

## Descripción

Es una aplicación de consola para gestionar categorías, productos, usuarios y pedidos usando JPA/Hibernate con base de datos H2. Parte del proyecto del Segundo Parcial de Programación III y lo amplía para el TPI Food Store.
La aplicación permite crear, modificar, listar y dar de baja lógica los registros,
además de consultar los productos activos de una categoría.

Incluye:
- `BaseRepository<T>`: CRUD genérico (guardar, buscarPorId, listarActivos, eliminarLogico).
- `CategoriaRepository`: consulta JPQL de productos por categoría y alta de productos asociados.
- `ProductoRepository`: hereda el CRUD base.
- `UsuarioRepository`: búsqueda de usuarios activos por mail y consulta de pedidos por usuario.
- `PedidoRepository`: consulta de pedidos por estado y creación de pedidos en una única transacción.
- `Main`: menú de consola con ABM de categorías, productos y usuarios, gestión de pedidos y reportes.

Todas las bajas son lógicas (`eliminado = true`): los registros permanecen en la base.

## Gestión de pedidos

Permite seleccionar un usuario, una forma de pago y productos con sus cantidades.
Se valida la disponibilidad y el stock, se calculan los subtotales y el total, y se descuenta el stock.

El guardado se realiza en una única transacción. Si ocurre un error, se hace rollback y no se guardan cambios parciales.

También permite listar pedidos, cambiar su estado y realizar bajas lógicas. Al dar de baja un pedido, sus detalles permanecen en la base y el stock no se repone.

## Reportes

- Productos activos por categoría.
- Pedidos activos por usuario.
- Pedidos activos por estado.
- Total facturado de los pedidos activos en estado TERMINADO.

## Cómo ejecutar el proyecto

Requisito: tener instalado JDK 17.
1. Descomprimir el ZIP y abrir la carpeta del proyecto en IntelliJ IDEA como proyecto Gradle.
2. Esperar a que finalice la sincronización de Gradle.
3. Abrir `src/main/java/com/tp/jpa/Main.java`.
4. Ejecutar `Main` con el botón verde junto al método `main`.
5. Para cerrar la aplicación, elegir `0. Salir` en el menú principal.
   
### Ejecución desde terminal en Windows

Abrir una terminal dentro de la carpeta `backend`.

Para compilar el proyecto:

```powershell
.\gradlew.bat build
```

Para ejecutar el menú de consola:

```powershell
.\gradlew.bat run --console=plain
```

El proyecto incluye Gradle Wrapper, por lo que no es necesario instalar Gradle por separado.
La base de datos H2 se guarda en `data/jpa_db.mv.db` dentro del proyecto.
Si el archivo todavía no existe, se crea al ejecutar la aplicación.
