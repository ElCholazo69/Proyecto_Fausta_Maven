package servlets;

import capaDatos.ConexionBD;
import java.sql.*;
import capaEntidad.Descuento;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "ListarPedidos", urlPatterns = {"/ListarPedidos"})
public class ListarPedidos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("idCliente");

        if (idParam == null) {
            out.print("{\"mensaje\":\"Error: falta parámetro idCliente\"}");
            return;
        }

        int idCliente;
        try {
            idCliente = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            out.print("{\"mensaje\":\"Error: idCliente inválido\"}");
            return;
        }

        ConexionBD conexion = new ConexionBD();
        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psDetalle = null;
        ResultSet rsPedido = null;
        ResultSet rsDetalle = null;

        try {
            conn = conexion.Conectar();

            String sqlPedido = "SELECT id_pedido, fecha, estado, metodo_envio, direccion, id_descuento " +
                               "FROM pedido WHERE id_cliente = ? ORDER BY fecha ASC";
            psPedido = conn.prepareStatement(sqlPedido);
            psPedido.setInt(1, idCliente);
            rsPedido = psPedido.executeQuery();

            StringBuilder json = new StringBuilder();
            json.append("[");
            
            boolean primerPedido = true;

            while (rsPedido.next()) {
                if (!primerPedido) json.append(",");
                double subtotalTotal =0;
                primerPedido = false;

                int idPedido = rsPedido.getInt("id_pedido");
                Date fecha = rsPedido.getDate("fecha");
                String estado = rsPedido.getString("estado");
                String metodo = rsPedido.getString("metodo_envio");
                String direccion = rsPedido.getString("direccion");
                Integer idDescuento = rsPedido.getObject("id_descuento") != null ? rsPedido.getInt("id_descuento") : null;

                json.append("{");
                json.append("\"id_pedido\":").append(idPedido).append(",");
                json.append("\"fecha\":\"").append(fecha).append("\",");
                json.append("\"estado\":\"").append(estado).append("\",");
                json.append("\"metodo_envio\":\"").append(metodo).append("\",");
                json.append("\"direccion\":").append(direccion != null ? "\"" + direccion + "\"," : "null,");

                // Detalle del pedido
                String sqlDetalle =
                    "SELECT d.cantidad, d.subtotal, d.tamano, " +
                    "p.nombre, p.imagen, p.precio1, p.precio2, p.precio3 " +
                    "FROM detallepedido d " +
                    "JOIN producto p ON d.id_producto = p.id_producto " +
                    "WHERE d.id_pedido = ?";
                psDetalle = conn.prepareStatement(sqlDetalle);
                psDetalle.setInt(1, idPedido);
                rsDetalle = psDetalle.executeQuery();

                json.append("\"detalles\":[");
                
                boolean primerDetalle = true;
                while (rsDetalle.next()) {
                    if (!primerDetalle) json.append(",");
                    primerDetalle = false;
                    int cantidad = rsDetalle.getInt("cantidad");
                    double subtotal = rsDetalle.getDouble("subtotal");
                    subtotalTotal += subtotal;

                    json.append("{");
                    json.append("\"producto\":\"").append(rsDetalle.getString("nombre")).append("\",");
                    json.append("\"imagen\":\"").append(rsDetalle.getString("imagen")).append("\",");
                    json.append("\"cantidad\":").append(cantidad).append(",");
                    json.append("\"subtotal\":").append(subtotal).append(",");
                    json.append("\"precio1\":").append(rsDetalle.getDouble("precio1")).append(",");
                    json.append("\"precio2\":").append(rsDetalle.getDouble("precio2")).append(",");
                    json.append("\"precio3\":").append(rsDetalle.getDouble("precio3")).append(",");
                    String tam = rsDetalle.getString("tamano");
                    json.append("\"tamano\":").append(tam != null ? "\"" + tam + "\"" : "null");
                    json.append("}");
                }
                json.append("]"); // detalles

                // Calcular descuento aplicado
                double descuentoAplicado = 0;
                if (idDescuento != null) {
                    String sqlDesc = "SELECT descuento FROM descuento WHERE id_descuento = ?";
                    PreparedStatement psDesc = conn.prepareStatement(sqlDesc);
                    psDesc.setInt(1, idDescuento);
                    ResultSet rsDesc = psDesc.executeQuery();
                    if (rsDesc.next()) {
                        double porcentaje = rsDesc.getDouble("descuento"); // decimal 0.10
                        descuentoAplicado = subtotalTotal * porcentaje;
                    }
                    rsDesc.close();
                    psDesc.close();
                }

                json.append(",\"descuento\":").append(descuentoAplicado);

                json.append("}"); // pedido
            }

            json.append("]"); // lista
            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"mensaje\":\"Error interno: " + e.getMessage() + "\"}");
        } finally {
            try { if (rsDetalle != null) rsDetalle.close(); } catch (Exception e) {}
            try { if (psDetalle != null) psDetalle.close(); } catch (Exception e) {}
            try { if (rsPedido != null) rsPedido.close(); } catch (Exception e) {}
            try { if (psPedido != null) psPedido.close(); } catch (Exception e) {}
            try { if (conn != null) conexion.Desconectar(); } catch (Exception e) {}
        }

        out.flush();
    }
}
