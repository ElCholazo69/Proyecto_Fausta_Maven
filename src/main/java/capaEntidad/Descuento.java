package capaEntidad;

public class Descuento {
    private int id_descuento;
    private String codigo;
    private float descuento;

    public Descuento() {
    }

    public Descuento(int id_descuento, String codigo, float descuento) {
        this.id_descuento = id_descuento;
        this.codigo = codigo;
        this.descuento = descuento;
    }

    public int getId_descuento() {
        return id_descuento;
    }

    public void setId_descuento(int id_descuento) {
        this.id_descuento = id_descuento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
    
}
