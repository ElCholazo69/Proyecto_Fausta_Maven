package servlets;

import capaDatos.ConexionBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import capaEntidad.Descuento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "hacerPedido", urlPatterns = {"/hacerPedido"})
public class hacerPedido extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        ConexionBD conexion = new ConexionBD();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Obtener los parámetros del fetch
            int idCliente = Integer.parseInt(request.getParameter("idcliente"));
            String idEmpleadoStr = request.getParameter("idEmpleado");
            Integer idEmpleado = (idEmpleadoStr != null && !idEmpleadoStr.isEmpty())
                    ? Integer.parseInt(idEmpleadoStr)
                    : null;
            String estado = request.getParameter("estado");
            String metodoEnvio = request.getParameter("metodoEnvio");
            String direccion = request.getParameter("direccion");
            String codigoDescuento = request.getParameter("codigoDescuento"); //Opcional (puede ser null)

            // Fecha automatica del pedido
            java.sql.Date sqlFecha = new java.sql.Date(System.currentTimeMillis());

            // Manejar dirección: null si metodoEnvio == "tienda"
            if ("tienda".equals(metodoEnvio)) {
                direccion = null;
            }

            // Validaciones (direccion puede ser null)
            if (idCliente == 0 || estado == null || metodoEnvio == null) {
                out.print("{\"mensaje\":\"Error: Datos incompletos\"}");
                return;
            }

            conn = conexion.Conectar();
            conn.setAutoCommit(false);

            Descuento descuento = null;
            if (codigoDescuento != null && !codigoDescuento.isEmpty()) {
                String sqlBuscarDescuento = "SELECT id_descuento, codigo, descuento FROM descuento WHERE codigo = ?";
                ps = conn.prepareStatement(sqlBuscarDescuento);
                ps.setString(1, codigoDescuento);
                rs = ps.executeQuery();
                if (rs.next()) {
                    descuento = new Descuento(
                            rs.getInt("id_descuento"),
                            rs.getString("codigo"),
                            rs.getFloat("descuento")
                    );
                }
                rs.close();
                ps.close();
            }
            
            String sqlPedido = "INSERT INTO pedido (id_cliente, id_empleado, fecha, estado, metodo_envio, direccion, id_descuento) VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idCliente);
            
            if (idEmpleado != null) {
                ps.setInt(2, idEmpleado);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setDate(3, sqlFecha);
            ps.setString(4, estado);
            ps.setString(5, metodoEnvio);
            if (direccion != null) {
                ps.setString(6, direccion);
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }
            if (descuento != null){
                ps.setInt(7, descuento.getId_descuento());
            }
              else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas == 0) {
                conn.rollback();
                out.print("{\"mensaje\":\"Error: No se pudo registrar el pedido\"}");
                return;
            }

            // Obtener id_pedido generado
            rs = ps.getGeneratedKeys();
            int idPedido = 0;
            if (rs.next()) {
                idPedido = rs.getInt(1);
            }
            rs.close();
            ps.close();

            // Procesar productos
            double subtotalTotal = 0;
            int totalProductos = Integer.parseInt(request.getParameter("totalProductos"));
            for (int i = 0; i < totalProductos; i++) {
                int idProducto = Integer.parseInt(request.getParameter("producto" + i + "Id"));
                int precioIndex = Integer.parseInt(request.getParameter("producto" + i + "PrecioIndex"));
                int cantidad = Integer.parseInt(request.getParameter("producto" + i + "Cantidad"));
                //Leer el tamano enviado desde el frontend
                String tamano = request.getParameter("producto" + i + "Tamano");

                //Validación del stock antes de vender
                String sqlCheckStock = "SELECT nombre,stock FROM producto WHERE id_producto = ?";
                ps = conn.prepareStatement(sqlCheckStock);
                ps.setInt(1, idProducto);
                rs = ps.executeQuery();

                if (!rs.next()) {
                    conn.rollback();
                    out.print("{\"mensaje\":\"Error: Producto no encontrado\"}");
                    return;
                }
                
                String nombre = rs.getString("nombre");
                int stockActual = rs.getInt("stock");
                rs.close();
                ps.close();

                if (stockActual < cantidad) {
                    conn.rollback();
                    out.print("{\"mensaje\":\"No hay stock suficiente para: " + nombre + "\"}");
                    return;
                }
                // Obtener precios de producto
                String sqlProducto = "SELECT precio1, precio2, precio3 FROM producto WHERE id_producto = ?";
                ps = conn.prepareStatement(sqlProducto);
                ps.setInt(1, idProducto);
                rs = ps.executeQuery();
                if (rs.next()) {
                    double precio1 = rs.getDouble("precio1");
                    double precio2 = rs.getDouble("precio2");
                    double precio3 = rs.getDouble("precio3");

                    double precio = 0;

                    // 1) Si viene precioIndex desde el frontend, lo respetamos SI el precio es válido (>0)
                    if (precioIndex == 0 && precio1 > 0) {
                        precio = precio1;
                    } else if (precioIndex == 1 && precio2 > 0) {
                        precio = precio2;
                    } else if (precioIndex == 2 && precio3 > 0) {
                        precio = precio3;
                    } 
                    double subtotal = precio * cantidad;
                    subtotalTotal+= subtotal;

                    // Insertar en DetallePedido (ahora con tamano)
                    String sqlDetalle = "INSERT INTO detallepedido (id_pedido, id_producto, cantidad, subtotal, tamano) VALUES (?, ?, ?, ?, ?)";
                    ps = conn.prepareStatement(sqlDetalle);
                    ps.setInt(1, idPedido);
                    ps.setInt(2, idProducto);
                    ps.setInt(3, cantidad);
                    ps.setDouble(4, subtotal);
                    if (tamano != null) {
                        ps.setString(5, tamano);
                    } else {
                        ps.setNull(5, java.sql.Types.VARCHAR);
                    }
                    ps.executeUpdate();
                    ps.close();
                    
                    String sqlRestarStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
                        ps = conn.prepareStatement(sqlRestarStock);
                        ps.setInt(1, cantidad);
                        ps.setInt(2, idProducto);
                        ps.executeUpdate();
                        ps.close();

                    } else {
                        conn.rollback();
                        out.print("{\"mensaje\":\"Error: Producto con ID '" + idProducto + "' no encontrado\"}");
                        return;
                    }
                }
                //Aplicar descuento
                double descuentoAplicado = (descuento != null) ? subtotalTotal * descuento.getDescuento() : 0;
                double subtotalConDescuento = subtotalTotal - descuentoAplicado;
                double igv = subtotalConDescuento * 0.18;
                double total = subtotalConDescuento + igv;

                conn.commit();
                out.print("{\"mensaje\":\"Pedido creado con éxito\","
                    + "\"idPedido\":" + idPedido + ","
                    + "\"subtotal\":" + subtotalTotal + ","
                    + "\"descuento\":" + descuentoAplicado + ","
                    + "\"subtotalConDescuento\":" + subtotalConDescuento + ","
                    + "\"igv\":" + igv + ","
                    + "\"total\":" + total
                    + "}");

            } catch (Exception e) {
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
                e.printStackTrace();
                out.print("{\"mensaje\":\"Error interno: " + e.getMessage() + "\"}");
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException e) {}
                if (ps != null) try { ps.close(); } catch (SQLException e) {}
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conexion.Desconectar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            out.flush();
        }
}