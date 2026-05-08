package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import capaDatos.ConexionBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet("/login")
public class login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String identificador = request.getParameter("email"); //correo o DNI
        String contrasena = request.getParameter("contrasena");

        if (identificador == null || contrasena == null || identificador.isEmpty() || contrasena.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Datos incompletos\"}");
            return;
        }

        try (Connection con = new ConexionBD().Conectar();
         CallableStatement cs = con.prepareCall("{ call LoginUsuario(?, ?) }")) {

        cs.setString(1, identificador);
        cs.setString(2, contrasena);

        ResultSet rs = cs.executeQuery();

        if (rs.next()) {
            String estado = rs.getString("estado");

            if ("OK".equals(estado)) {
                String tipo = rs.getString("tipo");
                int id = rs.getInt("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                String dni = rs.getString("dni");
                int ciudad = rs.getInt("id_ciudad");

           String jsonResponse = String.format(
                        "{\"estado\":\"%s\",\"tipo\":\"%s\",\"id\":%d,\"nombres\":\"%s\",\"apellidos\":\"%s\",\"email\":\"%s\",\"telefono\":\"%s\",\"dni\":\"%s\",\"id_ciudad\":%d}",
                        estado, tipo, id, nombres, apellidos, email, telefono, dni, ciudad
            );

            response.getWriter().write(jsonResponse);

            } else {
                // Errores devueltos desde el procedimiento
                String mensaje = rs.getString("mensaje");
                response.getWriter().write("{\"error\":\"" + mensaje + "\"}");
            }
        } else {
            response.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
        }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error en el servidor\"}");
        }
    }
}
