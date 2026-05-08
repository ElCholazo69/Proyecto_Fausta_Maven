package servlets;

import capaDatos.clsDatos;
import capaEntidad.Consulta;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ListarConsultas", urlPatterns = {"/ListarConsultas"})
public class ListarConsultas extends HttpServlet {

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            clsDatos datos = new clsDatos();
            List<Consulta> consultas = datos.obtenerConsultas();

            StringBuilder json = new StringBuilder();
            json.append("[");

            boolean primero = true;
            for (Consulta c : consultas) {
                if (!primero) json.append(",");
                primero = false;

                json.append("{");
                json.append("\"id_consulta\":").append(c.getId_consulta()).append(",");
                json.append("\"id_cliente\":").append(c.getId_cliente()).append(",");
                json.append("\"asunto\":\"").append(c.getAsunto()).append("\",");
                json.append("\"motivo\":\"").append(c.getMotivo()).append("\"");
                json.append("}");
            }

            json.append("]");
            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Error al obtener consultas\"}");
        } finally {
            out.flush();
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }
}
