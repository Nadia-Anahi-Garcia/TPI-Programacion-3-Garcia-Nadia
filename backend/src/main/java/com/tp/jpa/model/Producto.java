package com.tp.jpa.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "nombre", callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "PRODUCTOS")
public class Producto extends Base {
    //-------------------------------------------------
    // Atributos
    //-------------------------------------------------
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PRECIO")
    private Double precio;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "STOCK")
    private int stock;
    @Column(name = "IMAGEN")
    private String imagen;
    @Builder.Default
    @Column(name = "DISPONIBLE")
    private Boolean disponible = true;

}