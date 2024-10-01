<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Principal - Usuarios</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        p {
            text-align: center;
            color: #333;
            font-size: 18px;
        }

        /* Estilo para centrar la imagen y ajustarla en el área blanca */
        .content {
            background-color: white;
            padding: 40px;
            text-align: center;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 80%;
            margin: 20px auto;
            border-radius: 8px;
        }

        .content img {
            max-width: 100%;
            height: auto;
            border-radius: 10px;
            margin-top: 20px;
        }
    </style>
</head>
<body>

    <!-- Incluir el header -->
    <jsp:include page="decorators/header.jsp" />

    <!-- Incluir el menú para usuarios -->
    <jsp:include page="decorators/menuUser.jsp" />
    
    <!-- Contenido con imagen -->
    <div class="content">
        <p>Bienvenido a la aplicación de gestión de productos. Esta es la sección para los usuarios normales.</p>

        <!-- Imagen añadida -->
        <img src="img/back.png" alt="Imagen descriptiva de productos">
    </div>

    <!-- Incluir el footer -->
    <jsp:include page="decorators/footer.jsp" />

</body>
</html>
