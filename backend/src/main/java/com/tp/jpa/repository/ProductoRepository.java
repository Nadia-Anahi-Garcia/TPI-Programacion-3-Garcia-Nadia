package com.tp.jpa.repository;

import com.tp.jpa.model.Producto;

/*
    Repositorio de Produto, hereda el CRUD de BaseRepository. En este
    caso Producto no conoce a categoria porque es una relación unidireccional
    la consulta por categoria se implementa en CategoriaRepository.
 */
public class ProductoRepository extends BaseRepository<Producto>{
    public ProductoRepository() {
        super(Producto.class);
    }
}
