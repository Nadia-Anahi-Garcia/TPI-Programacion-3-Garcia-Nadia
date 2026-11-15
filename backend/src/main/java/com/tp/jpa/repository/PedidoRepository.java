package com.tp.jpa.repository;

import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PedidoRepository extends BaseRepository <Pedido> {

    public PedidoRepository(){
        super(Pedido.class);
    }

    // BUSCAR POR ESTADO

    public List <Pedido> buscarPorEstado(Estado estado){
        EntityManager em = emf.createEntityManager();

        try {
            // Consulta JPQL: retorna los pedidos activos con el estado indicado.
            // El parámetro :estado permite seleccionar el estado buscado.
            // Se excluyen los pedidos dados de baja.
            String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado AND p.eliminado = false";
            List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
                    .setParameter("estado", estado)
                    .getResultList();

            return pedidos;

        }finally {
            em.close();
        }
    }

    // CREAR PEDIDO

    public Pedido crearPedido(Long idUsuario, FormaPago formaPago, Map<Long, Integer> items){
        EntityManager em = emf.createEntityManager();
        try {
            if(formaPago == null){
                throw new IllegalArgumentException("Error. El pedido debe contener al menos un producto.");
            }

            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class,idUsuario);

            if(usuario == null || usuario.isEliminado()){
                throw new IllegalArgumentException("Error. El usuario no existe o está dado de baja.");
            }

            Pedido pedido = new Pedido(LocalDate.now(), Estado.PENDIENTE, formaPago);

            for (Map.Entry<Long, Integer> item : items.entrySet()) {
                Long idProducto = item.getKey();
                Integer cantidad = item.getValue();

                if (cantidad == null || cantidad <= 0) {
                    throw new IllegalArgumentException("Error. La cantidad debe ser mayor a cero.");
                }

                Producto producto = em.find(Producto.class, idProducto);

                if (producto == null || producto.isEliminado()) {
                    throw new IllegalArgumentException("Error. El producto no existe o está dado de baja.");
                }

                if (!Boolean.TRUE.equals(producto.getDisponible())) {
                    throw new IllegalArgumentException(
                            "El producto " + producto.getNombre() + " no está disponible."
                    );
                }
                if (producto.getStock() < cantidad) {
                    throw new IllegalArgumentException(
                            "Stock insuficiente para " + producto.getNombre()
                                    + ". Stock disponible: " + producto.getStock()
                    );
                }

                pedido.addDetallePedido(cantidad, producto);
                producto.setStock(producto.getStock() - cantidad);
            }

            pedido.calcularTotal();
            em.persist(pedido);
            usuario.getPedidos().add(pedido);

            em.getTransaction().commit();

            return pedido;

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }

    }

}
