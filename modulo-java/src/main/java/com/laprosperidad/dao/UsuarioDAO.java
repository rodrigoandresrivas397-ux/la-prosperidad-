package com.laprosperidad.dao;

import com.laprosperidad.conexion.Conexion;
import com.laprosperidad.seguridad.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Gestiona el registro y consulta de usuarios en PostgreSQL.
public class UsuarioDAO {

    // Registra un usuario con su contraseña protegida.
    public boolean registrar(String nombreUsuario, String password)
            throws Exception {

        String sql = "INSERT INTO usuarios "
                   + "(nombre_usuario, password_hash) VALUES (?, ?)";

        String hash = PasswordUtil.generarHash(password);

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombreUsuario);
            consulta.setString(2, hash);

            return consulta.executeUpdate() == 1;
        }
    }

    // Comprueba las credenciales de acceso.
    public boolean autenticar(String nombreUsuario, String password)
            throws Exception {

        String sql = "SELECT password_hash FROM usuarios "
                   + "WHERE nombre_usuario = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, nombreUsuario);

            try (ResultSet resultado = consulta.executeQuery()) {

                if (resultado.next()) {
                    String hash = resultado.getString("password_hash");

                    return PasswordUtil.verificarPassword(password, hash);
                }
            }
        }

        return false;
    }
}
