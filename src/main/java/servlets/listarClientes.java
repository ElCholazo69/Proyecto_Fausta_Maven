package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import capaDatos.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(name = "listarClientes", urlPatterns = {"/listarClientes"})
public class listarClientes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ConexionBD conexion = new ConexionBD();
        Connection con = conexion.Conectar();

        StringBuilder json = new StringBuilder("[");
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (con != null) {
                ps = con.prepareStatement("CALL ListarClientes()");
                rs = ps.executeQuery();

                while (rs.next()) {
                    json.append("{")
                        .append("\"id_cliente\":").append(rs.getInt("id_cliente")).append(",")
                        .append("\"nombres\":\"").append(rs.getString("nombres")).append("\",")
                        .append("\"apellidos\":\"").append(rs.getString("apellidos")).append("\",")
                        .append("\"telefono\":\"").append(rs.getString("telefono")).append("\",")
                        .append("\"email\":\"").append(rs.getString("email")).append("\"")
                        .append("},");
                }

                if (json.length() > 1) {
                    json.setLength(json.length() - 1);
                }
                json.append("]");
            } else {
                json.append("[]"); // Si la conexión falla
            }

            response.getWriter().print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("[]");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}