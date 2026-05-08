package capaEntidad;

public class Empleado {
    private int id_empleado;
    private String nombres;
    private String apellidos;
    private String email;
    private String contrasena;
    private String telefono;
    
    //metodos constructores
    public Empleado() {
    }

    public Empleado(int id_empleado, String nombres, String apellidos, String email, String contrasena,
            String telefono) {
        this.id_empleado = id_empleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
