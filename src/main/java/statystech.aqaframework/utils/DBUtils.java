package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.DataObjects.DBUser;

import java.sql.*;

public class DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    private Connection connection = null;

    public void connectDB(DBUser user) {
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
        //Connection connection = null;
        try {
            connection = DriverManager.getConnection(user.getUrl(), user.getName(), user.getPassword());
        } catch (SQLException e) {
            logger.error("Can't get connection. Incorrect URL");
            e.printStackTrace();
        }
        if (connection != null) {
            logger.info("DB connected!");
        }
        //LOGGER.info to implement
    }

    public int insertJsonToStageOrder(String jsonContent) throws SQLException {
        Statement statement = connection.createStatement();
        int createdId;
        statement.executeUpdate("INSERT INTO stageOrder (orderData, status) Values ('" + jsonContent + "','N')",
                Statement.RETURN_GENERATED_KEYS);
        logger.info("SQL request was executed: " + jsonContent);
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                createdId = (int) generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating failed, no ID obtained.");
            }
        }
        return createdId;
    }

    public void select() throws SQLException {
    }

    public boolean deleteRow(int stageOrderId) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.execute("DELETE FROM stageOrder WHERE (`stageOrderID` = '" + stageOrderId + "'");
    }
}
