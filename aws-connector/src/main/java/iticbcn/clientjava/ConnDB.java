package iticbcn.clientjava;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConnDB {
    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("/home/this_andres/AWSJava/aws-connector/src/main/resources/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SQLException("No se pudo cargar el archivo de configuración", e);
        }
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        if (url == null || username == null || password == null) {
            throw new SQLException("Faltan propiedades en el archivo de configuración.");
        }
        return DriverManager.getConnection(url, username, password);
    }
}
