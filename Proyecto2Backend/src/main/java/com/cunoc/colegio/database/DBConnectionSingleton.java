package com.cunoc.colegio.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DBConnectionSingleton {

    // Apuntamos al nombre que definiste en tu context.xml
    private static final String JNDI_NAME = "java:comp/env/jdbc/ColegioDB";

    private static DBConnectionSingleton instance;

    private DataSource dataSource;

    private DBConnectionSingleton() {
        try {

            dataSource = (DataSource) new InitialContext().lookup(JNDI_NAME);
        } catch (NamingException e) {
            throw new IllegalStateException(
                    "No se pudo resolver el DataSource JNDI: " + JNDI_NAME, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static DBConnectionSingleton getInstance() {
        if (instance == null) {
            instance = new DBConnectionSingleton();
        }
        return instance;
    }
}