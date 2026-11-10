package com.tp.jpa.model;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

@Getter
@SuperBuilder
@NoArgsConstructor

@Entity
@Table(name = "PEDIDOS")
public class Pedido extends Base implements Calculable {

    //-------------------------------------------------
    // Atributos
    //-------------------------------------------------

    @Column(name = "FECHA")
    private LocalDate fecha;
    @Column(name = "ESTADO")
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Builder.Default
    @Column(name = "TOTAL")
    private Double total = 0.0;
    @Column(name = "FORMA_DE_PAGO")
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private Set<DetallePedido> detallesPedido = new HashSet<>();


    //-------------------------------------------------
    // Constructor
    //-------------------------------------------------
    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago) {
        this();
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.total = 0.0;
    }


    //-------------------------------------------------
    // Getter y Setter
    //-------------------------------------------------



    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }


    @Override
    public void calcularTotal() {
        total = 0.0;
        for (DetallePedido detalle : detallesPedido) {
            total += detalle.getSubtotal();
        }
    }

    public void  addDetallePedido(int cantidad,Producto producto){
        DetallePedido detalle = new DetallePedido(cantidad, producto);
        detallesPedido.add(detalle);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto){
        for (DetallePedido detalle : detallesPedido) {
            if (detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }

        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto){
        DetallePedido detalle = findDetallePedidoByProducto(producto);

        if (detalle != null) {
            detallesPedido.remove(detalle);
            calcularTotal();
        }
    }


}
