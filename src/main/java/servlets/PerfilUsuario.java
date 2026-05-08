package servlets;

import capaDatos.ConexionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "PerfilUsuario", urlPatterns = {"/PerfilUsuario"})
public class PerfilUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            // Obtener parámetros enviados por el formulario
            int id_cliente = Integer.parseInt(request.getParameter("id_cliente"));
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String dni = request.getParameter("dni");
            int id_ciudad = Integer.parseInt(request.getParameter("id_ciudad"));

            ConexionBD con = new ConexionBD();
            Connection cn = con.Conectar();

            CallableStatement cs = cn.prepareCall("{CALL ActualizarPerfil(?,?,?,?,?,?,?)}");
            cs.setInt(1, id_cliente);
            cs.setString(2, nombres);
            cs.setString(3, apellidos);
            cs.setString(4, email);
            cs.setString(5, telefono);
            cs.setString(6, dni);
            cs.setInt(7, id_ciudad);

            ResultSet rs = cs.executeQuery();

            // Respuesta JSON manual
            if (rs.next()) {
                String estado = rs.getString("estado");
                String mensaje = rs.getString("mensaje");

                String json = "{"
                        + "\"estado\":\"" + estado + "\","
                        + "\"mensaje\":\"" + mensaje + "\""
                        + "}";

                out.print(json);
            } else {
                out.print("{\"estado\":\"ERROR\",\"mensaje\":\"Sin respuesta del procedimiento\"}");
            }

            rs.close();
            cs.close();
            con.Desconectar();

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"estado\":\"ERROR\",\"mensaje\":\"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }   

}
