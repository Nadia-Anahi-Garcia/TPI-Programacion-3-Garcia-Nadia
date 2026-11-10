package com.tp.jpa.model;

import com.tp.jpa.model.enums.Rol;


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
@EqualsAndHashCode(of = "mail", callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor ()

@Entity
@Table(name = "USUARIOS")
public class Usuario extends Base {

    //-------------------------------------------------
    // Atributos
    //-------------------------------------------------

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "MAIL")
    private String mail;
    @Column(name = "CELULAR")
    private String celular;
    @Column(name = "CONTRASENIA")
    private String contrasenia;
    @Column(name = "ROL")
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @Builder.Default
    private Set<Pedido> pedidos = new HashSet<>();



   public void addPedido(Pedido pedido) {
        if(pedido != null){
            pedidos.add(pedido);
        }

    }





}
