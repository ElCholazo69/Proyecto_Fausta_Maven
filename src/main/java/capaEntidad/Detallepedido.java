package capaEntidad;

public class Detallepedido {
    private int id_detalle;
    private int id_pedido;
    private int id_producto;
    private int cantidad;
    private float subtotal;
    
    //Campos extras para mostrar la información del producto
    private String nombre;
    private String imagen;
    private String tamano;

    //metodos constructores
    public Detallepedido() {
    }

    public Detallepedido(int id_detalle, int id_pedido, int id_producto, int cantidad, float subtotal, String nombre, String imagen, String tamano) {
        this.id_detalle = id_detalle;
        this.id_pedido = id_pedido;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.nombre = nombre;
        this.imagen = imagen;
        this.tamano = tamano;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    
}
