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


@WebServlet(name = "listaProductos", urlPatterns = {"/listaProductos"})
public class listaProductos extends HttpServlet {

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
                ps = con.prepareStatement("CALL ListarProductos()");
                rs = ps.executeQuery();

                while (rs.next()) {
                    json.append("{")
                        .append("\"imagen\":\"").append(rs.getString("imagen")).append("\",")
                        .append("\"nombre\":\"").append(rs.getString("nombre")).append("\",")
                        .append("\"descripcion\":\"").append(rs.getString("descripcion")).append("\",")
                        .append("\"precio\":").append(rs.getDouble("precio")).append(",")
                        .append("\"categoria\":\"").append(rs.getString("categoria")).append("\",")
                        .append("\"stock\":").append(rs.getInt("stock"))
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