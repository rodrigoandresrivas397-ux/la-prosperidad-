
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Locale, com.laprosperidad.modelo.Producto" %>

<%!
    // Escapa caracteres especiales para mostrar datos de forma segura.
    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    String mensaje = request.getParameter("mensaje");
    Producto productoEditar = (Producto) request.getAttribute("productoEditar");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");

    int totalProductos = productos == null ? 0 : productos.size();
    int unidades = 0;
    double valorInventario = 0;

    if (productos != null) {
        for (Producto p : productos) {
            unidades += p.getCantidad();
            valorInventario += p.getPrecio() * p.getCantidad();
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>La Prosperidad | Gestión de inventario</title>

    <style>
        :root {
            --verde: #174d3b;
            --verde-claro: #e8f2ed;
            --dorado: #c5a45b;
            --fondo: #f4f7f5;
            --texto: #24352e;
            --gris: #718078;
            --borde: #e3eae5;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: var(--fondo);
            color: var(--texto);
        }

        header {
            background: var(--verde);
            color: white;
            padding: 22px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
        }

        .marca { display: flex; align-items: center; gap: 12px; }

        .logo {
            width: 45px;
            height: 45px;
            border-radius: 12px;
            background: var(--dorado);
            color: var(--verde);
            display: grid;
            place-items: center;
            font-size: 22px;
            font-weight: bold;
        }

        header h1 { margin: 0; font-size: 22px; }
        header p { margin: 5px 0 0; color: #d8e8df; font-size: 13px; }

        .estado {
            background: #ffffff1c;
            padding: 10px 15px;
            border-radius: 20px;
            font-size: 13px;
        }

        main {
            width: 90%;
            max-width: 1250px;
            margin: 35px auto;
        }

        .bienvenida { margin-bottom: 25px; }
        .bienvenida h2 { margin: 0 0 8px; font-size: 27px; color: var(--verde); }
        .bienvenida p { margin: 0; color: var(--gris); }

        .resumen {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 18px;
            margin-bottom: 25px;
        }

        .tarjeta, .panel {
            background: white;
            border: 1px solid var(--borde);
            border-radius: 14px;
            padding: 22px;
            box-shadow: 0 3px 12px #173d2b0a;
        }

        .tarjeta span { color: var(--gris); font-size: 13px; }
        .tarjeta strong {
            display: block;
            color: var(--verde);
            font-size: 25px;
            margin-top: 10px;
        }

        .contenido {
            display: grid;
            grid-template-columns: 340px 1fr;
            gap: 22px;
            align-items: start;
        }

        .panel h3 { color: var(--verde); margin: 0 0 8px; font-size: 19px; }
        .subtitulo { color: var(--gris); font-size: 13px; margin: 0 0 20px; }

        label {
            display: block;
            font-size: 13px;
            font-weight: bold;
            margin: 15px 0 7px;
        }

        input {
            width: 100%;
            padding: 12px;
            border: 1px solid var(--borde);
            border-radius: 8px;
            font-size: 14px;
            outline: none;
        }

        input:focus {
            border-color: var(--verde);
            box-shadow: 0 0 0 3px #174d3b15;
        }

        button, .boton {
            border: none;
            border-radius: 8px;
            padding: 11px 16px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 13px;
        }

        .principal {
            background: var(--verde);
            color: white;
            width: 100%;
            margin-top: 20px;
        }

        .principal:hover { background: #103c2d; }

        .secundario {
            background: var(--verde-claro);
            color: var(--verde);
            margin-top: 10px;
            text-align: center;
        }

        .tabla-contenedor { overflow-x: auto; width: 100%; }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
            margin-top: 18px;
        }

        th {
            background: var(--verde-claro);
            color: var(--verde);
            font-size: 12px;
            text-align: left;
            padding: 14px 12px;
        }

        td { padding: 14px 12px; border-bottom: 1px solid var(--borde); font-size: 13px; }
        tr:hover td { background: #fafcfb; }

        .acciones { display: flex; align-items: center; gap: 8px; }
        .acciones form { margin: 0; }
        .editar { background: #fff4dc; color: #805b13; }
        .eliminar { background: #fcebea; color: #a52e2a; }
        .precio { font-weight: bold; color: var(--verde); }

        .alerta {
            padding: 13px 16px;
            background: var(--verde-claro);
            color: var(--verde);
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .vacio { text-align: center; padding: 35px; color: var(--gris); }

        footer {
            text-align: center;
            padding: 25px;
            color: var(--gris);
            font-size: 12px;
        }

        @media (max-width: 850px) {
            .contenido { grid-template-columns: 1fr; }
            .resumen { grid-template-columns: 1fr; }
            main { width: 94%; }
        }
    </style>
</head>

<body>
<header>
    <div class="marca">
        <div class="logo">LP</div>
        <div>
            <h1>La Prosperidad</h1>
            <p>Sistema de gestión de inventario</p>
        </div>
    </div>
    <div class="estado">● Sistema de inventario</div>
</header>

<main>
    <section class="bienvenida">
        <h2>Panel de productos</h2>
        <p>Administra y consulta los productos registrados en el sistema.</p>
    </section>

    <% if (mensaje != null) { %>
        <div class="alerta">
            <% if ("guardado".equals(mensaje)) { %>
                Producto registrado correctamente.
            <% } else if ("actualizado".equals(mensaje)) { %>
                Producto actualizado correctamente.
            <% } else if ("eliminado".equals(mensaje)) { %>
                Producto eliminado correctamente.
            <% } else if ("no-encontrado".equals(mensaje)) { %>
                No se encontró el producto solicitado.
            <% } else { %>
                Operación procesada.
            <% } %>
        </div>
    <% } %>

    <section class="resumen">
        <div class="tarjeta">
            <span>Productos registrados</span>
            <strong><%= totalProductos %></strong>
        </div>
        <div class="tarjeta">
            <span>Unidades disponibles</span>
            <strong><%= unidades %></strong>
        </div>
        <div class="tarjeta">
            <span>Valor del inventario</span>
            <strong>$<%= String.format(Locale.US, "%,.2f", valorInventario) %></strong>
        </div>
    </section>

    <section class="contenido">
        <div class="panel">
            <% if (productoEditar != null) { %>
                <h3>Editar producto</h3>
                <p class="subtitulo">Actualiza la información del producto.</p>

                <form action="productos" method="post">
                    <input type="hidden" name="accion" value="actualizar">
                    <input type="hidden" name="id" value="<%= productoEditar.getId() %>">

                    <label for="nombre">Nombre del producto</label>
                    <input id="nombre" type="text" name="nombre"
                           value="<%= escapar(productoEditar.getNombre()) %>"
                           maxlength="100" required>

                    <label for="descripcion">Descripción</label>
                    <input id="descripcion" type="text" name="descripcion"
                           value="<%= escapar(productoEditar.getDescripcion()) %>"
                           maxlength="255">

                    <label for="precio">Precio</label>
                    <input id="precio" type="number" step="0.01" min="0"
                           name="precio" value="<%= productoEditar.getPrecio() %>" required>

                    <label for="cantidad">Cantidad</label>
                    <input id="cantidad" type="number" min="0" name="cantidad"
                           value="<%= productoEditar.getCantidad() %>" required>

                    <button class="principal" type="submit">Guardar cambios</button>
                    <a class="boton secundario" href="productos">Cancelar</a>
                </form>
            <% } else { %>
                <h3>Nuevo producto</h3>
                <p class="subtitulo">Completa los datos para agregar un producto.</p>

                <form action="productos" method="post">
                    <input type="hidden" name="accion" value="insertar">

                    <label for="nombre">Nombre del producto</label>
                    <input id="nombre" type="text" name="nombre"
                           placeholder="Ej. Cuaderno" maxlength="100" required>

                    <label for="descripcion">Descripción</label>
                    <input id="descripcion" type="text" name="descripcion"
                           placeholder="Descripción del producto" maxlength="255">

                    <label for="precio">Precio</label>
                    <input id="precio" type="number" step="0.01" min="0"
                           name="precio" placeholder="0.00" required>

                    <label for="cantidad">Cantidad</label>
                    <input id="cantidad" type="number" min="0" name="cantidad"
                           placeholder="0" required>

                    <button class="principal" type="submit">+ Registrar producto</button>
                </form>
            <% } %>
        </div>

        <div class="panel">
            <h3>Inventario de productos</h3>
            <p class="subtitulo">Listado de productos almacenados en la base de datos.</p>

            <div class="tabla-contenedor">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Producto</th>
                            <th>Descripción</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (productos != null && !productos.isEmpty()) {
                        for (Producto p : productos) { %>
                        <tr>
                            <td><%= p.getId() %></td>
                            <td><strong><%= escapar(p.getNombre()) %></strong></td>
                            <td><%= escapar(p.getDescripcion()) %></td>
                            <td class="precio">$<%= String.format(Locale.US, "%,.2f", p.getPrecio()) %></td>
                            <td><%= p.getCantidad() %></td>
                            <td>
                                <div class="acciones">
                                    <a class="boton editar"
                                       href="productos?accion=editar&id=<%= p.getId() %>">Editar</a>

                                    <form action="productos" method="post"
                                          onsubmit="return confirm('¿Deseas eliminar este producto?');">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="id" value="<%= p.getId() %>">
                                        <button class="eliminar" type="submit">Eliminar</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    <% } } else { %>
                        <tr>
                            <td colspan="6" class="vacio">No hay productos registrados.</td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</main>

<footer>
    La Prosperidad | Sistema de gestión de inventario
</footer>
</body>
</html>
