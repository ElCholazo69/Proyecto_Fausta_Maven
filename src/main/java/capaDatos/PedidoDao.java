package capaDatos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import capaEntidad.Pedido;
import capaEntidad.Detallepedido;
import capaEntidad.Descuento;

public class PedidoDao {
    private Connection con;
    
    public PedidoDao(){
        ConexionBD conexion = new ConexionBD();
        con = conexion.Conectar();
    }
    
    // Listar todos los pedidos con detalles por id DESC
    public List<Pedido> listarPedidosASC() {
        List<Pedido> lista = new ArrayList<>();
        String sqlPedido = "SELECT p.id_pedido, p.id_cliente, p.id_empleado, p.fecha, p.estado, p.metodo_envio, p.direccion, c.nombres, c.apellidos " +
                           "FROM pedido p " +
                           "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                           "ORDER BY p.id_pedido ASC";

        try (PreparedStatement psPedido = con.prepareStatement(sqlPedido);
             ResultSet rsPedido = psPedido.executeQuery()) {

            while (rsPedido.next()) {
                Pedido p = new Pedido();
                p.setId_pedido(rsPedido.getInt("id_pedido"));
                p.setId_cliente(rsPedido.getInt("id_cliente"));
                p.setId_empleado(rsPedido.getInt("id_empleado"));
                p.setFecha(rsPedido.getDate("fecha"));
                p.setEstado(rsPedido.getString("estado"));
                p.setMetodoEnvio(rsPedido.getString("metodo_envio"));
                p.setDireccion(rsPedido.getString("direccion"));
                p.setNombreCliente(rsPedido.getString("nombres") + " " + rsPedido.getString("apellidos"));

                // Cargar detalles del pedido
                String sqlDetalle = "SELECT d.id_detalle, d.id_producto, d.cantidad, d.subtotal, d.tamano, pr.nombre, pr.imagen " +
                                    "FROM detallepedido d " +
                                    "JOIN producto pr ON d.id_producto = pr.id_producto " +
                                    "WHERE d.id_pedido = ?";
                try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                    psDetalle.setInt(1, p.getId_pedido());
                    try (ResultSet rsDetalle = psDetalle.executeQuery()) {
                        List<Detallepedido> detalles = new ArrayList<>();
                        while (rsDetalle.next()) {
                            Detallepedido det = new Detallepedido();
                            det.setId_detalle(rsDetalle.getInt("id_detalle"));
                            det.setId_pedido(p.getId_pedido());
                            det.setId_producto(rsDetalle.getInt("id_producto"));
                            det.setCantidad(rsDetalle.getInt("cantidad"));
                            det.setSubtotal(rsDetalle.getFloat("subtotal"));
                            det.setTamano(rsDetalle.getString("tamano"));
                            det.setNombre(rsDetalle.getString("nombre"));
                            det.setImagen(rsDetalle.getString("imagen"));
                            detalles.add(det);
                        }
                        p.setDetalles(detalles);
                    }
                }

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Actualizar el estado de un pedido y descontar stock si pasa a Finalizado
    public boolean actualizarEstado(int idPedido, String nuevoEstado) {
        try {
        // Obtener estado anterior
        String estadoAnterior = obtenerEstadoActual(idPedido);

        // Actualizar estado
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }

        // Devolver stock si se cancela
        if (
            nuevoEstado.equals("Cancelado") &&
            !estadoAnterior.equals("Cancelado")
        ) {
            devolverStock(idPedido);
        }
        return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar un pedido
    public boolean eliminarPedido(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Metodo para devolver stock cuando se cancela un pedido
    private void devolverStock(int idPedido) throws SQLException {

        String sql = "SELECT id_producto, cantidad FROM detallepedido WHERE id_pedido = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idProducto = rs.getInt("id_producto");
                int cantidad = rs.getInt("cantidad");

                String sqlUpdate =
                    "UPDATE producto SET stock = stock + ? WHERE id_producto = ?";

                try (PreparedStatement ps2 = con.prepareStatement(sqlUpdate)) {

                    ps2.setInt(1, cantidad);
                    ps2.setInt(2, idProducto);

                    ps2.executeUpdate();
                }
            }
        }
    }
    
    //Metodo para obtener el estado actual
    private String obtenerEstadoActual(int idPedido) {
        String sql = "SELECT estado FROM pedido WHERE id_pedido = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("estado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    //Metodo para descontar la cantidad del pedido
    private void descontarStock(int idPedido) throws SQLException {
    // Obtener productos del pedido
    String sql = "SELECT id_producto, cantidad FROM detallepedido WHERE id_pedido = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            // Recorrer los productos del pedido
            while (rs.next()) {
                int idProducto = rs.getInt("id_producto");
                int cantidad = rs.getInt("cantidad");

                // Actualizar stock
                String sqlUpdate = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
                try (PreparedStatement ps2 = con.prepareStatement(sqlUpdate)) {
                    ps2.setInt(1, cantidad);
                    ps2.setInt(2, idProducto);
                    ps2.executeUpdate();
                }
            }
        }
    }
    
    //Obtener un pedido especifico por ID con detalles y descuento
    public Pedido obtenerPedidoPorId(int idPedido) {
        Pedido pedido = null;
        String sqlPedido = "SELECT p.id_pedido, p.id_cliente, p.id_empleado, p.fecha, p.estado, p.metodo_envio, p.direccion, " +
                           "c.nombres, c.apellidos, d.id_descuento, d.codigo, d.descuento AS porcentaje " +
                           "FROM pedido p " +
                           "JOIN cliente c ON p.id_cliente = c.id_cliente " +
                           "LEFT JOIN descuento d ON p.id_descuento = d.id_descuento " +
                           "WHERE p.id_pedido = ?";

        try (PreparedStatement psPedido = con.prepareStatement(sqlPedido)) {
            psPedido.setInt(1, idPedido);
            try (ResultSet rsPedido = psPedido.executeQuery()) {
                if (rsPedido.next()) {
                    pedido = new Pedido();
                    pedido.setId_pedido(rsPedido.getInt("id_pedido"));
                    pedido.setId_cliente(rsPedido.getInt("id_cliente"));
                    pedido.setId_empleado(rsPedido.getInt("id_empleado"));
                    pedido.setFecha(rsPedido.getDate("fecha"));
                    pedido.setEstado(rsPedido.getString("estado"));
                    pedido.setMetodoEnvio(rsPedido.getString("metodo_envio"));
                    pedido.setDireccion(rsPedido.getString("direccion"));
                    pedido.setNombreCliente(rsPedido.getString("nombres") + " " + rsPedido.getString("apellidos"));

                    // Cargar detalles del pedido
                    String sqlDetalle = "SELECT d.id_detalle, d.id_producto, d.cantidad, d.subtotal, d.tamano, pr.nombre, pr.imagen " +
                                        "FROM detallepedido d " +
                                        "JOIN producto pr ON d.id_producto = pr.id_producto " +
                                        "WHERE d.id_pedido = ?";
                    try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                        psDetalle.setInt(1, idPedido);
                        try (ResultSet rsDetalle = psDetalle.executeQuery()) {
                            List<Detallepedido> detalles = new ArrayList<>();
                            while (rsDetalle.next()) {
                                Detallepedido det = new Detallepedido();
                                det.setId_detalle(rsDetalle.getInt("id_detalle"));
                                det.setId_pedido(idPedido);
                                det.setId_producto(rsDetalle.getInt("id_producto"));
                                det.setCantidad(rsDetalle.getInt("cantidad"));
                                det.setSubtotal(rsDetalle.getFloat("subtotal"));
                                det.setTamano(rsDetalle.getString("tamano"));
                                det.setNombre(rsDetalle.getString("nombre"));
                                det.setImagen(rsDetalle.getString("imagen"));
                                detalles.add(det);
                            }
                            pedido.setDetalles(detalles);
                        }
                    }

                    // Cargar descuento si existe
                    int idDescuento = rsPedido.getInt("id_descuento");
                    if (!rsPedido.wasNull()) {
                        Descuento dcto = new Descuento();
                        dcto.setId_descuento(idDescuento);
                        dcto.setCodigo(rsPedido.getString("codigo"));
                        dcto.setDescuento(rsPedido.getFloat("porcentaje"));
                        pedido.setDescuento(dcto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedido;
    }
    
}
