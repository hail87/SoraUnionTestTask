package statystech.aqaframework.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.DBUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionDB.class);

    public static Connection connection = null;

    private DBUser user = null;

    public Connection getCurrentConnection()
    {
        if(connection != null)
        {
            return connection;
        }
        else
        {
            connectDB(this.user);
            return connection;
        }
    }

    public void connectDB(DBUser user) {

        if (connection!=null){
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
}
