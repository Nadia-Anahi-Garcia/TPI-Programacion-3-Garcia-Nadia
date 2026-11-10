package com.tp.jpa.repository;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import jakarta.persistence.EntityManager;


import java.util.List;

public class CategoriaRepository extends  BaseRepository <Categoria> {

    public CategoriaRepository() {
        super(Categoria.class);
    }

    public List<Producto> buscarProductosPorCategoria(Long categoriaId){
        EntityManager em = emf.createEntityManager();

        try {
            /*
                Consulta JPQL: obtiene los productos activos de una categoría.
                Como la relación es unidireccional y Categoria es la propietaria,
                se parte de ella y se recorre su colección mediante JOIN c.productos.
                Se filtra por el ID recibido con el parámetro :catId y se excluyen
                los productos dados de baja mediante p.eliminado = false.
            */

            String jpql = "SELECT p FROM Categoria c JOIN c.productos p "
                    + "WHERE c.id = :catId AND p.eliminado = false";
            List<Producto> productos = em.createQuery(jpql, Producto.class)
                    .setParameter("catId", categoriaId)
                    .getResultList();
            return productos;
        } finally {
            em.close();
        }
    }

    public Producto agregarProducto (Long categoriaId, Producto producto){
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Categoria categoriaRecuperada = em.find(Categoria.class, categoriaId);

            if(categoriaRecuperada == null || categoriaRecuperada.isEliminado()){
                throw new IllegalArgumentException("La categoría no existe o está dada de baja.");
            }
            validarNombreProducto(categoriaRecuperada, producto.getNombre(), null);

            // Se persiste el producto para que reciba su ID.
            em.persist(producto);

            // Después se lo agrega a la colección de la categoría.
            categoriaRecuperada.addProducto(producto);

            em.getTransaction().commit();
            return producto;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // Validación
    private void validarNombreProducto(Categoria categoria, String nombre, Long productoId){
        for (Producto existente : categoria.getProductos()){
            boolean mismoProducto = existente.getId().equals(productoId);
            boolean mismoNombre = nombre.equalsIgnoreCase(existente.getNombre());

            if (!mismoProducto && mismoNombre){
                throw new IllegalArgumentException(
                        "La categoría " + categoria.getNombre() +
                                " ya tiene un producto con el nombre " + nombre + ". "
                );
            }
        }
    }

    /*
       Obtiene la categoría a la que pertenece el producto que se está modificando
       Como el producto no conoce a su categoría, se busca desde la categoría,
       uniendo (JOIN) con su lista de productos c.productos.
       Se filtra por el ID del producto con el parámetro :productoId.
       Con esa categoría se valida que el nuevo nombre no esté repetido.

     */

    public void validarNombreAlModificarProducto(Long productoId, String nuevoNombre) {
        EntityManager em = emf.createEntityManager();
        try {
            // Busca la categoría a la que pertenece el producto que se está editando.
            String jpql = "SELECT c FROM Categoria c JOIN c.productos p "
                    + "WHERE p.id = :productoId";

            List<Categoria> categorias = em.createQuery(jpql, Categoria.class)
                    .setParameter("productoId", productoId)
                    .getResultList();

            if (categorias.isEmpty()) {
                throw new IllegalArgumentException("El producto no tiene una categoría asociada.");
            }

            validarNombreProducto(categorias.get(0), nuevoNombre, productoId);
        } finally {
            em.close();
        }
    }
}
