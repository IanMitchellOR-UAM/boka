<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú de Navegación</title>
    <style>
        /* Estilos para el menú de navegación */
        nav {
            background-color: #9B69D6;
            padding: 15px;
        }

        nav ul {
            list-style: none;
            margin: 0;
            padding: 0;
            display: flex;
        }

        nav ul li {
            margin-right: 20px;
        }

        nav ul li a {
            text-decoration: none;
            color: white;
            font-weight: bold;
            font-family: Arial, sans-serif;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        nav ul li a:hover {
            background-color: #7B4FB3;
        }

        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>
    <nav>
        <ul>
            <li><a href="index.jsp">Inicio</a></li>
            <li><a href="menu_principal.jsp">Menú admin</a></li>
        </ul>
    </nav>
</body>
</html>
