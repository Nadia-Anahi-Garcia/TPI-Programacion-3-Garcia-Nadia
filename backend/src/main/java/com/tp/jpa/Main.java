package com.tp.jpa;

import com.tp.jpa.model.*;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
import com.tp.jpa.repository.PedidoRepository;

import java.util.*;

public class Main {
    // ==================== MENÚ PRINCIPAL ====================
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            CategoriaRepository categoriaRepo = new CategoriaRepository();
            ProductoRepository productoRepo = new ProductoRepository();
            UsuarioRepository usuarioRepo = new UsuarioRepository();
            PedidoRepository pedidoRepo = new PedidoRepository();

            System.out.println("===========================================");
            System.out.println("    Sistema de Gestión de Pedidos - JPA    ");
            System.out.println("===========================================");

            String opcion;
            do {
                System.out.println("\n================================");
                System.out.println("         MENÚ PRINCIPAL");
                System.out.println("================================");
                System.out.println("  1. Gestión de Categorías");
                System.out.println("  2. Gestión de Productos");
                System.out.println("  3. Gestión de Usuario");
                System.out.println("  4. Gestión de Pedidos");
                System.out.println("  5. Reportes");
                System.out.println("  0. Salir");
                System.out.println("--------------------------------");

                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextLine().trim();

                try {
                    switch (opcion) {
                        case "1":
                            System.out.println("\n====== Submenú: Categorías ======");
                            menuCategorias(scanner, categoriaRepo);
                            break;
                        case "2":
                            System.out.println("\n====== Submenú: Productos ======");
                            menuProductos(scanner, categoriaRepo, productoRepo);
                            break;
                        case "3":
                            System.out.println("\n====== Submenú: Usuarios ======");
                            menuUsuarios(scanner, usuarioRepo);
                            break;
                        case "4":
                            System.out.println("\n====== Submenú: Pedidos ======");
                            menuPedidos(scanner, usuarioRepo, productoRepo, pedidoRepo);
                            break;
                        case "5":
                            System.out.println("\n====== Submenú: Reportes ======");
                            menuReportes(scanner, categoriaRepo, usuarioRepo, pedidoRepo);
                            break;
                        case "0":
                            System.out.println("\nSaliendo de la aplicación...");
                            break;
                        default:
                            mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                            break;
                    }
                } catch (RuntimeException e) {
                    mostrarAviso(
                            "La operación no se pudo completar. " + e.getMessage()
                    );
                }

            } while (!opcion.equals("0"));

        } catch (java.util.NoSuchElementException e) {
            System.out.println("\nFin de la entrada. Saliendo...");
        } finally {
            scanner.close();
            JPAUtil.close();
        }

    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE CATEGORÍAS
    // ------------------------------------------------------------------
    public static void menuCategorias(Scanner scanner, CategoriaRepository categoriaRepo) {
        String opcion;
        do {
            System.out.println("\n===== GESTIÓN DE CATEGORÍAS =====");
            System.out.println("1. Alta");
            System.out.println("2. Modificar");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");

            System.out.print("\nSeleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Alta de categoría -----\n");
                        altaCategoria(scanner, categoriaRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Modificación de categoría -----\n");
                        modificarCategoria(scanner, categoriaRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Baja lógica de categoría -----\n");
                        bajaCategoria(scanner, categoriaRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Listado de categorías -----\n");
                        listarCategorias(categoriaRepo);
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                        break;
                }
            } catch (RuntimeException e) {
                mostrarAviso(e.getMessage());
            }
        } while (!opcion.equals("0"));

    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE PRODUCTOS
    // ------------------------------------------------------------------

    private static void menuProductos(Scanner scanner, CategoriaRepository categoriaRepo,
                                      ProductoRepository productoRepo) {
        String opcion;

        do {
            System.out.println("\n===== GESTIÓN DE PRODUCTOS =====");
            System.out.println("1. Alta");
            System.out.println("2. Modificar");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");

            System.out.print("\nSeleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Alta de producto -----\n");
                        altaProducto(scanner, categoriaRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Modificar producto -----\n");
                        modificarProducto(scanner, productoRepo, categoriaRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Baja lógica de producto -----\n");
                        bajaProducto(scanner, productoRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Productos activos -----\n");
                        listarProductos(categoriaRepo);
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                        break;
                }
            } catch (RuntimeException e) {
                mostrarAviso(e.getMessage());
            }
        } while (!opcion.equals("0"));
    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE USUARIOS
    // ------------------------------------------------------------------

    private static void menuUsuarios(
            Scanner scanner, UsuarioRepository usuarioRepo) {

        String opcion;

        do {
            System.out.println("\n===== GESTIÓN DE USUARIOS =====");
            System.out.println("1. Alta");
            System.out.println("2. Modificar");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("5. Buscar por mail");
            System.out.println("0. Volver");

            System.out.print("\nSeleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Alta de usuario -----\n");
                        altaUsuario(scanner, usuarioRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Modificación de usuario -----\n");
                        modificarUsuario(scanner, usuarioRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Baja lógica de usuario -----\n");
                        bajaUsuario(scanner, usuarioRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Listado de usuarios -----\n");
                        listarUsuarios(usuarioRepo);
                        break;
                    case "5":
                        System.out.println("\n----- Búsqueda de usuario por mail -----\n");
                        buscarUsuarioPorMail(scanner, usuarioRepo);
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                        break;
                }
            } catch (RuntimeException e) {
                mostrarAviso(e.getMessage());
            }

        } while (!opcion.equals("0"));
    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE PEDIDOS
    // ------------------------------------------------------------------

    private static void menuPedidos(
            Scanner scanner,
            UsuarioRepository usuarioRepo,
            ProductoRepository productoRepo,
            PedidoRepository pedidoRepo) {

        String opcion;

        do {
            System.out.println("\n===== GESTIÓN DE PEDIDOS =====");
            System.out.println("1. Alta de pedido");
            System.out.println("2. Cambiar estado");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("5. Pedidos por usuario");
            System.out.println("6. Pedidos por estado");
            System.out.println("0. Volver");

            System.out.print("\nSeleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Alta de pedido -----\n");
                        altaPedido(scanner, usuarioRepo, productoRepo, pedidoRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Cambio de estado de pedido -----\n");
                        cambiarEstadoPedido(scanner, pedidoRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Baja lógica de pedido -----\n");
                        bajaPedido(scanner, pedidoRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Listado de pedidos -----\n");
                        listarPedidos(pedidoRepo);
                        break;
                    case "5":
                        System.out.println("\n----- Pedidos por usuario -----\n");
                        reportePedidosPorUsuario(scanner, usuarioRepo);
                        break;
                    case "6":
                        System.out.println("\n----- Pedidos por estado -----\n");
                        reportePedidosPorEstado(scanner, pedidoRepo);
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                        break;
                }
            } catch (RuntimeException e) {
                mostrarAviso(e.getMessage());
            }

        } while (!opcion.equals("0"));
    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE REPORTES
    // ------------------------------------------------------------------

    private static void menuReportes(
            Scanner scanner,
            CategoriaRepository categoriaRepo,
            UsuarioRepository usuarioRepo,
            PedidoRepository pedidoRepo) {

        String opcion;

        do {
            System.out.println("\n===== REPORTES =====");
            System.out.println("1. Productos por categoría");
            System.out.println("2. Pedidos por usuario");
            System.out.println("3. Pedidos por estado");
            System.out.println("4. Total facturado");
            System.out.println("0. Volver");

            System.out.print("\nSeleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Productos por categoría -----\n");
                        reporteProductosPorCategoria(scanner, categoriaRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Pedidos por usuario -----\n");
                        reportePedidosPorUsuario(scanner, usuarioRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Pedidos por estado -----\n");
                        reportePedidosPorEstado(scanner, pedidoRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Total facturado -----\n");
                        reporteTotalFacturado(pedidoRepo);
                        break;
                    case "0":
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        mostrarAviso("Opción inválida. Seleccione una opción del menú.");
                        break;
                }
            } catch (RuntimeException e) {
                mostrarAviso(e.getMessage());
            }

        } while (!opcion.equals("0"));
    }

    // ------------------------------------------------------------------
    // ABM de Categorías
    // ------------------------------------------------------------------
    private static void altaCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {

        System.out.print("\nIngrese el nombre de la categoría: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            mostrarAviso("El nombre no puede estar vacío.");
            return;
        }
        System.out.print("\n Descripción (opcional): ");
        String descripcion = scanner.nextLine().trim();

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        try {
            Categoria categoriaGuardada = categoriaRepo.guardar(categoria);
            System.out.println("\n--------------------------------------------------");
            System.out.println("CATEGORÍA CREADA CORRECTAMENTE");
            System.out.println("--------------------------------------------------");
            System.out.println("ID: " + categoriaGuardada.getId());
            System.out.println("--------------------------------------------------\n");
        } catch (RuntimeException e) {
            mostrarAviso("No se pudo guardar la categoría. " + e.getMessage());
        }
    }

    private static void modificarCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {
        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            return;
        }

        Long id = leerId(scanner, "\nIngrese el ID de la categoría a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            mostrarAviso("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            mostrarAviso("La categoría está dada de baja.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("CATEGORIA A MODIFICAR - ID: " + categoria.getId());
        System.out.println("--------------------------------------------------");

        System.out.println("Nombre actual: " + categoria.getNombre());
        System.out.println("Descripción actual: " + categoria.getDescripcion());
        System.out.println("\nDeje el campo vacío para conservar su valor.");

        System.out.print("\nIngrese nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("\nIngrese la nueva descripción: ");
        String nuevaDescripcion = scanner.nextLine().trim();

        if (!nuevoNombre.isEmpty()) {
            categoria.setNombre(nuevoNombre);
        }

        if (!nuevaDescripcion.isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion);
        }

        Categoria categoriaGuardada = categoriaRepo.guardar(categoria);

        System.out.println("\n--------------------------------------------------");
        System.out.println("Categoría modificada: " + categoriaGuardada.getNombre());
        System.out.println("--------------------------------------------------");
    }

    private static void bajaCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            return;
        }
        Long id = leerId(scanner, "\nIngrese el ID de la categoría: ");

        if (id == null) {
            return;
        }

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            mostrarAviso("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            mostrarAviso("La categoría ya está dada de baja.");
            return;
        }

        List<Producto> productos =
                categoriaRepo.buscarProductosPorCategoria(id);

        if (!productos.isEmpty()) {
            mostrarAviso(
                    "\nNo se puede dar de baja la categoría: tiene "
                            + productos.size() + " producto(s) activo(s)."
                            + "\nPrimero debe dar de baja sus productos."
            );
            return;
        }

        boolean eliminada = categoriaRepo.eliminarLogico(id);

        if (eliminada) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Categoría dada de baja: " + categoria.getNombre());
            System.out.println("--------------------------------------------------");
        } else {
            mostrarAviso("La categoría no existe o ya está dada de baja.");
        }
    }

    private static List<Categoria> listarCategorias(CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = categoriaRepo.listarActivos();

        if (categorias.isEmpty()) {
            mostrarAviso("No hay categorías activas.");
            return categorias;
        }

        System.out.println("\n----- Categorías activas -----\n");

        for (Categoria categoria : categorias) {
            System.out.printf(
                    "ID: %d | Nombre: %s | Descripción: %s%n",
                    categoria.getId(),
                    categoria.getNombre(),
                    categoria.getDescripcion()
            );
        }

        return categorias;
    }

    // ------------------------------------------------------------------
    // ABM de Productos
    // ------------------------------------------------------------------

    private static void altaProducto(Scanner scanner, CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            mostrarAviso("Primero debe crear una categoría.");
            return;
        }

        Long categoriaId = leerId(scanner, "\nIngrese el ID de la categoría: ");

        if (categoriaId == null) {
            return;
        }

        Optional<Categoria> resultado =
                categoriaRepo.buscarPorId(categoriaId);

        if (resultado.isEmpty()) {
            mostrarAviso("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            mostrarAviso("La categoría está dada de baja.");
            return;
        }

        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            mostrarAviso("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Double precio = leerPrecio(scanner, "\nIngrese el precio (mayor a 0): ");

        if (precio == null) {
            return;
        }

        Integer stock = leerStock(scanner, "\nIngrese el Stock (mayor o igual a 0): ");

        if (stock == null) {
            return;
        }

        System.out.print("Imagen (opcional): ");
        String imagen = scanner.nextLine().trim();

        System.out.print("Disponible (S/N, Enter = S): ");
        String respuesta = scanner.nextLine().trim();

        if (!respuesta.isEmpty()
                && !respuesta.equalsIgnoreCase("S")
                && !respuesta.equalsIgnoreCase("N")) {

            mostrarAviso("Debe ingresar S o N.");
            return;
        }

        boolean disponible =
                respuesta.isEmpty() || respuesta.equalsIgnoreCase("S");

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setDisponible(disponible);

        try {
            Producto productoGuardado =
                    categoriaRepo.agregarProducto(categoriaId, producto);

            System.out.println("\n--------------------------------------------------");
            System.out.println("PRODUCTO CREADO CORRECTAMENTE");
            System.out.println("--------------------------------------------------");
            System.out.println("ID: " + productoGuardado.getId());
            System.out.println("Categoría: " + categoria.getNombre());
            System.out.println("--------------------------------------------------\n");
        } catch (IllegalArgumentException e) {
            // Si la categoría no existe, está dada de baja o ya tiene un producto con ese nombre.
            // Lanza el error.
            mostrarAviso(e.getMessage());
        }
    }

    private static void modificarProducto(Scanner scanner, ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {

        List<Producto> productos = productoRepo.listarActivos();

        if (productos.isEmpty()) {
            mostrarAviso("No hay productos activos.");
            return;
        }

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }

        Long id = leerId(scanner, " \nIngrese el ID del producto a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            mostrarAviso("El producto no existe.");
            return;
        }

        Producto producto = resultado.get();

        if (producto.isEliminado()) {
            mostrarAviso("El producto está dado de baja.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("Nombre actual: " + producto.getNombre());
        System.out.println("Precio actual: " + producto.getPrecio());
        System.out.println("Stock actual: " + producto.getStock());
        System.out.println("\nDeje el campo vacío para conservar su valor.");
        System.out.println("\n--------------------------------------------------");

        System.out.print("\nIngrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("\nIngrese el nuevo precio: ");
        String textoPrecio = scanner.nextLine().trim();

        System.out.print("\nIngrese el nuevo stock: ");
        String textoStock = scanner.nextLine().trim();

        Double nuevoPrecio = producto.getPrecio();
        int nuevoStock = producto.getStock();

        // Se validan los datos antes de modificar y guardar el producto.
        if (!textoPrecio.isEmpty()) {
            try {
                nuevoPrecio = Double.parseDouble(textoPrecio);

                if (!Double.isFinite(nuevoPrecio) || nuevoPrecio <= 0) {
                    mostrarAviso("El precio debe ser mayor a 0.");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarAviso("El precio debe ser numérico. Use un punto para los decimales.");
                return;
            }
        }

        if (!textoStock.isEmpty()) {
            try {
                nuevoStock = Integer.parseInt(textoStock);

                if (nuevoStock < 0) {
                    mostrarAviso("El stock no puede ser negativo.");
                    return;
                }

            } catch (NumberFormatException e) {
                mostrarAviso("El stock debe ser un número entero.");
                return;
            }
        }

        if (!nuevoNombre.isEmpty()) {
            try {
                categoriaRepo.validarNombreAlModificarProducto(producto.getId(), nuevoNombre);
            } catch (IllegalArgumentException e) {
                mostrarAviso(e.getMessage());
                return;
            }

            producto.setNombre(nuevoNombre);
        }

        producto.setPrecio(nuevoPrecio);
        producto.setStock(nuevoStock);

        Producto productoGuardado = productoRepo.guardar(producto);

        System.out.println("\n--------------------------------------------------");
        System.out.println("Producto modificado: " + productoGuardado.getNombre());
        System.out.println("--------------------------------------------------");
    }

    private static void bajaProducto(Scanner scanner, ProductoRepository productoRepo) {
        List<Producto> productos = productoRepo.listarActivos();

        if (productos.isEmpty()) {
            mostrarAviso("No hay productos activos.");
            return;
        }

        System.out.println("\n----- Productos activos -----\n");

        productos.forEach(producto ->
                System.out.printf(
                        "ID: %d | Nombre: %s | Stock: %d%n",
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock()
                )
        );

        System.out.println();

        Long id = leerId(scanner, "Ingrese el ID del producto: ");

        if (id == null) {
            return;
        }

        // Se recupera el producto para mostrar su nombre al confirmar.
        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        boolean eliminado = productoRepo.eliminarLogico(id);

        if (eliminado && resultado.isPresent()) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Producto dado de baja: " + resultado.get().getNombre());
            System.out.println("--------------------------------------------------");
        } else {
            mostrarAviso("El producto no existe o ya está dado de baja.");
        }
    }

    private static void listarProductos(CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = categoriaRepo.listarActivos();
        boolean hayProductos = false;


        for (Categoria categoria : categorias) {
            List<Producto> productos =
                    categoriaRepo.buscarProductosPorCategoria(categoria.getId());

            for (Producto producto : productos) {
                hayProductos = true;

                String disponibilidad =
                        Boolean.TRUE.equals(producto.getDisponible())
                                ? "Sí" : "No";

                System.out.println(
                        "ID: " + producto.getId()
                                + " | Nombre: " + producto.getNombre()
                                + " | Precio: " + producto.getPrecio()
                                + " | Stock: " + producto.getStock()
                                + " | Disponible: " + disponibilidad
                                + " | Categoría: " + categoria.getNombre()
                );
            }
        }

        if (!hayProductos) {
            mostrarAviso("No hay productos activos asociados a categorías activas.");
        }
    }
    // ------------------------------------------------------------------
    // ABM DE USUARIOS
    // ------------------------------------------------------------------


    private static List<Usuario> listarUsuarios(UsuarioRepository usuarioRepo) {
        List<Usuario> usuarios = usuarioRepo.listarActivos();

        if (usuarios.isEmpty()) {
            mostrarAviso("No hay usuarios activos.");
            return usuarios;
        }

        System.out.println("\n----- Usuarios activos -----");

        for (Usuario usuario : usuarios) {
            System.out.println(
                    "ID: " + usuario.getId()
                            + " | Nombre: " + usuario.getNombre()
                            + " " + usuario.getApellido()
                            + " | Mail: " + usuario.getMail()
                            + " | Rol: " + usuario.getRol()
            );
        }

        return usuarios;
    }

    public static void altaUsuario(Scanner scanner, UsuarioRepository usuarioRepo) {
        System.out.print("\nIngrese el nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("\nIngrese el apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("\nIngrese el mail: ");
        String mail = scanner.nextLine().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || mail.isEmpty()) {
            mostrarAviso("Nombre, apellido y mail son obligatorios.");
            return;
        }
        Optional<Usuario> existente = usuarioRepo.buscarPorMail(mail);
        if (existente.isPresent()) {
            mostrarAviso("Ya existe un usuario activo con ese mail.");
            return;
        }
        System.out.print("\nIngrese el celular (opcional): ");
        String celular = scanner.nextLine().trim();

        System.out.print("\nIngrese la contraseña: ");
        String contrasenia = scanner.nextLine();
        if (contrasenia.isBlank()) {
            mostrarAviso("La contraseña no puede estar vacía.");
            return;
        }

        System.out.println("Seleccione el rol:");
        System.out.println("1. ADMIN");
        System.out.println("2. USUARIO");


        System.out.print("\nOpción: ");
        String opcionRol = scanner.nextLine().trim();

        Rol rol;

        switch (opcionRol) {
            case "1":
                rol = Rol.ADMIN;
                break;
            case "2":
                rol = Rol.USUARIO;
                break;
            default:
                mostrarAviso("El rol seleccionado no es válido.");
                return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setCelular(celular);
        usuario.setContrasenia(contrasenia);
        usuario.setRol(rol);

        Usuario usuarioGuardado = usuarioRepo.guardar(usuario);

        System.out.println("\n--------------------------------------------------");
        System.out.println("USUARIO CREADO CORRECTAMENTE");
        System.out.println("--------------------------------------------------");
        System.out.println("ID: " + usuarioGuardado.getId());
        System.out.println(
                "Nombre: " + usuarioGuardado.getNombre()
                        + " " + usuarioGuardado.getApellido()
        );
        System.out.println("Mail: " + usuarioGuardado.getMail());
        System.out.println("Rol: " + usuarioGuardado.getRol());
        System.out.println("--------------------------------------------------\n");

    }

    private static void modificarUsuario(Scanner scanner, UsuarioRepository usuarioRepo) {
        List<Usuario> usuarios = listarUsuarios(usuarioRepo);
        if (usuarios.isEmpty()) {
            return;
        }

        Long id = leerId(scanner, "\nIngrese el ID del usuario a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            mostrarAviso("El usuario no existe.");
            return;
        }
        Usuario usuario = resultado.get();

        if (usuario.isEliminado()) {
            mostrarAviso("El usuario está dado de baja.");
            return;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.println("USUARIO A MODIFICAR - ID: " + usuario.getId());
        System.out.println("--------------------------------------------------");
        System.out.println("Nombre actual: " + usuario.getNombre());
        System.out.println("Apellido actual: " + usuario.getApellido());
        System.out.println("Mail actual: " + usuario.getMail());
        System.out.println("Celular actual: " + usuario.getCelular());
        System.out.println("--------------------------------------------------");

        System.out.println(
                "\nDeje los campos vacíos para conservar sus valores.\n"
        );

        System.out.print("\nIngrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("\nIngrese el nuevo apellido: ");
        String nuevoApellido = scanner.nextLine().trim();

        System.out.print("\nIngrese el nuevo mail: ");
        String nuevoMail = scanner.nextLine().trim();

        System.out.print("\nIngrese el nuevo celular: ");
        String nuevoCelular = scanner.nextLine().trim();

        System.out.print("\nIngrese la nueva contraseña: ");
        String nuevaContrasenia = scanner.nextLine();

        if (!nuevoMail.isEmpty()) {
            Optional<Usuario> usuarioConMail = usuarioRepo.buscarPorMail(nuevoMail);

            if (usuarioConMail.isPresent()
                    && !usuarioConMail.get().getId().equals(usuario.getId())) {

                mostrarAviso("Otro usuario activo ya utiliza ese mail.");
                return;
            }
        }

        if (!nuevoNombre.isEmpty()) {
            usuario.setNombre(nuevoNombre);
        }

        if (!nuevoApellido.isEmpty()) {
            usuario.setApellido(nuevoApellido);
        }

        if (!nuevoMail.isEmpty()) {
            usuario.setMail(nuevoMail);
        }

        if (!nuevoCelular.isEmpty()) {
            usuario.setCelular(nuevoCelular);
        }

        if (!nuevaContrasenia.isBlank()) {
            usuario.setContrasenia(nuevaContrasenia);
        }

        Usuario usuarioGuardado = usuarioRepo.guardar(usuario);

        System.out.println("\n--------------------------------------------------");
        System.out.println(
                "Usuario modificado: " + usuarioGuardado.getNombre()
                        + " " + usuarioGuardado.getApellido());
        System.out.println("--------------------------------------------------");


    }

    private static void bajaUsuario(Scanner scanner, UsuarioRepository usuarioRepo) {

        List<Usuario> usuarios = listarUsuarios(usuarioRepo);

        if (usuarios.isEmpty()) {
            return;
        }
        Long id = leerId(scanner, "\nIngrese el ID del usuario: ");

        if (id == null) {
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorId(id);

        boolean eliminado = usuarioRepo.eliminarLogico(id);

        if (eliminado && resultado.isPresent()) {
            Usuario usuario = resultado.get();

            System.out.println("\n--------------------------------------------------");
            System.out.println(
                    "Usuario dado de baja: " + usuario.getNombre()
                            + " " + usuario.getApellido());
            System.out.println("--------------------------------------------------");
        } else {
            mostrarAviso("El usuario no existe o ya está dado de baja.");
        }
    }

    private static void buscarUsuarioPorMail(Scanner scanner, UsuarioRepository usuarioRepo) {

        System.out.print("\nIngrese el mail del usuario: ");
        String mail = scanner.nextLine().trim();

        if (mail.isEmpty()) {
            mostrarAviso("Debe ingresar un mail.");
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorMail(mail);

        if (resultado.isEmpty()) {
            mostrarAviso("No existe un usuario activo con ese mail.");
            return;
        }

        Usuario usuario = resultado.get();

        System.out.println("\n----- Usuario encontrado -----");
        System.out.println("ID: " + usuario.getId());
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Apellido: " + usuario.getApellido());
        System.out.println("Mail: " + usuario.getMail());
        System.out.println("Celular: " + usuario.getCelular());
        System.out.println("Rol: " + usuario.getRol());
    }

    // ------------------------------------------------------------------
    // ABM DE PEDIDOS
    // ------------------------------------------------------------------

    private static void altaPedido(Scanner scanner, UsuarioRepository usuarioRepo, ProductoRepository productoRepo,
                                   PedidoRepository pedidoRepo) {

        // Seleccionar un usuario activo.
        List<Usuario> usuarios = listarUsuarios(usuarioRepo);

        if (usuarios.isEmpty()) {
            return;
        }

        Long idUsuario = leerId(scanner, "\nIngrese el ID del usuario: ");

        if (idUsuario == null) {
            return;
        }

        Optional<Usuario> resultadoUsuario = usuarioRepo.buscarPorId(idUsuario);

        if (resultadoUsuario.isEmpty() || resultadoUsuario.get().isEliminado()) {
            mostrarAviso("El usuario no existe o está dado de baja.");
            return;
        }

        Usuario usuario = resultadoUsuario.get();

        // Seleccionar la forma de pago.
        System.out.println("----- Seleccione la forma de pago:");
        System.out.println("1. TARJETA");
        System.out.println("2. TRANSFERENCIA");
        System.out.println("3. EFECTIVO");

        System.out.print("\nOpción: ");

        String opcionPago = scanner.nextLine().trim();
        FormaPago formaPago;

        switch (opcionPago) {
            case "1":
                formaPago = FormaPago.TARJETA;
                break;
            case "2":
                formaPago = FormaPago.TRANSFERENCIA;
                break;
            case "3":
                formaPago = FormaPago.EFECTIVO;
                break;
            default:
                mostrarAviso("La forma de pago no es válida.");
                return;
        }


        Map<Long, Integer> items = new LinkedHashMap<>();

        while (true) {
            List<Producto> productos = listarProductosDisponibles(productoRepo);

            if (productos.isEmpty()) {
                return;
            }

            System.out.print("\nIngrese el ID del producto (0 para finalizar): ");
            String textoId = scanner.nextLine().trim();

            if (textoId.equals("0")) {
                break;
            }

            Long idProducto;

            try {
                idProducto = Long.parseLong(textoId);
            } catch (NumberFormatException e) {
                mostrarAviso("Ingrese un ID numérico válido.");
                continue;
            }

            // Buscar el producto dentro del catálogo disponible.
            Optional<Producto> resultadoProducto = productos.stream()
                    .filter(producto -> producto.getId().equals(idProducto))
                    .findFirst();

            if (resultadoProducto.isEmpty()) {
                mostrarAviso("Seleccione un producto del catálogo mostrado.");
                continue;
            }

            Producto producto = resultadoProducto.get();

            System.out.print("\nIngrese la cantidad: ");
            int cantidad;

            try {
                cantidad = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarAviso("La cantidad debe ser un número entero.");
                continue;
            }

            if (cantidad <= 0) {
                mostrarAviso("La cantidad debe ser mayor a cero.");
                continue;
            }

            int cantidadAnterior = items.getOrDefault(idProducto, 0);
            int stockRestante = Math.max(0, producto.getStock() - cantidadAnterior);

            if (cantidad > stockRestante) {
                mostrarAviso(
                        "Stock insuficiente. Puede agregar "
                                + stockRestante + " unidad(es) más."
                );
                continue;
            }

            items.put(idProducto, cantidadAnterior + cantidad);

            System.out.println(
                    "Producto agregado: " + producto.getNombre()
                            + " | Cantidad acumulada: " + items.get(idProducto)
            );

            String respuesta;

            do {
                System.out.print("¿Desea agregar otro producto? (S/N): ");
                respuesta = scanner.nextLine().trim();

                if (!respuesta.equalsIgnoreCase("S")
                        && !respuesta.equalsIgnoreCase("N")) {
                    mostrarAviso("Debe ingresar S o N.");
                }

            } while (!respuesta.equalsIgnoreCase("S")
                    && !respuesta.equalsIgnoreCase("N"));

            if (respuesta.equalsIgnoreCase("N")) {
                break;
            }
        }

        if (items.isEmpty()) {
            mostrarAviso("El pedido debe contener al menos un producto.");
            return;
        }

        // Guardar
        Pedido pedido = pedidoRepo.crearPedido(idUsuario, formaPago, items);

        System.out.println("\n--------------------------------------------------");
        System.out.println("PEDIDO CREADO CORRECTAMENTE");
        System.out.println("--------------------------------------------------");
        System.out.println("ID: " + pedido.getId());
        System.out.println("Fecha: " + pedido.getFecha());
        System.out.println(
                "Usuario: " + usuario.getNombre() + " " + usuario.getApellido()
        );
        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("Forma de pago: " + pedido.getFormaPago());
        System.out.println("Productos:");

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            System.out.printf(
                    Locale.US,
                    "%s | Cantidad: %d | Subtotal: $%.2f%n",
                    detalle.getProducto().getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotal()
            );
        }

        System.out.printf(Locale.US, "Total: $%.2f%n", pedido.getTotal());
    }

    private static void cambiarEstadoPedido(Scanner scanner, PedidoRepository pedidoRepo) {

        List<Pedido> pedidos = listarPedidos(pedidoRepo);

        if (pedidos.isEmpty()) {
            return;
        }
        Long id = leerId(scanner, "\nIngrese el ID del pedido: ");

        if (id == null) {
            return;
        }

        Optional<Pedido> resultado = pedidoRepo.buscarPorId(id);

        if (resultado.isEmpty() || resultado.get().isEliminado()) {
            mostrarAviso("El pedido no existe o está dado de baja.");
            return;
        }

        Pedido pedido = resultado.get();

        System.out.println("Estado actual: " + pedido.getEstado());

        Estado nuevoEstado = seleccionarEstado(scanner);

        if (nuevoEstado == null) {
            return;
        }

        pedido.setEstado(nuevoEstado);

        Pedido pedidoGuardado = pedidoRepo.guardar(pedido);

        System.out.println("\n--------------------------------------------------");
        System.out.println(
                "Pedido ID: " + pedidoGuardado.getId()
                        + " | Nuevo estado: " + pedidoGuardado.getEstado());
        System.out.println("--------------------------------------------------");
    }

    private static void bajaPedido(Scanner scanner, PedidoRepository pedidoRepo) {

        List<Pedido> pedidos = listarPedidos(pedidoRepo);

        if (pedidos.isEmpty()) {
            return;
        }
        Long id = leerId(scanner, "Ingrese el ID del pedido: ");

        if (id == null) {
            return;
        }

        Optional<Pedido> resultado = pedidoRepo.buscarPorId(id);

        boolean eliminado = pedidoRepo.eliminarLogico(id);

        if (eliminado && resultado.isPresent()) {
            Pedido pedido = resultado.get();


            System.out.println("\n--------------------------------------------------");
            System.out.printf(
                    Locale.US,
                    "Pedido dado de baja. ID: %d | Total: $%.2f%n",
                    pedido.getId(),
                    pedido.getTotal()
            );
            System.out.println("--------------------------------------------------");

        } else {
            mostrarAviso("El pedido no existe o ya está dado de baja.");
        }
    }

    private static  List<Pedido> listarPedidos(PedidoRepository pedidoRepo) {
        List<Pedido> pedidos = pedidoRepo.listarActivos();

        if (pedidos.isEmpty()) {
            mostrarAviso("No hay pedidos activos.");
            return pedidos;
        }

        System.out.println("\n----- Pedidos activos -----");

        for (Pedido pedido : pedidos) {
            System.out.printf(
                    Locale.US,
                    "ID: %d | Fecha: %s | Estado: %s | Forma de pago: %s | Total: $%.2f%n",
                    pedido.getId(),
                    pedido.getFecha(),
                    pedido.getEstado(),
                    pedido.getFormaPago(),
                    pedido.getTotal()
            );
        }
        System.out.println();
        return pedidos;
    }


    private static List<Producto> listarProductosDisponibles(ProductoRepository productoRepo) {

        List<Producto> productos = productoRepo.listarActivos()
                .stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getDisponible()))
                .filter(producto -> producto.getStock() > 0)
                .toList();

        if (productos.isEmpty()) {
            mostrarAviso("No hay productos disponibles con stock.");
            return productos;
        }

        System.out.println("\n------- Productos disponibles -------");

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }

        return productos;
    }


    // ------------------------------------------------------------------
    // Reportes
    // ------------------------------------------------------------------

    private static void reporteProductosPorCategoria(
            Scanner scanner, CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            return;
        }

        Long categoriaId = leerId(scanner, "\nIngrese el ID de la categoría: ");

        if (categoriaId == null) {
            return;
        }

        Optional<Categoria> resultado =
                categoriaRepo.buscarPorId(categoriaId);

        if (resultado.isEmpty()) {
            mostrarAviso("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            mostrarAviso("La categoría está dada de baja.");
            return;
        }

        List<Producto> productos =
                categoriaRepo.buscarProductosPorCategoria(categoriaId);

        if (productos.isEmpty()) {
            mostrarAviso("No hay productos activos en esta categoría.");
            return;
        }

        System.out.println("Categoría: " + categoria.getNombre());

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }
    }

    private static void reportePedidosPorUsuario(Scanner scanner, UsuarioRepository usuarioRepo) {

        List<Usuario> usuarios = listarUsuarios(usuarioRepo);

        if (usuarios.isEmpty()) {
            return;
        }

        Long idUsuario = leerId(scanner, "\nIngrese el ID del usuario: ");

        if (idUsuario == null) {
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorId(idUsuario);

        if (resultado.isEmpty() || resultado.get().isEliminado()) {
            mostrarAviso("El usuario no existe o está dado de baja.");
            return;
        }

        Usuario usuario = resultado.get();

        System.out.println(
                "\n----- Pedidos de " + usuario.getNombre()
                        + " " + usuario.getApellido() + " -----"
        );

        List<Pedido> pedidos = usuarioRepo.buscarPedidosPorUsuario(idUsuario);

        if (pedidos.isEmpty()) {
            mostrarAviso("El usuario no tiene pedidos activos.");
            return;
        }

        for (Pedido pedido : pedidos) {
            System.out.printf(
                    Locale.US,
                    "ID: %d | Fecha: %s | Estado: %s | Forma de pago: %s | Total: $%.2f%n",
                    pedido.getId(),
                    pedido.getFecha(),
                    pedido.getEstado(),
                    pedido.getFormaPago(),
                    pedido.getTotal()
            );
        }
    }

    private static void reportePedidosPorEstado(Scanner scanner, PedidoRepository pedidoRepo) {

        Estado estado = seleccionarEstado(scanner);

        if (estado == null) {
            return;
        }

        List<Pedido> pedidos = pedidoRepo.buscarPorEstado(estado);

        if (pedidos.isEmpty()) {
            mostrarAviso(
                    "No hay pedidos activos con estado " + estado + "."
            );
            return;
        }

        System.out.println("\n----- Pedidos con estado " + estado + " -----");

        for (Pedido pedido : pedidos) {
            System.out.printf(
                    Locale.US,
                    "ID: %d | Fecha: %s | Forma de pago: %s | Total: $%.2f%n",
                    pedido.getId(),
                    pedido.getFecha(),
                    pedido.getFormaPago(),
                    pedido.getTotal()
            );
        }
    }

    private static void reporteTotalFacturado(PedidoRepository pedidoRepo) {

        double total = pedidoRepo.buscarPorEstado(Estado.TERMINADO)
                .stream()
                .mapToDouble(pedido ->
                        pedido.getTotal() == null ? 0.0 : pedido.getTotal()
                )
                .sum();

        System.out.printf(Locale.US, "Total facturado: $%.2f%n", total);
    }


    // ------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ------------------------------------------------------------------

    private static void mostrarAviso(String mensaje) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("AVISO: " + mensaje);
        System.out.println("--------------------------------------------------\n");
    }

    // Lee un ID. Si el formato o el valor son inválidos, devuelve null.
    private static Long leerId(Scanner scanner, String mensaje) {
        System.out.print("\n" + mensaje);
        String texto = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(texto);

            if (id <= 0) {
                mostrarAviso("\nEl ID debe ser mayor a 0.");
                return null;
            }

            return id;

        } catch (NumberFormatException e) {
            mostrarAviso("El ID debe ser un número entero válido.");
            return null;
        }
    }

    // Lee y valida el precio para el alta de un producto.
    private static Double leerPrecio(Scanner scanner, String mensaje) {
        System.out.print("\n" + mensaje);
        String texto = scanner.nextLine().trim();

        try {
            Double precio = Double.parseDouble(texto);

            if (!Double.isFinite(precio) || precio <= 0) {
                mostrarAviso("El precio debe ser mayor a 0.");
                return null;
            }

            return precio;

        } catch (NumberFormatException e) {
            mostrarAviso(
                    "Debe ingresar un precio numérico usando punto para los decimales."
            );
            return null;
        }
    }


    // Lee y valida el stock para el alta de un producto.
    private static Integer leerStock(Scanner scanner, String mensaje) {
        System.out.print("\n" + mensaje);
        String texto = scanner.nextLine().trim();

        try {
            int stock = Integer.parseInt(texto);

            if (stock < 0) {
                Main.mostrarAviso("El stock no puede ser negativo.");
                return null;
            }

            return stock;

        } catch (NumberFormatException e) {
            Main.mostrarAviso("El stock debe ser un número entero válido.");
            return null;
        }
    }

    // Muestra los datos que solicita el reporte de productos por categoría.
    private static void mostrarProducto(Producto producto) {
        System.out.println(
                "ID: " + producto.getId()
                        + " | Nombre: " + producto.getNombre()
                        + " | Precio: " + producto.getPrecio()
                        + " | Stock: " + producto.getStock()
        );
    }

    private static Estado seleccionarEstado(Scanner scanner) {
        System.out.println("Seleccione el estado:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. CONFIRMADO");
        System.out.println("3. TERMINADO");
        System.out.println("4. CANCELADO");
        System.out.print("Opción: ");

        String opcion = scanner.nextLine().trim();

        switch (opcion) {
            case "1":
                return Estado.PENDIENTE;
            case "2":
                return Estado.CONFIRMADO;
            case "3":
                return Estado.TERMINADO;
            case "4":
                return Estado.CANCELADO;
            default:
                Main.mostrarAviso("El estado seleccionado no es válido.");
                return null;
        }
    }


}



















