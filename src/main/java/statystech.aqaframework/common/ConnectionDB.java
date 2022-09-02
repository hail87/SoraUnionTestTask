package statystech.aqaframework.common;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.DBUser;
import statystech.aqaframework.utils.DBUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionDB.class);

    @Setter
    @Getter
    private Connection connection = null;

    private DBUser user = null;

    public void connectDB() throws SQLException {
        connectDB(new DBUser());
        logger.info("Data Base connected!");
    }

    public void connectDB(String propertyFileName) throws SQLException {
        connectDB(new DBUser(propertyFileName));
        logger.info("Data Base connected!");
    }

    public void closeConnection() {
        try {
            new DBUtils().closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getCurrentConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectDB();
        }
        return connection;
    }

    public void connectDB(DBUser user) throws SQLException {

        if (connection != null && !connection.isClosed()) {
            logger.info("DB connection already exist, no new connection won't be created!");
            return;
        }

        this.user = user;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("Can't get class. No driver found");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(user.getUrl(), user.getName(), user.getPassword());
        } catch (SQLException e) {
            logger.error("Can't get connection. Incorrect URL");
            e.printStackTrace();
        }
        if (connection != null) {
            logger.info("DB connected!");
        }
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }
}
