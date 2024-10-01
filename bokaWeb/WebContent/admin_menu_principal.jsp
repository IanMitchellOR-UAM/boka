<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session == null || session.getAttribute("admin") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Principal - Administración de Base de Datos</title>
        <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        h2 {
            color: #9B69D6;
            text-align: center;
            margin-top: 20px;
        }

        p {
            text-align: center;
            font-size: 16px;
            color: #333;
        }

        ul {
            list-style: none;
            padding: 0;
            text-align: center;
        }

        ul li {
            margin: 15px 0;
        }

        ul li a {
            text-decoration: none;
            font-size: 18px;
            color: white;
            background-color: #9B69D6;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s;
        }

        ul li a:hover {
            background-color: #7B4FB3;
        }

        form {
            text-align: center;
            margin-top: 30px;
        }

        form button {
            background-color: #9B69D6;
            color: white;
            border: none;
            padding: 10px 20px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        form button:hover {
            background-color: #7B4FB3;
        }

    </style>

</head>
<body>

    <!-- Incluir el header -->
    <jsp:include page="decorators/admin_header.jsp" />

    <h2>Menú Principal</h2>
    <p>Selecciona la tabla que deseas administrar:</p>

    <ul>
        <!-- Enlace para administrar la tabla de productos -->
        <li><a href="admin_productos.jsp">Administrar Productos</a></li>

        <!-- Enlace para administrar la tabla de tipo de productos -->
        <li><a href="admin_tipo_producto.jsp">Administrar Tipos de Productos</a></li>

        <!-- Enlace para administrar la tabla de ingredientes -->
        <li><a href="admin_ingrediente.jsp">Administrar Ingredientes</a></li>
    </ul>
    
    <!-- Opción para cerrar sesión -->
    <form action="logoutServlet" method="POST">
        <button type="submit">Cerrar Sesión</button>
    </form>

    <!-- Incluir el footer -->
    <jsp:include page="decorators/footer.jsp" />

</body>
</html>
