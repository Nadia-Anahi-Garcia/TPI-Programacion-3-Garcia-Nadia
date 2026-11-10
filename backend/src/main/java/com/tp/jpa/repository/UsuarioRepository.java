package com.tp.jpa.repository;

import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario> {

    public UsuarioRepository (){
        super(Usuario.class);
    }


    //BUSQUEDA POR MAIL

    public Optional<Usuario> buscarPorMail(String mail) {
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta JPQL: busca un usuario activo por su mail.
            // El parámetro :mail recibe el correo indicado al llamar al método.
            String jpql =  "SELECT u FROM Usuario u WHERE u.mail = :mail AND u.eliminado = false";
            TypedQuery<Usuario> q = em.createQuery(jpql, Usuario.class);
            q.setParameter("mail", mail);
            List<Usuario> usuarios = q.getResultList();
            return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));

        }finally {
            em.close();
        }
    }

    //BUSQUEDA DE PEDIDOS POR USUARIO

    public List <Pedido> buscarPedidosPorUsuario(Long idUsuario){

        EntityManager em = emf.createEntityManager();

        try {
            // Consulta JPQL: retorna los pedidos activos de un usuario.
            // Se navega desde Usuario hacia su colección u.pedidos mediante JOIN.
            // Se filtra por el ID del usuario y se excluyen las bajas lógicas.

            String jpql = "SELECT p FROM Usuario u JOIN u.pedidos p "
                    + "WHERE u.id = :uid AND p.eliminado = false";
            List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
                    .setParameter("uid", idUsuario)
                    .getResultList();
            return pedidos;

        }finally {
            em.close();
        }



    }
}
