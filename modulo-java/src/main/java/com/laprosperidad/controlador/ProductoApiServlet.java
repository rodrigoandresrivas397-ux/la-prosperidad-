package com.laprosperidad.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laprosperidad.dao.ProductoDAO;
import com.laprosperidad.modelo.Producto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/productos/*")
public class ProductoApiServlet extends HttpServlet {

    private final ProductoDAO dao = new ProductoDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            String ruta = request.getPathInfo();

            if (ruta == null || "/".equals(ruta)) {
                response.setStatus(200);
                response.getWriter().write(gson.toJson(dao.listar()));
                return;
            }

            int id = obtenerId(ruta);
            Producto producto = dao.buscarPorId(id);

            if (producto == null) {
                responder(response, 404,
                        Map.of("error", "Producto no encontrado"));
                return;
            }

            responder(response, 200, producto);

        } catch (NumberFormatException e) {
            responder(response, 400, Map.of("error", "ID no válido"));
        } catch (SQLException e) {
            responder(response, 500,
                    Map.of("error", "Error al consultar productos"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        procesarProducto(request, response, false);
    }

    @Override
    protected void doPut(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        procesarProducto(request, response, true);
    }

    private void procesarProducto(HttpServletRequest request,
            HttpServletResponse response, boolean actualizar)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            String ruta = request.getPathInfo();
            int id = 0;

            if (actualizar) {
                if (ruta == null || "/".equals(ruta)) {
                    responder(response, 400,
                            Map.of("error", "Debe indicar el ID del producto"));
                    return;
                }
                id = obtenerId(ruta);
            } else if (ruta != null && !"/".equals(ruta)) {
                responder(response, 404,
                        Map.of("error", "Ruta no encontrada"));
                return;
            }

            Producto producto =
                    gson.fromJson(request.getReader(), Producto.class);

            if (producto == null || producto.getNombre() == null
                    || producto.getNombre().trim().isEmpty()
                    || producto.getPrecio() < 0
                    || producto.getCantidad() < 0) {
                responder(response, 400,
                        Map.of("error", "Datos del producto no válidos"));
                return;
            }

            if (actualizar) {
                producto.setId(id);

                if (dao.actualizar(producto)) {
                    responder(response, 200,
                            Map.of("mensaje", "Producto actualizado correctamente"));
                } else {
                    responder(response, 404,
                            Map.of("error", "Producto no encontrado"));
                }
            } else {
                if (dao.insertar(producto)) {
                    responder(response, 201,
                            Map.of("mensaje", "Producto registrado correctamente"));
                } else {
                    responder(response, 500,
                            Map.of("error", "No fue posible registrar el producto"));
                }
            }

        } catch (JsonSyntaxException e) {
            responder(response, 400,
                    Map.of("error", "El formato JSON no es válido"));
        } catch (NumberFormatException e) {
            responder(response, 400, Map.of("error", "ID no válido"));
        } catch (SQLException e) {
            responder(response, 500,
                    Map.of("error", "Error al guardar el producto"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        try {
            String ruta = request.getPathInfo();

            if (ruta == null || "/".equals(ruta)) {
                responder(response, 400,
                        Map.of("error", "Debe indicar el ID del producto"));
                return;
            }

            int id = obtenerId(ruta);

            if (dao.eliminar(id)) {
                responder(response, 200,
                        Map.of("mensaje", "Producto eliminado correctamente"));
            } else {
                responder(response, 404,
                        Map.of("error", "Producto no encontrado"));
            }

        } catch (NumberFormatException e) {
            responder(response, 400, Map.of("error", "ID no válido"));
        } catch (SQLException e) {
            responder(response, 500,
                    Map.of("error", "Error al eliminar el producto"));
        }
    }

    private int obtenerId(String ruta) {
        if (!ruta.matches("/[0-9]+")) {
            throw new NumberFormatException();
        }
        return Integer.parseInt(ruta.substring(1));
    }

    private void responder(HttpServletResponse response,
            int codigo, Object datos) throws IOException {

        response.setStatus(codigo);
        response.getWriter().write(gson.toJson(datos));
    }
}