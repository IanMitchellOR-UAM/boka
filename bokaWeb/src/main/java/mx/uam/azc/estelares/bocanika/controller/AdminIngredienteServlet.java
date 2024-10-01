package mx.uam.azc.estelares.bocanika.controller;

import mx.uam.azc.estelares.bocanika.data.AdminIngredienteDTO;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet implementation class AdminIngredienteServlet
 */
@WebServlet(name = "IngredienteUpdate", urlPatterns = { "/IngredienteUpdate" })
public class AdminIngredienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AdminIngredienteServlet() {
        super();
    }

    /**
     * Maneja las solicitudes POST (Añadir, Modificar, Borrar).
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("AdminIngredienteServlet: manejando acción de ingrediente...");

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "Agregar":
                    agregarIngrediente(request);
                    break;
                case "Modificar":
                    modificarIngrediente(request);
                    break;
                case "Borrar":
                    borrarIngrediente(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        String base = request.getContextPath();
        response.sendRedirect(base + "/admin_ingrediente.jsp");
    }

    /**
     * Método para añadir un nuevo ingrediente usando AdminIngredienteDTO.
     */
    private void agregarIngrediente(HttpServletRequest request) throws NamingException, SQLException {
        AdminIngredienteDTO ingrediente = getIngredienteFromRequest(request);

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO ingrediente (nombre_ingrediente) VALUES (?)")) {

            statement.setString(1, ingrediente.getNombreIngrediente());
            statement.executeUpdate();
        }
    }

    /**
     * Método para modificar un ingrediente existente usando AdminIngredienteDTO.
     */
    private void modificarIngrediente(HttpServletRequest request) throws NamingException, SQLException {
        AdminIngredienteDTO ingrediente = getIngredienteFromRequest(request);

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE ingrediente SET nombre_ingrediente = ? WHERE id_ingrediente = ?")) {

            statement.setString(1, ingrediente.getNombreIngrediente());
            statement.setInt(2, ingrediente.getIdIngrediente());
            statement.executeUpdate();
        }
    }

    /**
     * Método para borrar un ingrediente existente.
     */
    private void borrarIngrediente(HttpServletRequest request) throws NamingException, SQLException {
        int idIngrediente = Integer.parseInt(request.getParameter("id_ingrediente"));

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ingrediente WHERE id_ingrediente = ?")) {

            statement.setInt(1, idIngrediente);
            statement.executeUpdate();
        }
    }

    /**
     * Método para extraer la información del ingrediente de la solicitud (request) y crear un AdminIngredienteDTO.
     */
    private AdminIngredienteDTO getIngredienteFromRequest(HttpServletRequest request) {
        AdminIngredienteDTO ingrediente = new AdminIngredienteDTO();

        String idIngrediente = request.getParameter("id_ingrediente");
        if (idIngrediente != null && !idIngrediente.isEmpty()) {
            ingrediente.setIdIngrediente(Integer.parseInt(idIngrediente));
        }

        ingrediente.setNombreIngrediente(request.getParameter("nombre_ingrediente"));
        return ingrediente;
    }
}
