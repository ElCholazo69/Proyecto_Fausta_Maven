package servlets;

import capaEntidad.Pedido;
import capaEntidad.Detallepedido;
import capaDatos.PedidoDao;
import capaEntidad.Descuento;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "GenerarBoleta", urlPatterns = {"/GenerarBoleta"})
public class GenerarBoleta extends HttpServlet {

    private PdfPCell createRightCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell createRightCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idPedidoStr = request.getParameter("idPedido");
        if (idPedidoStr == null || idPedidoStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro idPedido faltante o inválido");
            return;
        }

        int idPedido;
        try {
            idPedido = Integer.parseInt(idPedidoStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "idPedido debe ser un número válido");
            return;
        }

        PedidoDao pedidoDao = new PedidoDao();
        Pedido pedido = pedidoDao.obtenerPedidoPorId(idPedido);

        if (pedido == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado");
            return;
        }

        // Calcular total sumando importes
        double subtotal = 0.0;
        for (Detallepedido d : pedido.getDetalles()) {
            subtotal += d.getSubtotal();
        }
        
        // Calcular descuento
        double descuento = 0;
        Descuento dcto = pedido.getDescuento();
        if (dcto != null) {
            descuento = subtotal * dcto.getDescuento();
        }
        
        // Calcular IGV sobre subtotal descontado
        double igv = subtotal * 0.18;

        // Calcular total final
        double total = subtotal - descuento + igv;

        // Configurar PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=boleta_N°" + idPedido +"-"+ pedido.getNombreCliente()+".pdf");

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Contenedor principal (estilo card)
            PdfPTable mainTable = new PdfPTable(1);
            mainTable.setWidthPercentage(90);

            PdfPCell mainCell = new PdfPCell();
            mainCell.setBorder(PdfPCell.BOX);
            mainCell.setBorderColor(BaseColor.ORANGE);
            mainCell.setBorderWidth(2);
            mainCell.setPadding(20);
            mainCell.setBackgroundColor(new BaseColor(250, 250, 250));

            // Encabezado
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 1});

            try {
                Image logo = Image.getInstance(getServletContext().getRealPath("/img/logoEmpresa.jpg"));
                logo.scaleToFit(80, 80);

                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(PdfPCell.NO_BORDER);
                headerTable.addCell(logoCell);

            } catch (Exception e) {
                PdfPCell empty = new PdfPCell(new Paragraph(""));
                empty.setBorder(PdfPCell.NO_BORDER);
                headerTable.addCell(empty);
            }

            PdfPCell boletaCell = new PdfPCell();
            boletaCell.setBorder(PdfPCell.NO_BORDER);
            boletaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph boletaTitle = new Paragraph("BOLETA", boldFont);
            boletaTitle.setAlignment(Element.ALIGN_RIGHT);

            Paragraph boletaNum = new Paragraph("N° " + pedido.getId_pedido());
            boletaNum.setAlignment(Element.ALIGN_RIGHT);

            boletaCell.addElement(boletaTitle);
            boletaCell.addElement(boletaNum);

            headerTable.addCell(boletaCell);
            mainCell.addElement(headerTable);

            // Datos empresa
            Font empresaFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            mainCell.addElement(new Paragraph("Fausta Pastelería Peruana", empresaFont));
            mainCell.addElement(new Paragraph("RUC: 20604408165"));
            mainCell.addElement(new Paragraph("Cliente: " + pedido.getNombreCliente()));
            mainCell.addElement(new Paragraph("Teléfono empresa: +51 989 496 359"));
            mainCell.addElement(new Paragraph("Dirección empresa: Jr. Pedro Conde 472, Lince"));

            // Fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormat = sdf.format(pedido.getFecha());

            Paragraph fechaP = new Paragraph("Fecha de emisión: " + fechaFormat);
            fechaP.setAlignment(Element.ALIGN_RIGHT);
            mainCell.addElement(fechaP);

            // Espacio
            mainCell.addElement(new Paragraph(" "));

            // Tabla de detalles
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell("Producto");
            table.addCell("Tamaño");
            table.addCell("Cantidad");
            table.addCell("Precio Unit.");
            table.addCell("Importe");

            for (Detallepedido d : pedido.getDetalles()) {
                table.addCell(d.getNombre());
                table.addCell(d.getTamano() != null ? d.getTamano() : "-");
                table.addCell(String.valueOf(d.getCantidad()));

                double precioUnit = d.getCantidad() > 0 ? d.getSubtotal() / d.getCantidad() : 0;
                table.addCell("S/ " + String.format("%.2f", precioUnit));
                table.addCell("S/ " + String.format("%.2f", d.getSubtotal()));
            }

            mainCell.addElement(table);

            // Espacio
            mainCell.addElement(new Paragraph(" "));

            PdfPTable totalesTable = new PdfPTable(2);
            totalesTable.setWidthPercentage(40);
            totalesTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalesTable.setWidths(new float[]{1, 1});

            // SUBTOTAL
            totalesTable.addCell(createRightCell("Subtotal:"));
            totalesTable.addCell(createRightCell("S/ " + String.format("%.2f", subtotal)));

            // IGV
            totalesTable.addCell(createRightCell("IGV (18%):"));
            totalesTable.addCell(createRightCell("S/ " + String.format("%.2f", igv)));
            
            //Descuento
            totalesTable.addCell(createRightCell("Descuento:"));
            totalesTable.addCell(createRightCell("S/ " + String.format("%.2f", descuento)));

            // Línea separadora
            PdfPCell lineCell = new PdfPCell(new Paragraph("------------------------------------"));
            lineCell.setBorder(PdfPCell.NO_BORDER);
            lineCell.setColspan(2);
            lineCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalesTable.addCell(lineCell);

            // TOTAL
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            totalesTable.addCell(createRightCell("TOTAL:", totalFont));
            totalesTable.addCell(createRightCell("S/ " + String.format("%.2f", total), totalFont));

            mainCell.addElement(totalesTable);

            // Agregar celda principal
            mainTable.addCell(mainCell);
            document.add(mainTable);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando PDF");
        }
    }
}