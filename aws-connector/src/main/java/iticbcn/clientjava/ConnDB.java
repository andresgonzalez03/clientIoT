package iticbcn.clientjava;

import java.sql.*;

public class ConnDB {
    private String connection;
    private String user;
    private String password;

    public ConnDB(String connection, String user, String password) {
        this.connection = connection;
        this.user = user;
        this.password = password;
    }
    public String getConnection() {
        return connection;
    }
    public String getPassword() {
        return password;
    }
    public String getUser() {
        return user;
    }
    public static Connection getConnection(String urlConn, String name, String pass) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlConn, name, pass);
            return conn;
        } catch (SQLException e) {
            throw new SQLException("Error al establecer la conexión a la base de datos", e);
        }
    }

}