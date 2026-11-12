<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.laprosperidad.modelo.Producto" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>La Prosperidad - Productos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #2c3e50; color: white; }
        form input { margin: 4px 0; padding: 6px; }
        .ok { color: green; }
    </style>
</head>
<body>
    <h1>Gesti&oacute;n de productos</h1>
    <% String mensaje = request.getParameter("mensaje");
       if (mensaje != null) { %>
        <p class="ok">Operaci&oacute;n realizada: <%= mensaje %></p>
    <% } %>
    <h2>Nuevo producto</h2>
    <form action="productos" method="post">
        <input type="hidden" name="accion" value="insertar">
        <input type="text" name="nombre" placeholder="Nombre" required><br>
        <input type="text" name="descripcion" placeholder="Descripci&oacute;n"><br>
        <input type="number" step="0.01" name="precio" placeholder="Precio" required><br>
        <input type="number" name="cantidad" placeholder="Cantidad" required><br>
        <button type="submit">Guardar</button>
    </form>    <h2>Listado</h2>
    <table>
        <tr>
            <th>ID</th><th>Nombre</th><th>Descripci&oacute;n</th>
            <th>Precio</th><th>Cantidad</th><th>Acciones</th>
        </tr>
        <%
            List<Producto> productos = (List<Producto>) request.getAttribute("productos");
            if (productos != null) {
                for (Producto p : productos) {
        %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getNombre() %></td>
            <td><%= p.getDescripcion() %></td>
            <td><%= p.getPrecio() %></td>
            <td><%= p.getCantidad() %></td>
            <td>
                <a href="productos?accion=eliminar&id=<%= p.getId() %>"
                   onclick="return confirm('Eliminar este producto?');">Eliminar</a>
            </td>
        </tr>
        <%      }
            }
        %>
    </table>
</body>
</html>