package mx.uam.azc.estelares.bocanika.controller;

import mx.uam.azc.estelares.bocanika.data.AdminTipoProductoDTO;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet implementation class AdminTipoProductoServlet
 */
@WebServlet(name = "TipoProductoUpdate", urlPatterns = { "/TipoProductoUpdate" })
public class AdminTipoProductoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AdminTipoProductoServlet() {
        super();
    }

    /**
     * Maneja las solicitudes POST (Añadir, Modificar, Borrar).
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("AdminTipoProductoServlet: manejando acción de tipo de producto...");

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "Agregar":
                    agregarTipoProducto(request);
                    break;
                case "Modificar":
                    modificarTipoProducto(request);
                    break;
                case "Borrar":
                    borrarTipoProducto(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        String base = request.getContextPath();
        response.sendRedirect(base + "/admin_tipo_producto.jsp");
    }

    /**
     * Método para añadir un nuevo tipo de producto usando AdminTipoProductoDTO.
     */
    private void agregarTipoProducto(HttpServletRequest request) throws NamingException, SQLException {
        AdminTipoProductoDTO tipoProducto = getTipoProductoFromRequest(request);

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO tipo_producto (nombre_tipo) VALUES (?)")) {

            statement.setString(1, tipoProducto.getNombreTipoProducto());
            statement.executeUpdate();
        }
    }

    /**
     * Método para modificar un tipo de producto existente usando AdminTipoProductoDTO.
     */
    private void modificarTipoProducto(HttpServletRequest request) throws NamingException, SQLException {
        AdminTipoProductoDTO tipoProducto = getTipoProductoFromRequest(request);

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE tipo_producto SET nombre_tipo = ? WHERE id_tipo = ?")) {

            statement.setString(1, tipoProducto.getNombreTipoProducto());
            statement.setInt(2, tipoProducto.getIdTipoProducto());
            statement.executeUpdate();
        }
    }

    /**
     * Método para borrar un tipo de producto existente.
     */
    private void borrarTipoProducto(HttpServletRequest request) throws NamingException, SQLException {
        int idTipoProducto = Integer.parseInt(request.getParameter("id_tipo"));

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM tipo_producto WHERE id_tipo = ?")) {

            statement.setInt(1, idTipoProducto);
            statement.executeUpdate();
        }
    }

    /**
     * Método para extraer la información del tipo de producto de la solicitud (request) y crear un AdminTipoProductoDTO.
     */
    private AdminTipoProductoDTO getTipoProductoFromRequest(HttpServletRequest request) {
        AdminTipoProductoDTO tipoProducto = new AdminTipoProductoDTO();

        String idTipo = request.getParameter("id_tipo");
        if (idTipo != null && !idTipo.isEmpty()) {
            tipoProducto.setIdTipoProducto(Integer.parseInt(idTipo));
        }

        tipoProducto.setNombreTipoProducto(request.getParameter("nombre_tipo"));
        return tipoProducto;
    }
}
