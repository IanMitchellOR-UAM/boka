package mx.uam.azc.estelares.bocanika.controller;

import mx.uam.azc.estelares.bocanika.data.AdminProductoDTO;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet implementation class AdminProductoServlet
 */
@WebServlet(name = "ProductoUpdate", urlPatterns = { "/ProductoUpdate" })
public class AdminProductoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AdminProductoServlet() {
        super();
    }

    /**
     * Maneja las solicitudes POST (Añadir, Modificar, Borrar).
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("AdminProductoServlet: manejando acción de producto...");

        // Obtener la acción enviada desde el formulario (Agregar, Modificar, Borrar)
        String action = request.getParameter("action");

        // Procesar según la acción
        try {
            switch (action) {
                case "Agregar":
                    agregarProducto(request);
                    break;
                case "Modificar":
                    modificarProducto(request);
                    break;
                case "Borrar":
                    borrarProducto(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // Redirigir de vuelta a la página de administración de productos
        String base = request.getContextPath();
        response.sendRedirect(base + "/admin_productos.jsp");
    }

    /**
     * Método para añadir un nuevo producto usando el AdminProductoDTO.
     */
    private void agregarProducto(HttpServletRequest request) throws NamingException, SQLException {
        AdminProductoDTO producto = getProductoFromRequest(request);

        // Obtener la conexión a la base de datos
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO producto (nombre_producto, descripcion_producto, precio_producto, tipo_producto) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, producto.getNombreProducto());
            statement.setString(2, producto.getDescripcionProducto());
            statement.setDouble(3, producto.getPrecioProducto());
            statement.setInt(4, producto.getTipoProducto());

            statement.executeUpdate();

            // Obtener el ID del producto insertado
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idProducto = generatedKeys.getInt(1);
                agregarIngredientes(request, connection, idProducto);
            }
        }
    }

    /**
     * Método para agregar los ingredientes seleccionados al producto en la tabla intermedia producto_ingrediente.
     */
    private void agregarIngredientes(HttpServletRequest request, Connection connection, int idProducto) throws SQLException {
        String[] ingredientesSeleccionados = request.getParameterValues("ingredientes");

        if (ingredientesSeleccionados != null) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO producto_ingrediente (id_producto, id_ingrediente) VALUES (?, ?)")) {
                for (String idIngrediente : ingredientesSeleccionados) {
                    statement.setInt(1, idProducto);
                    statement.setInt(2, Integer.parseInt(idIngrediente));
                    statement.addBatch();  // Añadir a batch para ejecutar todos juntos
                }
                statement.executeBatch();  // Ejecutar el batch de inserciones
            }
        }
    }

    /**
     * Método para modificar un producto existente usando AdminProductoDTO.
     */
    private void modificarProducto(HttpServletRequest request) throws NamingException, SQLException {
        AdminProductoDTO producto = getProductoFromRequest(request);

        // Obtener la conexión a la base de datos
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE producto SET nombre_producto = ?, descripcion_producto = ?, precio_producto = ?, tipo_producto = ? WHERE id_producto = ?")) {

            statement.setString(1, producto.getNombreProducto());
            statement.setString(2, producto.getDescripcionProducto());
            statement.setDouble(3, producto.getPrecioProducto());
            statement.setInt(4, producto.getTipoProducto());
            statement.setInt(5, producto.getIdProducto());

            statement.executeUpdate();

            // Actualizar los ingredientes
            borrarIngredientes(connection, producto.getIdProducto());  // Borrar los ingredientes actuales
            agregarIngredientes(request, connection, producto.getIdProducto());  // Insertar los nuevos ingredientes
        }
    }

    /**
     * Método para borrar un producto existente.
     */
    private void borrarProducto(HttpServletRequest request) throws NamingException, SQLException {
        int idProducto = Integer.parseInt(request.getParameter("id_producto"));

        // Obtener la conexión a la base de datos
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM producto WHERE id_producto = ?")) {

            statement.setInt(1, idProducto);

            // Borrar primero los ingredientes relacionados en producto_ingrediente
            borrarIngredientes(connection, idProducto);
            statement.executeUpdate();
        }
    }

    /**
     * Método para borrar los ingredientes asociados a un producto en la tabla intermedia producto_ingrediente.
     */
    private void borrarIngredientes(Connection connection, int idProducto) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM producto_ingrediente WHERE id_producto = ?")) {
            statement.setInt(1, idProducto);
            statement.executeUpdate();
        }
    }

    /**
     * Método para extraer la información del producto de la solicitud (request) y crear un AdminProductoDTO.
     */
    private AdminProductoDTO getProductoFromRequest(HttpServletRequest request) {
        AdminProductoDTO producto = new AdminProductoDTO();

        // Establecer los valores del producto a partir de la solicitud
        String idProducto = request.getParameter("id_producto");
        if (idProducto != null && !idProducto.isEmpty()) {
            try {
                producto.setIdProducto(Integer.parseInt(idProducto));
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir id_producto: " + e.getMessage());
            }
        }

        producto.setNombreProducto(request.getParameter("nombre_producto"));
        producto.setDescripcionProducto(request.getParameter("descripcion_producto"));

        // Manejar el precio
        String precioProducto = request.getParameter("precio_producto");
        if (precioProducto != null && !precioProducto.isEmpty()) {
            try {
                producto.setPrecioProducto(Double.parseDouble(precioProducto));
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir precio_producto: " + e.getMessage());
            }
        }

        // Manejar tipo de producto
        String tipoProducto = request.getParameter("tipo_producto");
        if (tipoProducto != null && !tipoProducto.isEmpty()) {
            try {
                producto.setTipoProducto(Integer.parseInt(tipoProducto));
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir tipo_producto: " + e.getMessage());
            }
        }

        return producto;
    }

}