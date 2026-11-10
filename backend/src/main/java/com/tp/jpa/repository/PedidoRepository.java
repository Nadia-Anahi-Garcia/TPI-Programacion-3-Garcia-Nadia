package com.tp.jpa.repository;

import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.enums.Estado;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PedidoRepository extends BaseRepository <Pedido> {

    public PedidoRepository(){
        super(Pedido.class);
    }

    // BUSCAR POR ESTADO

    List <Pedido> buscarPorEstado(Estado estado){
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
}
