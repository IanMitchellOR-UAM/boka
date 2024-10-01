<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> <!-- Se añadió esta línea -->

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
    <title>Administrar Tipos de Producto</title>
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
            width: 80%;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center; /* Centramos los botones en el formulario */
        }

        form label {
            font-weight: bold;
            color: #333;
        }

        form input[type="text"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        form input[type="submit"] {
            background-color: #9B69D6;
            color: white;
            border: none;
            padding: 10px 20px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            margin: 10px;
        }

        form input[type="submit"]:hover {
            background-color: #7B4FB3;
        }

        table {
            width: 90%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        table th, table td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }

        table th {
            background-color: #9B69D6;
            color: white;
        }

        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tr:hover {
            background-color: #e6e6e6;
        }

        table input[type="text"] {
            padding: 5px;
            width: 90%;
        }

        table input[type="submit"] {
            background-color: #9B69D6;
            color: white;
            border: none;
            padding: 5px 10px;
            cursor: pointer;
            border-radius: 5px;
            margin: 5px;
        }

        table input[type="submit"]:hover {
            background-color: #7B4FB3;
        }

        h3 {
            color: #9B69D6;
            text-align: center;
            margin-top: 20px;
        }

    </style>
</head>
<body>

    <!-- Incluir el header -->
    <jsp:include page="decorators/admin_header.jsp" />

    <h2>Administración de Tipos de Producto</h2>

    <!-- Formulario de búsqueda por tipo de producto -->
    <form method="get" action="${ pageContext.request.contextPath }/admin_tipo_producto.jsp">
        <label for="busqueda_tipo">Buscar por Tipo de Producto:</label>
        <input type="text" name="busqueda_tipo" id="busqueda_tipo" value="${param.busqueda_tipo}" placeholder="Escribe tipo de producto">
        <input type="submit" value="Buscar">
    </form>

    <!-- Consulta para obtener todos los tipos de productos -->
    <sql:query var="tipos" dataSource="jdbc/TestDS">
        SELECT id_tipo, nombre_tipo 
        FROM tipo_producto 
        WHERE nombre_tipo LIKE ?
        ORDER BY nombre_tipo;
        <sql:param value="%${fn:escapeXml(param.busqueda_tipo)}%" />
    </sql:query>

    <!-- Mostrar los tipos de productos en una tabla -->
    <table border="1" width="100%">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre del Tipo de Producto</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="tipo" items="${tipos.rows}">
                <tr>
                    <form method="post" action="${ pageContext.request.contextPath }/TipoProductoUpdate">
                        <td>${tipo.id_tipo}<input type="hidden" name="id_tipo" value="${tipo.id_tipo}"></td>
                        <td><input type="text" name="nombre_tipo" value="${tipo.nombre_tipo}" size="20"></td>
                        <td>
                            <input type="submit" name="action" value="Modificar">
                            <input type="submit" name="action" value="Borrar" onclick="return confirm('¿Estás seguro de eliminar este tipo de producto?');">
                        </td>
                    </form>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h3>Añadir Nuevo Tipo de Producto</h3>
    <form method="post" action="${ pageContext.request.contextPath }/TipoProductoUpdate">
        <table>
            <tr>
                <td>Nombre:</td>
                <td><input type="text" name="nombre_tipo" size="20" required></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" name="action" value="Agregar"></td>
            </tr>
        </table>
    </form>

    <!-- Incluir el footer -->
    <jsp:include page="decorators/footer.jsp" />

</body>
</html>
