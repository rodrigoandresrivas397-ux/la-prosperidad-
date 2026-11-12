package com.laprosperidad.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/prosperidad_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Tatiana123";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de PostgreSQL no encontrado", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
