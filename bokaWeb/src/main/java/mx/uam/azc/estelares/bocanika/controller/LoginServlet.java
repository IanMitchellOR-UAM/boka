package mx.uam.azc.estelares.bocanika.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Verificar las credenciales
        if ("admin".equals(username) && "admin".equals(password)) {
            // Iniciar sesión
            HttpSession session = request.getSession();
            session.setAttribute("admin", "true");
            // Redirigir al menú de administración
            response.sendRedirect("admin_menu_principal.jsp");
        } else {
            // Redirigir de nuevo al login con mensaje de error
            request.setAttribute("error", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
