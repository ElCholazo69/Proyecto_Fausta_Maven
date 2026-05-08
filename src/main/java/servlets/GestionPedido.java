package servlets;

import capaDatos.PedidoDao;
import capaEntidad.Pedido;
import capaEntidad.Detallepedido;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "GestionPedido", urlPatterns = {"/GestionPedido"})
public class GestionPedido extends HttpServlet {

    private PedidoDao pedidoDao = new PedidoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Pedido> lista = pedidoDao.listarPedidosASC();

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.print("[");
        for (int i = 0; i < lista.size(); i++) {
            Pedido p = lista.get(i);
            out.print("{");
            out.print("\"id_pedido\":" + p.getId_pedido() + ",");
            out.print("\"nombreCliente\":\"" + p.getNombreCliente() + "\",");
            out.print("\"fecha\":\"" + p.getFecha() + "\",");
            out.print("\"estado\":\"" + p.getEstado() + "\",");
            out.print("\"metodoEnvio\":\"" + p.getMetodoEnvio() + "\",");
            out.print("\"direccion\":\"" + (p.getDireccion() != null ? p.getDireccion() : "") + "\",");
            
            // detalles
            out.print("\"detalles\":[");
            List<Detallepedido> detalles = p.getDetalles();
            for (int j = 0; j < detalles.size(); j++) {
                Detallepedido det = detalles.get(j);
                out.print("{");
                out.print("\"nombre\":\"" + det.getNombre() + "\",");
                out.print("\"imagen\":\"" + det.getImagen() + "\",");
                out.print("\"cantidad\":" + det.getCantidad() + ",");
                out.print("\"subtotal\":" + det.getSubtotal() + ",");
                out.print("\"tamano\":\"" + (det.getTamano() != null ? det.getTamano() : "") + "\"");
                out.print("}");
                if (j < detalles.size() - 1) out.print(",");
            }
            out.print("]");

            out.print("}");
            if (i < lista.size() - 1) out.print(",");
        }
        out.print("]");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String accion = request.getParameter("accion");

        try {

            if ("actualizarEstado".equals(accion)) {

                int idPedido = Integer.parseInt(request.getParameter("id_pedido"));
                String nuevoEstado = request.getParameter("estado");

                boolean exito = pedidoDao.actualizarEstado(idPedido, nuevoEstado);

                out.print("{\"exito\":" + exito + "}");

            } else if ("eliminar".equals(accion)) {

                int idPedido = Integer.parseInt(request.getParameter("id_pedido"));
                boolean exito = pedidoDao.eliminarPedido(idPedido);

                out.print("{\"exito\":" + exito + "}");

            } else {

                out.print("{\"exito\":false,\"mensaje\":\"Acción no reconocida\"}");

            }

        } catch (Exception e) {

            e.printStackTrace();
            out.print("{\"exito\":false,\"mensaje\":\"Error interno en el servidor\"}");

        }

        out.flush();
    }
}
