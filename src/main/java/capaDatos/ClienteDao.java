package capaDatos;

import capaEntidad.Clientes;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

public class ClienteDao {
    private final ConexionBD conexion;

    public ClienteDao() {
        conexion = new ConexionBD();
    }
    
    public String insertarCliente(Clientes cli){
        //Almacenar la conexión con la base de datos
        Connection cn =null;
        //Para ejecutar procedimientos almacenados
        CallableStatement cs=null;
        ResultSet rs = null;
        String mensaje = "";

        try {
            cn = conexion.Conectar();
            cs = cn.prepareCall("CALL InsertarCliente(?,?,?,?,?,?,?)");

            cs.setString(1, cli.getNombres());
            cs.setString(2, cli.getApellidos());
            cs.setString(3, cli.getEmail());
            cs.setString(4, cli.getContrasena());
            cs.setString(5, cli.getTelefono());
            cs.setString(6, cli.getDni());
            cs.setInt(7, cli.getId_ciudad());

            boolean hasResult = cs.execute();
            if (hasResult) {
                rs = cs.getResultSet();
                if (rs.next()) {
                    mensaje = rs.getString("mensaje"); // viene del SELECT del procedimiento almacenado
                }
            }
        } catch (SQLException e) {
            mensaje = "Error al registrar cliente: " + e.getMessage();
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (cn != null) conexion.Desconectar();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }

        return mensaje;
    }
}