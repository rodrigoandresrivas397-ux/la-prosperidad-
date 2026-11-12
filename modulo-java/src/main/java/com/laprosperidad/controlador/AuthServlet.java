package com.laprosperidad.controlador;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laprosperidad.dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

// Servicio REST para el registro y la autenticación de usuarios.
@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    // Recibe solicitudes POST de registro e inicio de sesión.
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String ruta = request.getPathInfo();

        try {
            StringBuilder cuerpo = new StringBuilder();

            try (BufferedReader lector = request.getReader()) {
                String linea;

                while ((linea = lector.readLine()) != null) {
                    cuerpo.append(linea);
                }
            }

            SolicitudAuth datos =
                    gson.fromJson(cuerpo.toString(), SolicitudAuth.class);

            if (datos == null || datos.usuario == null
                    || datos.password == null) {
                responder(response, 400,
                        Map.of("error", "Debe enviar usuario y contraseña"));
                return;
            }

            String usuario = datos.usuario.trim();

            if (usuario.length() < 3 || usuario.length() > 50
                    || datos.password.length() < 8) {
                responder(response, 400,
                        Map.of("error",
                        "Usuario de 3 a 50 caracteres y contraseña de mínimo 8"));
                return;
            }

            // Procesa el registro de un nuevo usuario.
            if ("/registro".equals(ruta)) {

                boolean registrado =
                        usuarioDAO.registrar(usuario, datos.password);

                if (registrado) {
                    responder(response, 201,
                            Map.of("mensaje", "Usuario registrado correctamente"));
                }

            // Procesa la autenticación de un usuario existente.
            } else if ("/login".equals(ruta)) {

                boolean autenticado =
                        usuarioDAO.autenticar(usuario, datos.password);

                if (autenticado) {
                    responder(response, 200,
                            Map.of("mensaje", "Autenticación satisfactoria"));
                } else {
                    responder(response, 401,
                            Map.of("error", "Error en la autenticación"));
                }

            } else {
                responder(response, 404,
                        Map.of("error", "Servicio no encontrado"));
            }

        } catch (JsonSyntaxException e) {
            responder(response, 400,
                    Map.of("error", "El formato JSON no es válido"));

        } catch (SQLException e) {

            // El código 23505 indica que el usuario ya está registrado.
            if ("23505".equals(e.getSQLState())) {
                responder(response, 409,
                        Map.of("error", "El nombre de usuario ya existe"));
            } else {
                responder(response, 500,
                        Map.of("error", "Error al consultar la base de datos"));
            }

        } catch (Exception e) {
            responder(response, 500,
                    Map.of("error", "No fue posible procesar la solicitud"));
        }
    }

    // Envía la respuesta JSON con su código HTTP correspondiente.
    private void responder(
            HttpServletResponse response,
            int codigo,
            Map<String, String> mensaje) throws IOException {

        response.setStatus(codigo);
        response.getWriter().write(gson.toJson(mensaje));
    }

    // Representa los datos recibidos en las solicitudes JSON.
    private static class SolicitudAuth {
        String usuario;
        String password;
    }
}
