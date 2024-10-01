package mx.uam.azc.estelares.bocanika.controller;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.uam.azc.estelares.bocanika.data.AdminProductoDTO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExportToPDFServlet", urlPatterns = {"/ExportToPDFServlet"})
public class ExportToPDFServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("busqueda_tipo");

        try {
            List<AdminProductoDTO> productos = getProductos(searchTerm);

            // Generar PDF
            generatePDF(productos, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<AdminProductoDTO> getProductos(String searchTerm) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = dataSource.getConnection();

        List<AdminProductoDTO> productos = new ArrayList<>();

        // Modifica este query si es necesario agregar un filtro por "searchTerm"
        String query = "SELECT p.id_producto, p.nombre_producto, p.descripcion_producto, p.precio_producto, t.id_tipo " +
                "FROM producto p " +
                "INNER JOIN tipo_producto t ON p.tipo_producto = t.id_tipo";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AdminProductoDTO producto = new AdminProductoDTO();
                    producto.setIdProducto(resultSet.getInt("id_producto"));
                    producto.setNombreProducto(resultSet.getString("nombre_producto"));
                    producto.setDescripcionProducto(resultSet.getString("descripcion_producto"));
                    producto.setPrecioProducto(resultSet.getDouble("precio_producto"));
                    producto.setTipoProducto(resultSet.getInt("id_tipo"));
                    productos.add(producto);
                }
            }
        } finally {
            connection.close();
        }

        return productos;
    }

    private void generatePDF(List<AdminProductoDTO> productos, HttpServletResponse response) throws DocumentException, IOException {
        // Configurar el archivo PDF
        Document document = new Document();
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment;filename=Productos.pdf");

        // Crear el flujo de salida
        OutputStream out = response.getOutputStream();

        PdfWriter.getInstance(document, out);

        // Abrir el documento
        document.open();

        // Título
        document.add(new Paragraph("Reporte de Productos"));

        // Crear una tabla con 5 columnas (ID, Nombre, Descripción, Precio, Tipo)
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        // Encabezados
        PdfPCell cell = new PdfPCell(new Paragraph("ID Producto"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Nombre Producto"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Descripción Producto"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Precio Producto"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Tipo Producto"));
        table.addCell(cell);

        // Agregar los datos de los productos
        for (AdminProductoDTO producto : productos) {
            table.addCell(String.valueOf(producto.getIdProducto()));
            table.addCell(producto.getNombreProducto());
            table.addCell(producto.getDescripcionProducto());
            table.addCell(String.valueOf(producto.getPrecioProducto()));
            table.addCell(String.valueOf(producto.getTipoProducto()));
        }

        // Agregar la tabla al documento PDF
        document.add(table);

        // Cerrar el documento
        document.close();

        // Cerrar el flujo de salida
        out.flush();
        out.close();
    }
}
