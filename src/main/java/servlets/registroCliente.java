package servlets;

import capaDatos.ConexionBD;
import capaEntidad.Clientes;
import capaDatos.ClienteDao;
import java.sql.*;
import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet(name = "registroCliente", urlPatterns = {"/registroCliente"})
public class registroCliente extends HttpServlet {
    
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {                
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String contrasena = request.getParameter("contrasena");
        String telefono = request.getParameter("telefono");
        String dni = request.getParameter("dni");
        String ciudad = request.getParameter("id_ciudad");
        

        if (nombres == null || apellidos == null || email == null || contrasena == null || telefono == null || dni == null || ciudad ==null||
            nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || contrasena.isEmpty() || telefono.isEmpty() || dni.isEmpty() || ciudad.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Faltan datos\"}");
            return;
        }

        ClienteDao dao = new ClienteDao();
        Clientes cliente = new Clientes();

        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setEmail(email);
        cliente.setContrasena(contrasena);
        cliente.setTelefono(telefono);
        cliente.setDni(dni);
        cliente.setId_ciudad(Integer.parseInt(ciudad));
        try {
            String resultado = dao.insertarCliente(cliente);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"mensaje\":\"" + resultado + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error en el servidor\"}");
        }
    }  
}
