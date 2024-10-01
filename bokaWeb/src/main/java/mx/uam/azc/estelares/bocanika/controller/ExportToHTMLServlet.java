package mx.uam.azc.estelares.bocanika.controller;

import mx.uam.azc.estelares.bocanika.data.AdminProductoDTO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ExportToHTMLServlet", urlPatterns = {"/ExportToHTMLServlet"})
public class ExportToHTMLServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("busqueda_tipo");

        try {
            List<AdminProductoDTO> productos = getProductos(searchTerm);

            // Generar HTML manualmente
            String htmlContent = generateHTML(productos);

            // Configurar la respuesta HTTP
            response.setContentType("text/html");
            response.addHeader("Content-Disposition", "attachment;filename=Productos.html");
            OutputStream out = response.getOutputStream();

            // Escribir el contenido HTML al flujo de salida
            out.write(htmlContent.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
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

    private String generateHTML(List<AdminProductoDTO> productos) {
        // Crear una cadena HTML manualmente
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Reporte de Productos</title>");
        html.append("<meta charset='UTF-8'>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>Reporte de Productos</h1>");
        html.append("<table border='1' width='100%'>");
        html.append("<tr>");
        html.append("<th>ID Producto</th>");
        html.append("<th>Nombre Producto</th>");
        html.append("<th>Descripción Producto</th>");
        html.append("<th>Precio Producto</th>");
        html.append("<th>Tipo Producto</th>");
        html.append("</tr>");

        // Añadir las filas de la tabla con los productos
        for (AdminProductoDTO producto : productos) {
            html.append("<tr>");
            html.append("<td>").append(producto.getIdProducto()).append("</td>");
            html.append("<td>").append(producto.getNombreProducto()).append("</td>");
            html.append("<td>").append(producto.getDescripcionProducto()).append("</td>");
            html.append("<td>").append(producto.getPrecioProducto()).append("</td>");
            html.append("<td>").append(producto.getTipoProducto()).append("</td>");
            html.append("</tr>");
        }

        html.append("</table>");
        html.append("</body>");
        html.append("</html>");

        // Devolver la cadena HTML generada
        return html.toString();
    }
}
