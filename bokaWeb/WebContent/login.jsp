<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Administración</title>
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

        form {
            margin: 20px auto;
            width: 300px;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        form label {
            font-weight: bold;
            color: #333;
            display: block;
            margin-bottom: 1px;
            text-align: left;
        }

        form input[type="text"],
        form input[type="password"] {
            width: 80%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        form button[type="submit"] {
            background-color: #9B69D6;
            color: white;
            border: none;
            padding: 10px 20px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 100%;
        }

        form button[type="submit"]:hover {
            background-color: #7B4FB3;
        }

        p {
            color: red;
            text-align: center;
        }

        /* Estilo para el mensaje de error */
        .error-message {
            color: red;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <!-- Incluir el header -->
    <jsp:include page="decorators/header.jsp" />

    <h2>Login de Administración</h2>

    <form action="loginServlet" method="POST">
        <label>Usuario:</label><br>
        <input type="text" name="username" required><br>

        <label>Contraseña:</label><br>
        <input type="password" name="password" required><br><br>

        <button type="submit">Ingresar</button>
    </form>

    <!-- Mostrar mensaje de error si las credenciales son incorrectas -->
    <%
        if (request.getAttribute("error") != null) {
    %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <%
        }
    %>

    <!-- Incluir el footer -->
    <jsp:include page="decorators/footer.jsp" />

</body>
</html>
