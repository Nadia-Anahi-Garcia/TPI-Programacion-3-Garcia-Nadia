# Segundo Parcial de Programación III

**Autora** Nadia García

## Descripción

Es una aplicación de consola para gestionar categorías y productos usando JPA/Hibernate 
con base de datos H2. Extiende el TP de la Unidad 8 sin modificar sus entidades, enums, 
JPAUtil ni el persistence.xml. 
La aplicación permite crear, modificar, listar y dar de baja lógica los registros,
además de consultar los productos activos de una categoría.

Incluye:
- `BaseRepository<T>`: CRUD genérico (guardar, buscarPorId, listarActivos, eliminarLogico).
- `CategoriaRepository`: consulta JPQL de productos por categoría y alta de productos asociados.
- `ProductoRepository`: hereda el CRUD base.
- `Main`: menú de consola con ABM de categorías, ABM de productos y reporte.

Todas las bajas son lógicas (`eliminado = true`): los registros permanecen en la base.


## Cómo ejecutar el proyecto

Requisito: tener instalado JDK 17.
1. Descomprimir el ZIP y abrir la carpeta del proyecto en IntelliJ IDEA como proyecto Gradle.
2. Esperar a que finalice la sincronización de Gradle.
3. Abrir `src/main/java/com/tp/jpa/Main.java`.
4. Ejecutar `Main` con el botón verde junto al método `main`.
5. Para cerrar la aplicación, elegir `0. Salir` en el menú principal.

La base de datos H2 se guarda en `data/jpa_db.mv.db` dentro del proyecto.
Si el archivo todavía no existe, se crea al ejecutar la aplicación.