package statystech.aqaframework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.ConnectionDB;
import org.apache.ibatis.jdbc.ScriptRunner;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.Path;

import java.io.*;
import java.sql.*;

public class DBUtils {

    private static final Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public int insertJsonToStageOrder(String jsonContent) throws SQLException, IOException {
        Statement statement = Context.getTestContext().getConnection().createStatement();
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

    public int insertJsonToStageProduct(String jsonContent) throws SQLException, IOException {
        Statement statement = Context.getTestContext().getConnection().createStatement();
        int createdId;
        statement.executeUpdate("INSERT INTO stageProduct " +
                        "(productData, status, createdDate, createdBy, modifiedDate, modifiedBy, processingComment) " +
                        "Values ('" + jsonContent + "','N',CURRENT_TIMESTAMP,'',CURRENT_TIMESTAMP,'','')",
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

    public static ResultSet execute(String fullRequest) {
        ResultSet rs = null;
        try {
            rs = Context.getTestContext().getConnection().createStatement().executeQuery(fullRequest);
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.error("!!!Query:\n" + fullRequest + "\nhasn't been executed!!!");
        }
        return rs;
    }

    public static String executeAndReturnString(String fullRequest) throws SQLException, IOException {
        ResultSet rs;
        rs = Context.getTestContext().getConnection().createStatement().executeQuery(fullRequest);
        rs.next();
        return rs.getString(1);
    }

    public String select(String tableName, int rowId, String columnName) throws SQLException {
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + tableName + "ID = " + rowId;
        ResultSet rs = execute(query);
        rs.next();
        String result = rs.getString(columnName);
        logger.info("\n!!!Query :\n" + query + "\n has been executed with result : " + result);
        return result;
    }

    public String select(String tableName, String columnName) throws SQLException {
        ResultSet rs = getLastRow(tableName);
        rs.next();
        return rs.getString(columnName);
    }

    public static String select(String tableName, int columnNumber) throws SQLException {
        ResultSet rs = getLastRow(tableName);
        rs.next();
        return rs.getString(columnNumber);
    }

    private static ResultSet getLastRow(String tableName) {
        return execute(String.format("SELECT * FROM %s ORDER by createdDate DESC LIMIT 1", tableName));
    }

    public boolean closeConnection() throws SQLException, IOException {
        boolean isClosed = false;
        Connection connection = Context.getTestContext().getConnection();
        try {
            connection.close();
            isClosed = connection.isClosed();
            if (isClosed)
                logger.info("Connection is closed");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isClosed;
    }

    public static void cleanDB(String scriptName) throws IOException, SQLException {
        Connection connection = new ConnectionDB().getCurrentConnection();
        ScriptRunner sr = new ScriptRunner(connection);
        Reader reader = new BufferedReader(new FileReader(Path.SQL_SCRIPTS_PATH.getPath() + scriptName));
        sr.runScript(reader);
        connection.close();
    }
}
