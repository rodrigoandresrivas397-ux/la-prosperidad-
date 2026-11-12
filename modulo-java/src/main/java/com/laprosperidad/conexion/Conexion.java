
package com.laprosperidad.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/prosperidad_db";

    private static final String USER = "postgres";

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection conectar() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                "No se ha configurado la variable DB_PASSWORD"
            );
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "Driver de PostgreSQL no encontrado", e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
