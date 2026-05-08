package capaDatos;

import capaEntidad.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    private Connection con;
    
    public ProductoDao(){
        ConexionBD conexion = new ConexionBD();
        con = conexion.Conectar();
    }
    
        //Mostrar todos los productos
        public List<Producto> listar(){
            List<Producto> lista = new ArrayList<>();
            String sql = "SELECT * FROM producto";

            try(PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Producto p = new Producto();
                    p.setId_producto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio1(rs.getFloat("precio1"));
                    p.setPrecio2(rs.getFloat("precio2"));
                    p.setPrecio3(rs.getFloat("precio3"));
                    p.setStock(rs.getInt("stock"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setImagen(rs.getString("imagen"));
                    lista.add(p);
                }
            } catch(Exception e){
                    System.out.println("ERROR al listar productos: "+e.getMessage());
                    }
            return lista;
            }
        
        //Insertar un producto
        public void insertar(Producto p){
            String sql = "INSERT INTO producto (nombre, descripcion, precio1, precio2, precio3, stock, categoria, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDescripcion());
                ps.setFloat(3, p.getPrecio1());
                ps.setFloat(4, p.getPrecio2());
                ps.setFloat(5, p.getPrecio3());
                ps.setInt(6, p.getStock());
                ps.setString(7, p.getCategoria());
                ps.setString(8, p.getImagen());
                ps.executeUpdate();
            } catch(Exception e){
                System.out.println("Error al insertar producto: "+e.getMessage());
            }
        }
        
        //Actualizar un producto
        public void actualizar(Producto p){
            String sql = "UPDATE producto SET nombre=?, descripcion=?, precio1=?, precio2=?, precio3=?, stock=?, categoria=?, imagen=? WHERE id_producto=?";
            try(PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1,p.getNombre());
                ps.setString(2,p.getDescripcion());
                ps.setFloat(3, p.getPrecio1());
                ps.setFloat(4, p.getPrecio2());
                ps.setFloat(5, p.getPrecio3());
                ps.setInt(6,p.getStock());
                ps.setString(7,p.getCategoria());
                ps.setString(8,p.getImagen());
                ps.setInt(9,p.getId_producto());
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("ERROR al actualizar producto: "+e.getMessage());
            }
        }
        
        //Eliminar producto
        public void eliminar(int id){
            String sql="DELETE FROM producto WHERE id_producto=?";
            try(PreparedStatement ps = con.prepareStatement(sql)){
                ps.setInt(1, id);
                ps.executeUpdate();
            }catch(Exception e){
                System.out.println("ERROR al eliminar el producto: "+e.getMessage());
            }
        }
        
        public Producto obtenerId(int id){
            Producto p = null;
            String sql="SELECT * FROM producto WHERE id_producto=?";
            
            try(PreparedStatement ps = con.prepareStatement(sql)){
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    p = new Producto();
                    p.setId_producto(rs.getInt("id_producto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio1(rs.getFloat("precio1"));
                    p.setPrecio2(rs.getFloat("precio2"));
                    p.setPrecio3(rs.getFloat("precio3"));
                    p.setStock(rs.getInt("stock"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setImagen(rs.getString("imagen"));
                }
            }catch(Exception e){
                System.out.println("Error al obtener el producto: "+e.getMessage());
            }
            return p;
        }
    }

