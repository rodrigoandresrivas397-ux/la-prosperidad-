<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.laprosperidad.modelo.Producto" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>La Prosperidad - Gestión de productos</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
            background: #f4f6f8;
        }

        h1, h2 { color: #2c3e50; }

        form {
            background: white;
            padding: 15px;
            margin-bottom: 20px;
            max-width: 450px;
            border-radius: 8px;
        }

        input {
            display: block;
            width: 95%;
            margin: 8px 0;
            padding: 9px;
        }

        button {
            padding: 9px 16px;
            cursor: pointer;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
            background: white;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background: #2c3e50;
            color: white;
        }

        .ok { color: green; font-weight: bold; }
        .acciones { display: flex; gap: 10px; align-items: center; }
        .acciones form { margin: 0; padding: 0; background: transparent; }
        .editar { background: #eaf2f8; padding: 15px; border-radius: 8px; }
    </style>
</head>

<body>

<h1>Gestión de productos</h1>

<%
    String mensaje = request.getParameter("mensaje");
    if (mensaje != null) {
%>
    <p class="ok">Operación realizada: <%= mensaje %></p>
<% } %>

<%
    Producto productoEditar =
        (Producto) request.getAttribute("productoEditar");

    if (productoEditar != null) {
%>

<h2>Editar producto</h2>

<form action="productos" method="post" class="editar">
    <input type="hidden" name="accion" value="actualizar">
    <input type="hidden" name="id" value="<%= productoEditar.getId() %>">

    <input type="text" name="nombre"
           value="<%= productoEditar.getNombre() %>"
           placeholder="Nombre" required>

    <input type="text" name="descripcion"
           value="<%= productoEditar.getDescripcion() == null ? "" : productoEditar.getDescripcion() %>"
           placeholder="Descripción">

    <input type="number" step="0.01" min="0" name="precio"
           value="<%= productoEditar.getPrecio() %>" required>

    <input type="number" min="0" name="cantidad"
           value="<%= productoEditar.getCantidad() %>" required>

    <button type="submit">Guardar cambios</button>
    <a href="productos">Cancelar</a>
</form>

<% } else { %>

<h2>Nuevo producto</h2>

<form action="productos" method="post">
    <input type="hidden" name="accion" value="insertar">

    <input type="text" name="nombre"
           placeholder="Nombre del producto" required>

    <input type="text" name="descripcion"
           placeholder="Descripción">

    <input type="number" step="0.01" min="0" name="precio"
           placeholder="Precio" required>

    <input type="number" min="0" name="cantidad"
           placeholder="Cantidad" required>

    <button type="submit">Guardar producto</button>
</form>

<% } %>

<h2>Listado de productos</h2>

<table>
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Descripción</th>
        <th>Precio</th>
        <th>Cantidad</th>
        <th>Acciones</th>
    </tr>

<%
    List<Producto> productos =
        (List<Producto>) request.getAttribute("productos");

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
            <div class="acciones">
                <a href="productos?accion=editar&id=<%= p.getId() %>">
                    Editar
                </a>

                <form action="productos" method="post"
                      onsubmit="return confirm('¿Deseas eliminar este producto?');">
                    <input type="hidden" name="accion" value="eliminar">
                    <input type="hidden" name="id" value="<%= p.getId() %>">
                    <button type="submit">Eliminar</button>
                </form>
            </div>
        </td>
    </tr>

<%
        }
    }
%>

</table>

</body>
</html>