package capaEntidad;

public class Consulta {
    private int id_consulta;
    private int id_cliente;
    private String asunto;
    private String motivo;

    //metodos constructores
    public Consulta() {
    }

    public Consulta(int id_consulta, int id_cliente, String asunto, String motivo) {
        this.id_consulta = id_consulta;
        this.id_cliente = id_cliente;
        this.asunto = asunto;
        this.motivo = motivo;
    }

    public int getId_consulta() {
        return id_consulta;
    }

    public void setId_consulta(int id_consulta) {
        this.id_consulta = id_consulta;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

}
