package servlets;

import capaDatos.ProductoDao;
import capaEntidad.Producto;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;        // <-IMPORTE PARA MANEJO DE IMAGEN
import java.nio.file.Path;  // <-IMPORTE PARA MANEJO DE IMAGEN


@WebServlet(name = "productoCRUD", urlPatterns = {"/productoCRUD"})
@MultipartConfig // Manejo de otros archivos(imagenes en este caso)
public class productoCRUD extends HttpServlet {

    ProductoDao dao = new ProductoDao();
    
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain;charset=UTF-8");
        String accion = request.getParameter("accion");
        
        String uploadPath = getServletContext().getRealPath("") + File.separator + "img";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try (PrintWriter out = response.getWriter()) {
            
            switch (accion) {

                case "agregar":
                    Producto nuevo = new Producto();
                    //Se obtiene el valor de los precios
                    String p1 = request.getParameter("precio1");
                    String p2 = request.getParameter("precio2");
                    String p3 = request.getParameter("precio3");
                    //Verificación si los precios estan vacios
                    if (p1 == null || p1.trim().isEmpty()) p1 = "0";
                    if (p2 == null || p2.trim().isEmpty()) p2 = "0";
                    if (p3 == null || p3.trim().isEmpty()) p3 = "0";
                    
                    nuevo.setNombre(request.getParameter("nombre"));
                    nuevo.setDescripcion(request.getParameter("descripcion"));
                    nuevo.setPrecio1(Float.parseFloat(p1));
                    nuevo.setPrecio2(Float.parseFloat(p2));
                    nuevo.setPrecio3(Float.parseFloat(p3));
                    nuevo.setStock(Integer.parseInt(request.getParameter("cantidad")));
                    nuevo.setCategoria(request.getParameter("categoria"));

                    // Subir imagen
                    Part filePart = request.getPart("imagen");
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
                        filePart.write(uploadPath + File.separator + fileName);
                        nuevo.setImagen(fileName);
                    }

                    dao.insertar(nuevo);
                    out.print("Producto agregado correctamente.");
                    break;

                case "eliminar":
                    int idE = Integer.parseInt(request.getParameter("id"));
                    dao.eliminar(idE);
                    out.print("Producto eliminado correctamente.");
                    break;

                case "actualizar":
                    int idActual= Integer.parseInt(request.getParameter("id"));
                    Producto existe = dao.obtenerId(idActual);
                    if(existe !=null){
                       Producto p = new Producto();
                       //Obtención de los datos de precio
                       String p1u = request.getParameter("precio1");
                       String p2u = request.getParameter("precio2");
                       String p3u = request.getParameter("precio3");
                       //Verificación si los datos estan vacios o no, para ponerle cero y evitar errores
                       if(p1u == null || p1u.trim().isEmpty()) p1u = "0";
                       if(p2u == null || p2u.trim().isEmpty()) p2u = "0";
                       if(p3u == null || p3u.trim().isEmpty()) p3u = "0";
                       
                       p.setId_producto(idActual);
                       p.setNombre(request.getParameter("nombre"));
                       p.setDescripcion(request.getParameter("descripcion"));
                       p.setPrecio1(Float.parseFloat(p1u));
                       p.setPrecio2(Float.parseFloat(p2u));
                       p.setPrecio3(Float.parseFloat(p3u));
                       p.setStock(Integer.parseInt(request.getParameter("cantidad")));
                       p.setCategoria(request.getParameter("categoria"));
                    // Si se envió nueva imagen
                    Part newFilePart = request.getPart("imagen");
                        if (newFilePart != null && newFilePart.getSize() > 0) {
                            String newFileName = Path.of(newFilePart.getSubmittedFileName()).getFileName().toString();
                            newFilePart.write(uploadPath + File.separator + newFileName);
                            p.setImagen(newFileName);
                        }else{
                           p.setImagen(existe.getImagen()); 
                        }
                        
                        dao.actualizar(p);
                        out.print("Producto actualizado correctamente.");
                    }else{
                        out.print("Producto no encontrado");
                    }
                    break;

                case "listar":
                    List<Producto> lista = dao.listar();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<table class='table table-striped'><thead>")
                      .append("<tr><th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio 1</th><th>Precio 2</th><th>Precio 3</th><th>Stock</th><th>Categoría</th><th>Imagen</th></tr>")
                      .append("</thead><tbody>");
                    for (Producto prod : lista) {
                        sb.append("<tr>")
                          .append("<td>").append(prod.getId_producto()).append("</td>")
                          .append("<td>").append(prod.getNombre()).append("</td>")
                          .append("<td>").append(prod.getDescripcion()).append("</td>")
                          .append("<td>").append(prod.getPrecio1()).append("</td>")
                          .append("<td>").append(prod.getPrecio2()).append("</td>")
                          .append("<td>").append(prod.getPrecio3()).append("</td>")
                          .append("<td>").append(prod.getStock()).append("</td>")
                          .append("<td>").append(prod.getCategoria()).append("</td>")
                          .append("<td><img src='../img/")
                          .append(prod.getImagen())
                          .append("' width='80'></td>")
                          .append("</tr>");
                    }
                    sb.append("</tbody></table>");
                    out.print(sb.toString());
                    break;
                    
                case "listarJson":
                        List<Producto> productos = dao.listar();
                        response.setContentType("application/json;charset=UTF-8");

                        StringBuilder json = new StringBuilder("[");
                        for (int i = 0; i < productos.size(); i++) {
                            Producto p = productos.get(i);
                            json.append("{")
                                .append("\"id_producto\":").append(p.getId_producto()).append(",")
                                .append("\"nombre\":\"").append(escapeJson(p.getNombre())).append("\",")
                                .append("\"descripcion\":\"").append(escapeJson(p.getDescripcion())).append("\",")
                                .append("\"categoria\":\"").append(escapeJson(p.getCategoria())).append("\",")
                                .append("\"imagen\":\"").append(escapeJson(p.getImagen())).append("\",")

                                // ENVÍA LOS TRES PRECIOS RAW (sin tamaños)
                                .append("\"precio1\":").append(p.getPrecio1()).append(",")
                                .append("\"precio2\":").append(p.getPrecio2()).append(",")
                                .append("\"precio3\":").append(p.getPrecio3()).append(",")
                                .append("\"cantidad\":").append(p.getStock()) //Enviar el stock del producto

                                .append("}");
                            if (i < productos.size() - 1) json.append(",");
                        }

                        json.append("]");
                        out.print(json.toString());
                        break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().print("Error: " + e.getMessage());
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "\\\"").replace("\n", "").replace("\r", "");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
