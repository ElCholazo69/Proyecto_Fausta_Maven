package capaDatos;

import capaEntidad.Clientes;
import capaEntidad.Consulta;
import java.util.ArrayList;
import java.sql.*;

public class clsDatos {
    ConexionBD cnn;
    ResultSet rs;
    PreparedStatement sentencia;
    CallableStatement stmt;
    
    //metodo para listar los datos
    public ArrayList<Clientes> Listado() {
        ArrayList<Clientes> miLista = new ArrayList<>();
        String SQL_LISTAR = "CALL ListarClientes()";
        
        try {
            cnn = new ConexionBD();
            sentencia = cnn.Conectar().prepareStatement(SQL_LISTAR);
            rs = sentencia.executeQuery();
            
            while(rs.next()) {
                miLista.add(new Clientes(
                   rs.getInt("id_cliente"),
                   rs.getString("nombres"),
                   rs.getString("apellidos"),
                   rs.getString("email"),
                   rs.getString("contrasena"),     
                   rs.getString("telefono"),
                   rs.getString("dni"),
                   rs.getInt("id_ciudad")
                ));
            }
            rs.close();
            
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return miLista;
    }
    
    //metodo para insertar datos
    public void Insertar(Clientes objCli) {
        String SQL_INSERTAR = "CALL InsertarCliente(?,?,?,?,?,?,?)";
        
        try {
            cnn = new ConexionBD();
            stmt = cnn.Conectar().prepareCall(SQL_INSERTAR);
            stmt.setString(1, objCli.getNombres());
            stmt.setString(2, objCli.getApellidos());
            stmt.setString(3, objCli.getEmail());
            stmt.setString(4, objCli.getContrasena());
            stmt.setString(5, objCli.getTelefono());
            stmt.setString(6, objCli.getDni());
            stmt.setInt(7, objCli.getId_ciudad());
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void InsertarConsulta(Consulta objCons) {
        String SQL_INSERTAR = "CALL InsertarConsulta(?,?,?)";
        
         try {
            cnn = new ConexionBD();
            stmt = cnn.Conectar().prepareCall(SQL_INSERTAR);
            stmt.setInt(1, objCons.getId_cliente());
            stmt.setString(2, objCons.getAsunto());
            stmt.setString(3, objCons.getMotivo());
            stmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    //metodo para listar los registros de la tabla consulta
    public ArrayList<Consulta> obtenerConsultas() {
        ArrayList<Consulta> miLista = new ArrayList<>();
        String SQL_LISTAR = "CALL ListarConsulta()";
        
        try {
            cnn = new ConexionBD();
            sentencia = cnn.Conectar().prepareStatement(SQL_LISTAR);
            rs = sentencia.executeQuery();
            
            while(rs.next()) {
                miLista.add(new Consulta(
                   rs.getInt("id_consulta"),
                   rs.getInt("id_cliente"),
                   rs.getString("asunto"),
                   rs.getString("motivo")
                ));
            }
            rs.close();
            
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return miLista;
    }
    
}
