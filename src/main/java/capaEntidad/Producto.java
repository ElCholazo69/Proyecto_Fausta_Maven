package capaEntidad;

public class Producto {
    private int id_producto;
    private String nombre;
    private String descripcion;
    private float precio1;
    private float precio2;
    private float precio3;
    private int stock;
    private String categoria;
    private String imagen;

    //metodos constructores
    public Producto() {
    }

    public Producto(int id_producto, String nombre, String descripcion, float precio1,float precio2,float precio3,int stock, String categoria,
            String imagen) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio1 = precio1;
        this.precio2 = precio2;
        this.precio3 = precio3;
        this.stock = stock;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio1() {
        return precio1;
    }

    public void setPrecio1(float precio1) {
        this.precio1 = precio1;
    }

    public float getPrecio2() {
        return precio2;
    }

    public void setPrecio2(float precio2) {
        this.precio2 = precio2;
    }

    public float getPrecio3() {
        return precio3;
    }

    public void setPrecio3(float precio3) {
        this.precio3 = precio3;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
}
