package com.tp.jpa.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

@Getter
@Setter
@EqualsAndHashCode(of = "nombre", callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORIAS")
public class Categoria extends Base {

    //-------------------------------------------------
    // Atributos
    //-------------------------------------------------
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @Builder.Default
    private Set<Producto> productos = new HashSet<>();


    public void addProducto(Producto producto) {
        if (producto != null){
            productos.add(producto);
        }
    }

}
