package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
import com.tp.jpa.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
                            System.out.println("====== Submenú: Productos ======");
                            menuProductos(scanner, categoriaRepo, productoRepo);
                            break;
                        case "3":
                            System.out.println("====== Submenú: Usuarios ======");
                            menuUsuarios(scanner,  usuarioRepo);
                            break;
                        case "4":
                            System.out.println("====== Submenú: Pedidos ======");
                            menuPedidos(scanner, usuarioRepo, productoRepo, pedidoRepo);
                            break;
                        case "5":
                            System.out.println("====== Submenú: Reportes ======");
                            menuReportes(scanner, categoriaRepo);
                            break;
                        case "0":
                            System.out.println("Saliendo de la aplicación...");
                            break;
                        default:
                            System.out.println("Opción inválida, por favor marque la opción correcta.");
                            break;
                    }
                }catch (RuntimeException e) {
                    System.out.println(
                            "La operación no se pudo completar : " + e.getMessage()
                    );
                }

            } while (!opcion.equals("0"));

        }catch (java.util.NoSuchElementException e) {
            System.out.println("\nFin de la entrada. Saliendo...");
        }finally {
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
            System.out.println("\n--- GESTIÓN DE CATEGORÍAS ---");
            System.out.println("1. Alta");
            System.out.println("2. Modificar");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("----- Alta de categoría -----");
                        altaCategoria(scanner, categoriaRepo);
                        break;
                    case "2":
                        System.out.println("----- Modificación de categoría -----");
                        modificarCategoria(scanner, categoriaRepo);
                        break;
                    case "3":
                        System.out.println(" ----- Baja lógica de categoría -----");
                        bajaCategoria(scanner, categoriaRepo);
                        break;
                    case "4":
                        System.out.println("----- Listado de categorías -----");
                        listarCategorias(categoriaRepo);
                        break;
                    case "0":
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            }catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!opcion.equals("0"));

    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE PRODUCTOS
    // ------------------------------------------------------------------

    private static void menuProductos( Scanner scanner, CategoriaRepository categoriaRepo,
                                       ProductoRepository productoRepo) {
       String opcion;

        do {
            System.out.println("\n===== GESTIÓN DE PRODUCTOS =====");
            System.out.println("1. Alta");
            System.out.println("2. Modificar");
            System.out.println("3. Baja lógica");
            System.out.println("4. Listado");
            System.out.println("0. Volver");

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Alta de producto -----");
                        altaProducto(scanner, categoriaRepo);
                        break;
                    case "2":
                        System.out.println("\n----- Modificar producto -----");
                        modificarProducto(scanner, productoRepo,categoriaRepo);
                        break;
                    case "3":
                        System.out.println("\n----- Baja lógica de producto -----");
                        bajaProducto(scanner, productoRepo);
                        break;
                    case "4":
                        System.out.println("\n----- Productos activos -----");
                        listarProductos(categoriaRepo);
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
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

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        altaUsuario(scanner, usuarioRepo);
                        break;
                    case "2":
                        modificarUsuario(scanner, usuarioRepo);
                        break;
                    case "3":
                        bajaUsuario(scanner, usuarioRepo);
                        break;
                    case "4":
                        listarUsuarios(usuarioRepo);
                        break;
                    case "5":
                        buscarUsuarioPorMail(scanner, usuarioRepo);
                        break;
                    case "0":
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
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

            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        altaPedido(scanner, usuarioRepo, productoRepo, pedidoRepo);
                        break;
                    case "2":
                        cambiarEstadoPedido(scanner, pedidoRepo);
                        break;
                    case "3":
                        bajaPedido(scanner, pedidoRepo);
                        break;
                    case "4":
                        listarPedidos(pedidoRepo);
                        break;
                    case "5":
                        reportePedidosPorUsuario(scanner, usuarioRepo);
                        break;
                    case "6":
                        reportePedidosPorEstado(scanner, pedidoRepo);
                        break;
                    case "0":
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (!opcion.equals("0"));
    }

    // ------------------------------------------------------------------
    // SUBMENÚ DE REPORTES
    // ------------------------------------------------------------------

    private static void menuReportes(Scanner scanner, CategoriaRepository categoriaRepo) {
        String opcion;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Productos por categoría");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine().trim();

            try {
                switch (opcion) {
                    case "1":
                        System.out.println("\n----- Productos por categoría -----");
                        reporteProductosPorCategoria(scanner, categoriaRepo);
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (!opcion.equals("0"));
    }
    // ------------------------------------------------------------------
    // ABM de Categorías
    // ------------------------------------------------------------------
    private static void altaCategoria( Scanner scanner, CategoriaRepository categoriaRepo) {

        System.out.print("\n Ingrese el nombre de la categoría: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("Error, el nombre no puede estar vacío.");
            return;
        }
        System.out.print("\n Descripción (opcional): ");
        String descripcion = scanner.nextLine().trim();

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        try {
            Categoria categoriaGuardada = categoriaRepo.guardar(categoria);
            System.out.println(
                "La categoría ha sido creada. ID: " + categoriaGuardada.getId()
            );
        } catch (RuntimeException e) {
            System.out.println("Error al guardar la categoría: " + e.getMessage());
        }
    }

    private static void modificarCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {
        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            return;
        }

        Long id = leerId(scanner, "Ingrese el ID de la categoría a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            System.out.println("La categoría está dada de baja.");
            return;
        }

        System.out.println("Nombre actual: " + categoria.getNombre());
        System.out.println("Descripción actual: " + categoria.getDescripcion());
        System.out.println("Deje el campo vacío para conservar su valor.");

        System.out.print("Ingrese nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("Ingrese la nueva descripción: ");
        String nuevaDescripcion = scanner.nextLine().trim();

        if (!nuevoNombre.isEmpty()) {
            categoria.setNombre(nuevoNombre);
        }

        if (!nuevaDescripcion.isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion);
        }

        Categoria categoriaGuardada = categoriaRepo.guardar(categoria);

        System.out.println(
                "Categoría modificada: " + categoriaGuardada.getNombre()
        );
    }

    private static void bajaCategoria(Scanner scanner, CategoriaRepository categoriaRepo) {

        Long id = leerId(scanner, "Ingrese el ID de la categoría: ");

        if (id == null) {
            return;
        }

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("La categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            System.out.println("La categoría ya está dada de baja.");
            return;
        }

        List<Producto> productos =
                categoriaRepo.buscarProductosPorCategoria(id);

        if (!productos.isEmpty()) {
            System.out.println(
                    "Error: la categoría tiene " + productos.size()
                            + " producto(s) activo(s). "
                            + "Primero debe dar de baja sus productos."
            );
            return;
        }

        boolean eliminada = categoriaRepo.eliminarLogico(id);

        if (eliminada) {
            System.out.println(
                    "Categoría dada de baja: " + categoria.getNombre()
            );
        } else {
            System.out.println(
                    "Error: la categoría no existe o ya está dada de baja."
            );
        }
    }

    private static List<Categoria> listarCategorias(CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = categoriaRepo.listarActivos();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías activas.");
            return categorias;
        }

        System.out.println("\n----- Categorías activas -----");

        for (Categoria categoria : categorias) {
            System.out.println(
                    "ID: " + categoria.getId()
                            + " | Nombre: " + categoria.getNombre()
                            + " | Descripción: " + categoria.getDescripcion()
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
            System.out.println("Primero debe crear una categoría.");
            return;
        }

        Long categoriaId = leerId(scanner, "Ingrese el ID de la categoría: ");

        if (categoriaId == null) {
            return;
        }

        Optional<Categoria> resultado =
                categoriaRepo.buscarPorId(categoriaId);

        if (resultado.isEmpty()) {
            System.out.println("Error, la categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            System.out.println("Error, la categoría está dada de baja.");
            return;
        }

        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("Error, el nombre no puede estar vacío.");
            return;
        }

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();

        Double precio = leerPrecio(scanner, "Ingrese el precio (mayor a 0): ");

        if (precio == null) {
            return;
        }

        Integer stock = leerStock(scanner, "Ingrese el Stock (mayor o igual a 0): ");

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

            System.out.println("Error, debe ingresar S o N.");
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

            System.out.println(
                    "Producto creado. ID: " + productoGuardado.getId()
                            + " | Categoría: " + categoria.getNombre()
            );
        } catch (IllegalArgumentException e) {
            // Si la categoría no existe, está dada de baja o ya tiene un producto con ese nombre.
            // Lanza el error.
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void modificarProducto(Scanner scanner, ProductoRepository productoRepo,CategoriaRepository categoriaRepo) {

        List<Producto> productos = productoRepo.listarActivos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos activos.");
            return;
        }

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }

        Long id = leerId(scanner, " Ingrese el ID del producto a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("Error, el producto no existe.");
            return;
        }

        Producto producto = resultado.get();

        if (producto.isEliminado()) {
            System.out.println("Error, el producto está dado de baja.");
            return;
        }

        System.out.println("Nombre actual: " + producto.getNombre());
        System.out.println("Precio actual: " + producto.getPrecio());
        System.out.println("Stock actual: " + producto.getStock());
        System.out.println("Deje el campo vacío para conservar su valor.");

        System.out.print("Ingrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("Ingrese el nuevo precio: ");
        String textoPrecio = scanner.nextLine().trim();

        System.out.print("Ingrese el nuevo stock: ");
        String textoStock = scanner.nextLine().trim();

        Double nuevoPrecio = producto.getPrecio();
        int nuevoStock = producto.getStock();

        // Se validan los datos antes de modificar y guardar el producto.
        if (!textoPrecio.isEmpty()) {
            try {
                nuevoPrecio = Double.parseDouble(textoPrecio);

                if (!Double.isFinite(nuevoPrecio) || nuevoPrecio <= 0) {
                    System.out.println("Error, el precio debe ser mayor a 0.");
                    return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Error, el precio debe ser numérico (usar un punto para los decimales).");
                return;
            }
        }

        if (!textoStock.isEmpty()) {
            try {
                nuevoStock = Integer.parseInt(textoStock);

                if (nuevoStock < 0) {
                    System.out.println("Error, el stock no puede ser negativo.");
                    return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Error, el stock debe ser un número entero.");
                return;
            }
        }

        if (!nuevoNombre.isEmpty()) {
            try {
                categoriaRepo.validarNombreAlModificarProducto(producto.getId(), nuevoNombre);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }

            producto.setNombre(nuevoNombre);
        }

        producto.setPrecio(nuevoPrecio);
        producto.setStock(nuevoStock);

        Producto productoGuardado = productoRepo.guardar(producto);

        System.out.println(
                "Producto modificado: " + productoGuardado.getNombre()
        );
    }

    private static void bajaProducto(Scanner scanner, ProductoRepository productoRepo) {

        Long id = leerId(scanner, "Ingrese el ID del producto: ");

        if (id == null) {
            return;
        }

        // Se recupera el producto para mostrar su nombre al confirmar.
        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        boolean eliminado = productoRepo.eliminarLogico(id);

        if (eliminado && resultado.isPresent()) {
            System.out.println(
                    "Producto dado de baja: " + resultado.get().getNombre()
            );
        } else {
            System.out.println(
                    "Error, el producto no existe o ya está dado de baja."
            );
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
            System.out.println(
                    "No hay productos activos asociados a categorías activas."
            );
        }
    }
    // ------------------------------------------------------------------
    // ABM DE USUARIOS
    // ------------------------------------------------------------------


    private static List<Usuario> listarUsuarios(UsuarioRepository usuarioRepo) {
        List<Usuario> usuarios = usuarioRepo.listarActivos();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios activos.");
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
    public static void altaUsuario ( Scanner scanner, UsuarioRepository usuarioRepo){
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Ingrese el apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("Ingrese el mail: ");
        String mail = scanner.nextLine().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || mail.isEmpty()) {
            System.out.println("Error: nombre, apellido y mail son obligatorios.");
            return;
        }
        Optional<Usuario> existente = usuarioRepo.buscarPorMail(mail);
        if (existente.isPresent()) {
            System.out.println("Error: ya existe un usuario activo con ese mail.");
            return;
        }
        System.out.print("Ingrese el celular (opcional): ");
        String celular = scanner.nextLine().trim();

        System.out.print("Ingrese la contraseña: ");
        String contrasenia = scanner.nextLine();
        if (contrasenia.isBlank()) {
            System.out.println("Error: la contraseña no puede estar vacía.");
            return;
        }

        System.out.println("Seleccione el rol:");
        System.out.println("1. ADMIN");
        System.out.println("2. USUARIO");
        System.out.print("Opción: ");
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
                System.out.println("Error: el rol seleccionado no es válido.");
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

        System.out.println(
                "Usuario creado. ID: " + usuarioGuardado.getId()
                        + " | Nombre: " + usuarioGuardado.getNombre()
                        + " " + usuarioGuardado.getApellido()
        );

    }
    private static void modificarUsuario ( Scanner scanner, UsuarioRepository usuarioRepo){
        List<Usuario> usuarios = listarUsuarios(usuarioRepo);
        if (usuarios.isEmpty()) {
            return;
        }

        Long id = leerId(scanner, "Ingrese el ID del usuario a modificar: ");

        if (id == null) {
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorId(id);

        if (resultado.isEmpty()) {
            System.out.println("Error: el usuario no existe.");
            return;
        }
        Usuario usuario = resultado.get();

        if (usuario.isEliminado()) {
            System.out.println("Error: el usuario está dado de baja.");
            return;
        }

        System.out.println("Nombre actual: " + usuario.getNombre());
        System.out.println("Apellido actual: " + usuario.getApellido());
        System.out.println("Mail actual: " + usuario.getMail());
        System.out.println("Celular actual: " + usuario.getCelular());
        System.out.println("Deje los campos vacíos para conservar sus valores.");

        System.out.print("Ingrese el nuevo nombre: ");
        String nuevoNombre = scanner.nextLine().trim();

        System.out.print("Ingrese el nuevo apellido: ");
        String nuevoApellido = scanner.nextLine().trim();

        System.out.print("Ingrese el nuevo mail: ");
        String nuevoMail = scanner.nextLine().trim();

        System.out.print("Ingrese el nuevo celular: ");
        String nuevoCelular = scanner.nextLine().trim();

        System.out.print("Ingrese la nueva contraseña: ");
        String nuevaContrasenia = scanner.nextLine();

        if (!nuevoMail.isEmpty()) {
            Optional<Usuario> usuarioConMail = usuarioRepo.buscarPorMail(nuevoMail);

            if (usuarioConMail.isPresent()
                    && !usuarioConMail.get().getId().equals(usuario.getId())) {

                System.out.println("Error: otro usuario activo ya utiliza ese mail.");
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

        System.out.println(
                "Usuario modificado: " + usuarioGuardado.getNombre()
                        + " " + usuarioGuardado.getApellido()
        );



    }
    private static void bajaUsuario(Scanner scanner, UsuarioRepository usuarioRepo) {

        Long id = leerId(scanner, "Ingrese el ID del usuario: ");

        if (id == null) {
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorId(id);

        boolean eliminado = usuarioRepo.eliminarLogico(id);

        if (eliminado && resultado.isPresent()) {
            Usuario usuario = resultado.get();

            System.out.println(
                    "Usuario dado de baja: " + usuario.getNombre()
                            + " " + usuario.getApellido()
            );
        } else {
            System.out.println(
                    "Error: el usuario no existe o ya está dado de baja."
            );
        }
    }
    private static void buscarUsuarioPorMail(Scanner scanner, UsuarioRepository usuarioRepo) {

        System.out.print("Ingrese el mail del usuario: ");
        String mail = scanner.nextLine().trim();

        if (mail.isEmpty()) {
            System.out.println("Error: debe ingresar un mail.");
            return;
        }

        Optional<Usuario> resultado = usuarioRepo.buscarPorMail(mail);

        if (resultado.isEmpty()) {
            System.out.println("No existe un usuario activo con ese mail.");
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


    // ------------------------------------------------------------------
    // Reportes
    // ------------------------------------------------------------------

    private static void reporteProductosPorCategoria(
            Scanner scanner, CategoriaRepository categoriaRepo) {

        List<Categoria> categorias = listarCategorias(categoriaRepo);

        if (categorias.isEmpty()) {
            return;
        }

        Long categoriaId = leerId(scanner, "Ingrese el ID de la categoría: ");

        if (categoriaId == null) {
            return;
        }

        Optional<Categoria> resultado =
                categoriaRepo.buscarPorId(categoriaId);

        if (resultado.isEmpty()) {
            System.out.println("Error, la categoría no existe.");
            return;
        }

        Categoria categoria = resultado.get();

        if (categoria.isEliminado()) {
            System.out.println("Error, la categoría está dada de baja.");
            return;
        }

        List<Producto> productos =
                categoriaRepo.buscarProductosPorCategoria(categoriaId);

        if (productos.isEmpty()) {
            System.out.println("No hay productos activos en esta categoría.");
            return;
        }

        System.out.println("Categoría: " + categoria.getNombre());

        for (Producto producto : productos) {
            mostrarProducto(producto);
        }
    }



    // ------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // ------------------------------------------------------------------

    // Lee un ID. Si el formato o el valor son inválidos, devuelve null.
    private static Long leerId(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();

        try {
            Long id = Long.parseLong(texto);

            if (id <= 0) {
                System.out.println("Error, el ID debe ser mayor a 0.");
                return null;
            }

            return id;

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error, el ID debe ser un número entero válido."
            );
            return null;
        }
    }

    // Lee y valida el precio para el alta de un producto.
    private static Double leerPrecio(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();

        try {
            Double precio = Double.parseDouble(texto);

            if (!Double.isFinite(precio) || precio <= 0) {
                System.out.println("Error, el precio debe ser mayor a 0.");
                return null;
            }

            return precio;

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error, debe ingresar un precio numérico usando punto para los decimales."
            );
            return null;
        }
    }

    // Lee y valida el stock para el alta de un producto.
    private static Integer leerStock(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();

        try {
            int stock = Integer.parseInt(texto);

            if (stock < 0) {
                System.out.println("Error, el stock no puede ser negativo.");
                return null;
            }

            return stock;

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error, el stock debe ser un número entero válido."
            );
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
}



















