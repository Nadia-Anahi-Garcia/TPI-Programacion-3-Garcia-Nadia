package com.tp.jpa;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;

public class PruebaPersistencia {
    public static void main(String[] args) {
        // OBTENEMOS EL EntityManager POR MEDIO DEL JPAUtil
        EntityManager em = JPAUtil.getEntityManager();

        try {
            // INICIAMOS LA TRANSACCIÓN PARA GUARDAR LOS DATOS Y LAS RELACIONES
            em.getTransaction().begin();
            //  CREAMOS 2 USUARIOS
            Usuario u1 = Usuario.builder()
                    .nombre("Romina")
                    .apellido("García")
                    .mail("rgarcia@correo.com")
                    .celular("2994656869")
                    .contrasenia("256")
                    .rol(Rol.USUARIO)
                    .build();
            Usuario u2 = Usuario.builder()
                    .nombre("María")
                    .apellido("Gatti")
                    .mail("mgatti@correo.com")
                    .celular("2994258869")
                    .contrasenia("257")
                    .rol(Rol.USUARIO)
                    .build();

            // PERSISTENCIA DE USUARIOS
            em.persist(u1);
            em.persist(u2);

            // CREAMOS LAS TRES CATEGORIAS
            Categoria gaseosas = Categoria.builder()
                    .nombre("Gaseosas")
                    .descripcion("Bebidas gaseosas")
                    .build();
            Categoria pizzas = Categoria.builder()
                    .nombre("Pizzas")
                    .descripcion("Pizzas a la piedra")
                    .build();

            Categoria ensaladas = Categoria.builder()
                    .nombre("Ensaladas")
                    .descripcion("Ensaladas con vegetales frescos")
                    .build();
            // PERSISTIR CATEGORIAS
            em.persist(gaseosas);
            em.persist(pizzas);
            em.persist(ensaladas);

            // CREAMOS 10 PRODUCTOS
            Producto pizzaMuzzarella = Producto.builder()
                    .nombre("Pizza Muzzarella")
                    .precio(8500.0)
                    .descripcion("Muzzarella y aceitunas")
                    .stock(10)
                    .imagen("pizza-muzzarella.jpg")
                    .disponible(true)
                    .build();
            Producto pizzaNapolitana = Producto.builder()
                    .nombre("Pizza Napolitana")
                    .precio(9200.0)
                    .descripcion("Tomate, muzzarella y ajo")
                    .stock(12)
                    .imagen("pizza-napolitana.jpg")
                    .disponible(true)
                    .build();

            Producto pizzaEspecial = Producto.builder()
                    .nombre("Pizza Especial")
                    .precio(10500.0)
                    .descripcion("Jamón, morrón y muzzarella")
                    .stock(12)
                    .imagen("pizza-especial.jpg")
                    .disponible(true)
                    .build();

            Producto pizzaFugazzeta = Producto.builder()
                    .nombre("Pizza Fugazzeta")
                    .precio(9800.0)
                    .descripcion("Cebolla y muzzarella")
                    .stock(10)
                    .imagen("pizza-fugazzeta.jpg")
                    .disponible(true)
                    .build();

            Producto cocaCola = Producto.builder()
                    .nombre("Coca Cola")
                    .precio(2300.0)
                    .descripcion("Gaseosa sabor cola de 1,5 litros")
                    .stock(25)
                    .imagen("coca-cola.jpg")
                    .disponible(true)
                    .build();

            Producto sprite = Producto.builder()
                    .nombre("Sprite")
                    .precio(2300.0)
                    .descripcion("Gaseosa lima-limón de 1,5 litros")
                    .stock(25)
                    .imagen("sprite.jpg")
                    .disponible(true)
                    .build();

            Producto fanta = Producto.builder()
                    .nombre("Fanta")
                    .precio(2300.0)
                    .descripcion("Gaseosa sabor naranja de 1,5 litros")
                    .stock(18)
                    .imagen("fanta.jpg")
                    .disponible(true)
                    .build();

            Producto ensaladaCesar = Producto.builder()
                    .nombre("Ensalada César")
                    .precio(6500.0)
                    .descripcion("Lechuga, pollo y croutons")
                    .stock(5)
                    .imagen("ensalada-cesar.jpg")
                    .disponible(true)
                    .build();

            Producto ensaladaMixta = Producto.builder()
                    .nombre("Ensalada Mixta")
                    .precio(6500.0)
                    .descripcion("Lechuga, tomate y zanahoria")
                    .stock(5)
                    .imagen("ensalada-mixta.jpg")
                    .disponible(true)
                    .build();

            Producto ensaladaVegana = Producto.builder()
                    .nombre("Ensalada Vegana")
                    .precio(6200.0)
                    .descripcion("Vegetales frescos y semillas")
                    .stock(8)
                    .imagen("ensalada-vegana.jpg")
                    .disponible(true)
                    .build();
            // PERSISTENCIA DE LOS 10 PRODUCTOS
            em.persist(pizzaMuzzarella);
            em.persist(pizzaNapolitana);
            em.persist(pizzaEspecial);
            em.persist(pizzaFugazzeta);
            em.persist(cocaCola);
            em.persist(sprite);
            em.persist(fanta);
            em.persist(ensaladaCesar);
            em.persist(ensaladaMixta);
            em.persist(ensaladaVegana);

            // RECUPERAMOS LAS CATEGORIAS
            Categoria categoriaPizzas = em.find(Categoria.class, pizzas.getId());
            Categoria categoriaGaseosas = em.find(Categoria.class, gaseosas.getId());
            Categoria categoriaEnsaladas = em.find(Categoria.class, ensaladas.getId());
            // ASOCIAMOS CADA PRODUCTO CON SU CATEGORIA
            categoriaPizzas.addProducto(pizzaMuzzarella);
            categoriaPizzas.addProducto(pizzaNapolitana);
            categoriaPizzas.addProducto(pizzaEspecial);
            categoriaPizzas.addProducto(pizzaFugazzeta);

            categoriaGaseosas.addProducto(cocaCola);
            categoriaGaseosas.addProducto(sprite);
            categoriaGaseosas.addProducto(fanta);

            categoriaEnsaladas.addProducto(ensaladaCesar);
            categoriaEnsaladas.addProducto(ensaladaMixta);
            categoriaEnsaladas.addProducto(ensaladaVegana);
            // CREAMOS 3 PEDIDOS Y AGREGAMOS DOS DETALLES
            Pedido pedido1 = Pedido.builder()
                    .fecha(LocalDate.now())
                    .estado(Estado.PENDIENTE)
                    .formaPago(FormaPago.EFECTIVO)
                    .build();


            pedido1.addDetallePedido(2, pizzaMuzzarella);
            pedido1.addDetallePedido(1, cocaCola);

            Pedido pedido2 = Pedido.builder()
                    .fecha(LocalDate.now())
                    .estado(Estado.CONFIRMADO)
                    .formaPago(FormaPago.TARJETA)
                    .build();

            pedido2.addDetallePedido(1, pizzaEspecial);
            pedido2.addDetallePedido(2, sprite);

            Pedido pedido3 = Pedido.builder()
                    .fecha(LocalDate.now())
                    .estado(Estado.TERMINADO)
                    .formaPago(FormaPago.TRANSFERENCIA)
                    .build();

            pedido3.addDetallePedido(1, ensaladaCesar);
            pedido3.addDetallePedido(1, fanta);

            // ASOCIAMOS LOS PEDIDOS A LOS USUARIOS
            u1.addPedido(pedido1);
            u1.addPedido(pedido2);
            u2.addPedido(pedido3);

            // CONFIRMAMOS LA TRANSACCIÓN
            em.getTransaction().commit();

            System.out.println("===== DATOS PERSISTIDOS =====");
            System.out.println("Usuarios: " + u1.getNombre() + " (id " + u1.getId() + "), "
                    + u2.getNombre() + " (id " + u2.getId() + ")");
            System.out.println("Categorías: " + gaseosas.getNombre() + ", "
                    + pizzas.getNombre() + ", " + ensaladas.getNombre());
            System.out.println("Productos persistidos: 10");
            System.out.println("Pedido 1 (id " + pedido1.getId() + ") total: $" + pedido1.getTotal());
            System.out.println("Pedido 2 (id " + pedido2.getId() + ") total: $" + pedido2.getTotal());
            System.out.println("Pedido 3 (id " + pedido3.getId() + ") total: $" + pedido3.getTotal());

            // ACTUALIZAR PRODUCTOS
            em.getTransaction().begin();
            pizzaMuzzarella.setPrecio(9500.0);
            sprite.setStock(20);
            em.getTransaction().commit();

            System.out.println("===== PRODUCTOS ACTUALIZADOS =====");
            System.out.println(pizzaMuzzarella.getNombre() + " -> nuevo precio: $" + pizzaMuzzarella.getPrecio());
            System.out.println(sprite.getNombre() + " -> nuevo stock: " + sprite.getStock());

            // BUSCAR USUARIO POR ID
            em.clear();
            System.out.println("===== BÚSQUEDAS =====");
            Usuario usuarioBuscado = em.find(Usuario.class, u1.getId());
            System.out.println("Usuario buscado por ID: "
                    + usuarioBuscado.getNombre() + " "
                    + usuarioBuscado.getApellido());

            // BUSCAR USUARIO POR MAIL
            Usuario usuarioPorMail = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.mail = :mail", Usuario.class)
                    .setParameter("mail", u2.getMail())
                    .getSingleResult();

            System.out.println("Usuario buscado por mail: "
                    + usuarioPorMail.getNombre() + " "
                    + usuarioPorMail.getApellido() + " - "
                    + usuarioPorMail.getMail());
            // BORRAR UN PRODUCTO

            System.out.println("===== BORRADO DE PRODUCTO =====");
            em.getTransaction().begin();
            Producto productoBorrar = em.find(Producto.class, ensaladaMixta.getId());

            if (productoBorrar != null) {

                Long cantidad = em.createQuery(
                                "SELECT COUNT(d) FROM DetallePedido d WHERE d.producto = :producto",
                                Long.class)
                        .setParameter("producto", productoBorrar)
                        .getSingleResult();

                if (cantidad == 0) {
                    em.remove(productoBorrar);
                    System.out.println("Producto eliminado correctamente.");
                } else {
                    System.out.println("No se puede eliminar: el producto está asociado a un pedido.");
                }
            }

            em.getTransaction().commit();

        } catch (Exception e){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            em.close();
            JPAUtil.close();
        }


    }

}
