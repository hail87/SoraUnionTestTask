package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.ConnectionDB;

import java.sql.*;

public class DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    //private Connection connection = new ConnectionDB();


    public int insertJsonToStageOrder(String jsonContent) throws SQLException {
        Statement statement = ConnectionDB.connection.createStatement();
        int createdId;
        statement.executeUpdate("INSERT INTO stageOrder " +
                        "(orderData, status, processingComment, createdDate, createdBy, modifiedDate, modifiedBy) " +
                        "Values ('" + jsonContent + "','N','',CURRENT_TIMESTAMP,'',CURRENT_TIMESTAMP,'')",
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

    public ResultSet execute(String fullRequest) {
        ResultSet rs = null;
        try {
            rs = ConnectionDB.connection.createStatement().executeQuery(fullRequest);
            logger.info("Query:\n" + fullRequest + "\nhas been executed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("!!!Query:\n" + fullRequest + "\nhasn't been executed!!!");
        }
        return rs;
    }

    public String executeAndReturnString(String fullRequest) throws SQLException {
        ResultSet rs;
        rs = ConnectionDB.connection.createStatement().executeQuery(fullRequest);
        logger.info("Query:\n" + fullRequest + "\nhas been executed");
        rs.next();
        return rs.getString(1);
    }

    public String select(String tableName, int rowId, String columnName) throws SQLException {
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + tableName + "ID = " + rowId;
        ResultSet rs = execute(query);
        rs.next();
        String result = rs.getString(columnName);
        logger.info("!!!Query :\n" + query + "\n has been executed with result :\n" + result);
        return result;
    }

    public String select(String tableName, String columnName) throws SQLException {
        ResultSet rs = getLastRow(tableName);
        rs.next();
        return rs.getString(columnName);
    }

    public String select(String tableName, int columnNumber) throws SQLException {
        ResultSet rs = getLastRow(tableName);
        rs.next();
        return rs.getString(columnNumber);
    }

    private ResultSet getLastRow(String tableName) {
        return execute(String.format("SELECT * FROM %s ORDER by createdDate DESC LIMIT 1", tableName));
    }


    public boolean deleteRow(int stageOrderId) {
        boolean deleted = false;
        try {
            deleted = ConnectionDB.connection.createStatement().execute(
                    "DELETE FROM stageOrder WHERE stageOrderID = '" + stageOrderId + "'");
            logger.info("!!!Row with id [" + stageOrderId + "] has been deleted!!!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("!!!Row with id [" + stageOrderId + "] hasn't been deleted!!!");
        }
        return deleted;
    }


    public boolean closeConnection() {
        boolean isClosed = false;
        try {
            ConnectionDB.connection.close();
            isClosed = ConnectionDB.connection.isClosed();
            if (isClosed)
                logger.info("Connection is closed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isClosed;
    }

}
