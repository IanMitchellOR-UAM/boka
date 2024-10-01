package mx.uam.azc.estelares.bocanika.controller;

import mx.uam.azc.estelares.bocanika.data.AdminProductoDTO;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
import java.io.*;
import java.sql.*;
import java.util.*;

@WebServlet(name = "ExportToExcelServlet", urlPatterns = {"/ExportToExcelServlet"})
public class ExportToExcelServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("busqueda_tipo");

        try {
            List<AdminProductoDTO> productos = getProductos(searchTerm);
            Map<String, Object> beans = new HashMap<>();
            beans.put("productos", productos);

            // Exportar los productos a Excel
            xlsExport(beans, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<AdminProductoDTO> getProductos(String searchTerm) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = dataSource.getConnection();

        List<AdminProductoDTO> productos = new ArrayList<>();

        // Eliminar el filtro de búsqueda y obtener todos los productos
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


    private void xlsExport(Map<String, Object> beans, HttpServletResponse response) throws IOException {
        // Configurar el archivo Excel a partir de una plantilla
        ServletContext context = getServletContext();
        InputStream templateStream = context.getResourceAsStream("/WEB-INF/templates/PlantillaProductos.xls");
        
        XLSTransformer transformer = new XLSTransformer();
        HSSFWorkbook workbook = null;
        try {
            workbook = transformer.transformXLS(templateStream, beans);
        } catch (ParsePropertyException e) {
            e.printStackTrace();
        }

        // Configurar la respuesta HTTP para la descarga del archivo Excel
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=Productos.xls");

        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            os.flush();
        }
    }
}
