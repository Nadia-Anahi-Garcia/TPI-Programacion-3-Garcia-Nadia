package com.tp.jpa.repository;


import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;


import java.util.List;
import java.util.Optional;
/*
    Repositorio genérico con las operaciones de Guardar, buscar por id, listar
    los activos y baja lógica.
    Cada uno de los métodos abre su EntityManager al inicio y lo cierra en el
    bloque finally.
 */

public abstract class BaseRepository < T extends Base > {

    // ---- ATRIBUTOS ----
    private final Class<T> entityClass;
    protected final EntityManagerFactory emf;

    // ---- CONSTRUCTOR ----
    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    // ---- GETTER ----

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    // ---- GUARDAR ----

    /*
        Metodo que si el Id es igual a null se da de alta y utiliza persist, para
        que el Id lo asigne la base de datos.
        Caso que sea distinto de null se actualiza con el marge().
     */

    public T guardar(T entity){
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            T resultado;

            if (entity.getId() == null){
                // Si no tiene ID, se registra como una entidad nueva.
                em.persist(entity);
                resultado = entity;
            } else {
                // Si tiene ID, se actualiza mediante merge.
                resultado = em.merge(entity);
            }

            em.getTransaction().commit();
            return resultado;
        } catch (Exception e){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw  e;
        }finally {
                em.close();
        }

    }

    // ---- BUSCAR POR ID

    public Optional<T> buscarPorId (Long id){
        EntityManager em = emf.createEntityManager();
        try {
            T entidad = em.find(getEntityClass(), id);

            if (entidad != null){
                return Optional.of(entidad);
            }
            return  Optional.empty();
        }finally {
            em.close();
        }
    }

    // ---- LISTAR ACTIVOS

    /*
        Consulta JPQL: obtiene las entidades que no están dadas de baja,
        donde eliminado = false. Se crea con el nombre de la clase ( getEntityClass().getSimpleName())
        para que sirva para cualquier entidad que extienda de base.
     */

    public List<T> listarActivos() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery <T> query = em.createQuery("SELECT e FROM "
                    + getEntityClass().getSimpleName()
                    + " e WHERE e.eliminado = false", getEntityClass()
            );
            List <T> entidades = query.getResultList();
            return  entidades;
        }finally {
            em.close();
        }
    }

    // ---- BAJA LÓGICA ----

    public boolean eliminarLogico (Long id){
        EntityManager em = emf.createEntityManager();

        try {
            T entidad = em.find(getEntityClass(), id);

            if (entidad == null || entidad.isEliminado()){
                return false;
            }

            em.getTransaction().begin();
            entidad.setEliminado(true);
            em.merge(entidad);

            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }finally {
            em.close();
        }
    }
}
