package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import capaNegocio.clsNegocio;
import capaEntidad.Clientes;

@WebServlet(name = "procesar", urlPatterns = {"/procesar"})
public class procesar extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        clsNegocio ObjNeg = new clsNegocio();
        Clientes cliente = new Clientes();
        
        cliente.setNombres(request.getParameter("txtnombres"));
        cliente.setApellidos(request.getParameter("txtapellidos"));
//        cliente.setTelefono(Integer.parseInt(request.getParameter("txtedad")));
        
        ObjNeg.Insertar(cliente);
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
