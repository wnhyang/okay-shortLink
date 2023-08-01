package cn.wnhyang.okay.shortlink.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wnhyang
 * @date 2022-09-06 10:44
 **/
public class ConnectionManager {

    private static final ThreadLocal<Connection> DB_CONNECTION_LOCAL = ThreadLocal.withInitial(() -> {
        try {
            return DriverManager.getConnection("", "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    });

    public Connection getConnection() {
        return DB_CONNECTION_LOCAL.get();
    }
}
