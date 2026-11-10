package com.tp.jpa.model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor

@Entity
@Table(name = "DETALLE_PEDIDO")
public class DetallePedido extends  Base{

    //-------------------------------------------------
    // Atributos
    //-------------------------------------------------
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Column(name = "SUBTOTAL")
    private Double subtotal;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    //-------------------------------------------------
    // Constructor
    //-------------------------------------------------
    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * producto.getPrecio();
    }

    //-------------------------------------------------
    //  Setter
    //-------------------------------------------------

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        recalcularSubtotal();
    }

    //-------------------------------------------------
    //  Función para recalcular el subtotal
    //-------------------------------------------------
    private void recalcularSubtotal() {
        this.subtotal = cantidad * producto.getPrecio();
    }
}
