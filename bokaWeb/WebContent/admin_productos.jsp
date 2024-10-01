<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
    <title>Administrar Productos</title>
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

        form input[type="text"],
        form select {
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

        table input[type="text"],
        table select {
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

        table input[type="checkbox"] {
            margin-right: 5px;
        }

        h3 {
            color: #9B69D6;
            text-align: center;
            margin-top: 20px;
        }

        /* Centrar los botones de descarga */
        .button-group {
            display: flex;
            justify-content: center;
            gap: 10px; /* Espacio entre los botones */
        }

    </style>
</head>
<body>

    <!-- Incluir el header -->
    <jsp:include page="decorators/admin_header.jsp" />

    <h2>Administración de Productos</h2>

    <!-- Formulario de búsqueda por tipo de producto -->
    <form method="get" action="${ pageContext.request.contextPath }/admin_productos.jsp">
        <label for="busqueda_tipo">Buscar por Tipo de Producto:</label>
        <input type="text" name="busqueda_tipo" id="busqueda_tipo" value="${param.busqueda_tipo}" placeholder="Escribe tipo de producto">
        <input type="submit" value="Buscar">
    </form>

    <!-- Botón para descargar como Excel -->
    <form method="get" action="${ pageContext.request.contextPath }/ExportToExcelServlet">
        <input type="hidden" name="filter" value="${param.busqueda_tipo}" />
        <input type="submit" value="Descargar como Excel">
    </form>
    
    <!-- Botón para descargar como PDF -->
    <form method="get" action="${ pageContext.request.contextPath }/ExportToPDFServlet">
	    <!-- El valor del parámetro de búsqueda se pasará al servlet para exportar los datos filtrados -->
	    <input type="hidden" name="busqueda_tipo" value="${param.busqueda_tipo}">
	    <input type="submit" value="Descargar como PDF">
	</form>
    
    <!-- Botón para descargar como HTML -->
    <form method="get" action="${ pageContext.request.contextPath }/ExportToHTMLServlet">
	    <!-- El valor del parámetro de búsqueda se pasará al servlet para exportar los datos filtrados -->
	    <input type="hidden" name="busqueda_tipo" value="${param.busqueda_tipo}">
	    <input type="submit" value="Descargar como HTML">
	</form>

    <!-- Consulta para obtener todos los productos junto con su tipo y sus ingredientes -->
    <sql:query var="productos" dataSource="jdbc/TestDS">
        SELECT p.id_producto, p.nombre_producto, p.descripcion_producto, p.precio_producto, t.id_tipo, t.nombre_tipo, 
               GROUP_CONCAT(i.nombre_ingrediente SEPARATOR ', ') AS ingredientes
        FROM producto p
        INNER JOIN tipo_producto t ON p.tipo_producto = t.id_tipo
        LEFT JOIN producto_ingrediente pi ON p.id_producto = pi.id_producto
        LEFT JOIN ingrediente i ON pi.id_ingrediente = i.id_ingrediente
        WHERE t.nombre_tipo LIKE ?
        GROUP BY p.id_producto;

        <sql:param value="%${fn:escapeXml(param.busqueda_tipo)}%" />
    </sql:query>

    <!-- Mostrar los productos en una tabla -->
    <table border="1" width="100%">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tipo de Producto</th>
                <th>Ingredientes</th>
                <th>Nombre del Producto</th>
                <th>Descripción</th>
                <th>Precio</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="producto" items="${productos.rows}">
                <tr>
                    <form method="post" action="${ pageContext.request.contextPath }/ProductoUpdate">
                        <td>${producto.id_producto}<input type="hidden" name="id_producto" value="${producto.id_producto}"></td>

                        <!-- Select para tipo de producto -->
                        <td>
                            <select name="tipo_producto" required>
                                <sql:query var="tiposProducto" dataSource="jdbc/TestDS">
                                    SELECT id_tipo, nombre_tipo FROM tipo_producto;
                                </sql:query>
                                <c:forEach var="tipo" items="${tiposProducto.rows}">
                                    <option value="${tipo.id_tipo}" <c:if test="${producto.id_tipo == tipo.id_tipo}">selected</c:if>>${tipo.nombre_tipo}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <!-- Lista de ingredientes con checkboxes -->
                        <td>
                            <sql:query var="ingredientes" dataSource="jdbc/TestDS">
                                SELECT id_ingrediente, nombre_ingrediente FROM ingrediente;
                            </sql:query>
                            <c:forEach var="ingrediente" items="${ingredientes.rows}">
                                <input type="checkbox" name="ingredientes" value="${ingrediente.id_ingrediente}"
                                    <c:if test="${fn:contains(producto.ingredientes, ingrediente.nombre_ingrediente)}">checked</c:if>> 
                                ${ingrediente.nombre_ingrediente}<br>
                            </c:forEach>
                        </td>

                        <td><input type="text" name="nombre_producto" value="${producto.nombre_producto}" size="20"></td>
                        <td><input type="text" name="descripcion_producto" value="${producto.descripcion_producto}" size="30"></td>
                        <td><input type="text" name="precio_producto" value="${producto.precio_producto}" size="10"></td>
                        <td>
                            <input type="submit" name="action" value="Modificar">
                            <input type="submit" name="action" value="Borrar" onclick="return confirm('¿Estás seguro de eliminar este producto?');">
                        </td>
                    </form>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <h3>Añadir Nuevo Producto</h3>
    <form method="post" action="${ pageContext.request.contextPath }/ProductoUpdate">
        <table>
            <tr>
                <td>Nombre:</td>
                <td><input type="text" name="nombre_producto" size="20" required></td>
            </tr>
            <tr>
                <td>Descripción:</td>
                <td><input type="text" name="descripcion_producto" size="30"></td>
            </tr>
            <tr>    
                <td>Precio:</td>
                <td><input type="text" name="precio_producto" size="10" required></td>
            </tr>
            <tr>
                <td>Tipo de Producto:</td>
                <td>
                    <!-- Consulta para obtener los tipos de producto -->
                    <sql:query var="tiposProducto" dataSource="jdbc/TestDS">
                        SELECT id_tipo, nombre_tipo FROM tipo_producto;
                    </sql:query>
                    <select name="tipo_producto">
                        <c:forEach var="tipo" items="${tiposProducto.rows}">
                            <option value="${tipo.id_tipo}">${tipo.nombre_tipo}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Ingredientes:</td>
                <td>
                    <!-- Consulta para obtener los ingredientes -->
                    <sql:query var="ingredientes" dataSource="jdbc/TestDS">
                        SELECT id_ingrediente, nombre_ingrediente FROM ingrediente;
                    </sql:query>
                    <c:forEach var="ingrediente" items="${ingredientes.rows}">
                        <input type="checkbox" name="ingredientes" value="${ingrediente.id_ingrediente}">${ingrediente.nombre_ingrediente}<br>
                    </c:forEach>
                </td>
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
