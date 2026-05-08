package servlets;

import capaDatos.ConexionBD;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ListarCiudades", urlPatterns = {"/ListarCiudades"})
public class ListarCiudades extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        ConexionBD con = new ConexionBD();
        Connection cn = con.Conectar();

        StringBuilder json = new StringBuilder();
        json.append("[");

        try {
            String sql = "SELECT id_ciudad, nombre FROM ciudad ORDER BY nombre ASC";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                    .append("\"id_ciudad\":").append(rs.getInt("id_ciudad")).append(",")
                    .append("\"nombre\":\"").append(rs.getString("nombre")).append("\"")
                    .append("}");
            }

            rs.close();
            ps.close();
            con.Desconectar();

        } catch (Exception e) {
            System.out.println("Error en ListarCiudades: " + e.getMessage());
        }
        json.append("]");
        out.print(json.toString());
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

}
