package com.laprosperidad.controlador;

import com.laprosperidad.dao.ProductoDAO;
import com.laprosperidad.modelo.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductoServlet extends HttpServlet {

    private final ProductoDAO dao = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            String accion = request.getParameter("accion");

            if ("editar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Producto producto = dao.buscarPorId(id);

                if (producto == null) {
                    response.sendRedirect("productos?mensaje=no-encontrado");
                    return;
                }

                request.setAttribute("productoEditar", producto);
            }

            request.setAttribute("productos", dao.listar());
            request.getRequestDispatcher("/productos.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error al consultar productos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String accion = request.getParameter("accion");

            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.eliminar(id);
                response.sendRedirect("productos?mensaje=eliminado");
                return;
            }

            Producto producto = new Producto();
            producto.setNombre(request.getParameter("nombre"));
            producto.setDescripcion(request.getParameter("descripcion"));
            producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
            producto.setCantidad(Integer.parseInt(request.getParameter("cantidad")));

            if ("actualizar".equals(accion)) {
                producto.setId(Integer.parseInt(request.getParameter("id")));
                dao.actualizar(producto);
                response.sendRedirect("productos?mensaje=actualizado");
            } else {
                dao.insertar(producto);
                response.sendRedirect("productos?mensaje=guardado");
            }

        } catch (Exception e) {
            throw new ServletException("Error al procesar el producto", e);
        }
    }
}