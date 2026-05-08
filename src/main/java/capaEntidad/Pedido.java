package capaEntidad;

import java.sql.Date;
import java.util.List;

public class Pedido {
    private int id_pedido;
    private int id_cliente;
    private int id_empleado;
    private Date fecha;
    private String estado;
    private String metodoEnvio;
    private String direccion;
    private String nombreCliente;
    private List<Detallepedido> detalles;
    private Descuento descuento;
    
    //metodos constructores
    public Pedido() {
    }

    public Pedido(int id_pedido, int id_cliente, int id_empleado, Date fecha, String estado, String metodoEnvio, String direccion, String nombreCliente, List<Detallepedido> detalles, Descuento descuento) {
        this.id_pedido = id_pedido;
        this.id_cliente = id_cliente;
        this.id_empleado = id_empleado;
        this.fecha = fecha;
        this.estado = estado;
        this.metodoEnvio = metodoEnvio;
        this.direccion = direccion;
        this.nombreCliente = nombreCliente;
        this.detalles = detalles;
        this.descuento = descuento;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Detallepedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detallepedido> detalles) {
        this.detalles = detalles;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

      
}
