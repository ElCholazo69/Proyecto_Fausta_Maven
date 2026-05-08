package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import capaDatos.clsDatos;
import capaEntidad.Consulta;

@WebServlet(name = "consulta", urlPatterns = {"/consulta"})
public class consulta extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id_cliente = request.getParameter("id_cliente");
        String asunto = request.getParameter("asunto");
        String motivo = request.getParameter("motivo");

        if (id_cliente == null || asunto == null || motivo == null ||
            id_cliente.isEmpty() || asunto.isEmpty() || motivo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Faltan datos\"}");
            return;
        }

        clsDatos datos = new clsDatos();
        Consulta objCons = new Consulta();
        objCons.setId_cliente(Integer.parseInt(id_cliente));
        objCons.setAsunto(asunto);
        objCons.setMotivo(motivo);
        
        try {
            datos.InsertarConsulta(objCons);

            response.setContentType("application/json");
            response.getWriter().write("{\"mensaje\":\"Consulta registrada correctamente\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error en el servidor\"}");
        }
    }
}