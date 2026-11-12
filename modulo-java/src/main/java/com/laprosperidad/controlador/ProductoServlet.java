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
        try {
            String accion = request.getParameter("accion");

            if ("eliminar".equals(accion)) {
                dao.eliminar(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("productos?mensaje=eliminado");
                return;
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
            Producto producto = new Producto();

            producto.setNombre(request.getParameter("nombre"));
            producto.setDescripcion(request.getParameter("descripcion"));
            producto.setPrecio(Double.parseDouble(request.getParameter("precio")));
            producto.setCantidad(Integer.parseInt(request.getParameter("cantidad")));

            if ("actualizar".equals(accion)) {
                producto.setId(Integer.parseInt(request.getParameter("id")));
                dao.actualizar(producto);
            } else {
                dao.insertar(producto);
            }

            response.sendRedirect("productos?mensaje=guardado");

        } catch (Exception e) {
            throw new ServletException("Error al guardar el producto", e);
        }
    }
}