package statystech.aqaframework.TableObjects;

import com.google.common.base.CaseFormat;
import groovy.json.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;
import statystech.aqaframework.utils.DBUtils;
import statystech.aqaframework.utils.DataUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class TableObject {

    private static final Logger logger = LoggerFactory.getLogger(TableObject.class);

    protected String TABLE_NAME = "";

    int linesQuantity;

    public String getName() {
        return TABLE_NAME;
    }

    public void setTableRowsQuantity() {
        linesQuantity = getRowsQuantity();
        logger.info(TABLE_NAME + " table rows quantity: " + linesQuantity);
    }

    public int getRowsQuantity(int id) {
        ResultSet rs = getRowByID(id);
        int count = 0;
        try {
            while (rs.next()) {
                ++count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            System.out.println("No records found");
        }
        return count;
    }

    public int getRowsQuantity(ResultSet rs) {
        int count = 0;
        try {
            while (rs.next()) {
                ++count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            System.out.println("No records found");
        }
        return count;
    }

    public int getRowsQuantity() {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s", TABLE_NAME));
        int count = 0;
        try {
            while (rs.next()) {
                ++count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count == 0) {
            System.out.println("No records found");
        }
        return count;
    }

    public ResultSet getRows() {
        return DBUtils.executeUpdatable(String.format("select * from %s", TABLE_NAME));
    }

    public int getPrimaryID() throws SQLException, IOException {
        return Integer.parseInt(DBUtils.select(TABLE_NAME, 1));
    }

    public int getPrimaryID(String columnName, String value) throws SQLException {
        ResultSet resultSet = DBUtils.execute(String.format(
                "select * from %s where %s = '%s' ORDER by createdDate DESC LIMIT 1", TABLE_NAME, columnName, value));
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }

    public String getColumnValueByPrimaryID(int primaryID, String columnName) throws SQLException {
        return getProperRow(TABLE_NAME, primaryID).getString(columnName);
    }

    public String getColumnValueByColumnValue(String columnNameToGet, String columnNameToSearchIn, String valueToSearchBy) throws SQLException {
        return getProperRow(columnNameToSearchIn, valueToSearchBy).getString(columnNameToGet);
    }

    public String getColumnValueByProductName(String productName, String columnName) throws SQLException {
        if (productName.contains("®") || productName.contains("\t") || productName.contains("\\t") || productName.contains("\\u00ae")) {
            return getColumnValueContainsProductName(productName, columnName);
        } else {
            ResultSet rs = DBUtils.execute(String.format(
                    "select * from %s where productName = '%s' ORDER by createdDate DESC LIMIT 1", TABLE_NAME, StringEscapeUtils.unescapeJava(productName)));
            rs.next();
            return rs.getString(columnName);
        }
    }

    public String getColumnValueContainsProductName(String productName, String columnName) throws SQLException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where productName like '%s' ORDER by createdDate DESC LIMIT 1", TABLE_NAME, DataUtils.removeUnicode(productName)));
        rs.next();
        return rs.getString(columnName);
    }

    public String getLastRowColumnValue(String columnName) throws SQLException, IOException {
        String result = "";
        try {
            result = getLastRow(TABLE_NAME).getString(columnName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected ResultSet getLastRow(String tableName) throws SQLException, IOException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d ORDER by createdDate DESC LIMIT 1", tableName, tableName, getPrimaryID()));
        rs.next();
        return rs;
    }

    @Deprecated
    protected ResultSet getProperRow(String tableName, int id) throws SQLException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", tableName, tableName, id));
        rs.next();
        return rs;
    }

    protected ResultSet getProperRow(int id) throws SQLException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", TABLE_NAME, TABLE_NAME, id));
        rs.next();
        return rs;
    }

    protected ResultSet getProperRow(String columnName, String columnValue) throws SQLException {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %s = %s", TABLE_NAME, columnName, columnValue));
        rs.next();
        return rs;
    }

    public ResultSet getRowByID(int id) {
        ResultSet rs = DBUtils.execute(String.format(
                "select * from %s where %sID = %d", TABLE_NAME, TABLE_NAME, id));
        return rs;
    }

    public boolean deleteRow(int primaryID) {
        boolean deleted = false;
        try {
            deleted = Context.getTestContext(LwaTestContext.class).getConnection().createStatement().execute(
                    "DELETE FROM " + TABLE_NAME + " WHERE stageOrderID = '" + primaryID + "'");
            logger.info("!!!Row with id [" + primaryID + "] has been deleted!!!");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            logger.error("!!!Row with id [" + primaryID + "] hasn't been deleted!!!");
        }
        return deleted;
    }

    public Map<String, String> getJsonAndTableValue(int primaryID, String jsonNodeKey1, String jsonNodeKey2) throws SQLException {
        LwaTestContext lwaTestContext = Context.getTestContext(Thread.currentThread().getStackTrace()[3].getMethodName(), LwaTestContext.class);
        String jsonValue = "";
        try {
            jsonValue = lwaTestContext.getJsonObject().getAsJsonObject(jsonNodeKey1).get(jsonNodeKey2).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = lwaTestContext.getJsonObject().getAsJsonArray(jsonNodeKey1).get(0).getAsJsonObject().get(jsonNodeKey2)
                    .toString().replace("\"", "");
        }
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey2);
        columnName = Introspector.decapitalize(columnName);
        String tableValue = null;
        tableValue = DBUtils.executeAndReturnString(String.format(
                "select %s from %s where %s.%s = %d", columnName, TABLE_NAME, TABLE_NAME, getFirstColumnName(TABLE_NAME), primaryID));
        return Map.of("jsonValue", jsonValue, "tableValue", tableValue);
    }

    public Map<String, String> getJsonAndTableDecryptValue(int primaryID, String jsonNodeKey1, String jsonNodeKey2) throws SQLException {
        LwaTestContext lwaTestContext = Context.getTestContext(Thread.currentThread().getStackTrace()[3].getMethodName(), LwaTestContext.class);
        String jsonValue = "";
        try {
            jsonValue = lwaTestContext.getJsonObject().getAsJsonObject(jsonNodeKey1).get(jsonNodeKey2).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = lwaTestContext.getJsonObject().getAsJsonArray(jsonNodeKey1).get(0).getAsJsonObject().get(jsonNodeKey2)
                    .toString().replace("\"", "");
        }
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey2);
        columnName = Introspector.decapitalize(columnName);
        String tableValue = null;
        tableValue = DBUtils.executeAndReturnString(String.format(
                "select %s from %s where %s.%s = %d", columnName, TABLE_NAME, TABLE_NAME, getFirstColumnName(TABLE_NAME), primaryID));
        return Map.of("jsonValue", jsonValue, "tableValue", DataUtils.decrypt(tableValue));
    }

    public Map<String, String> getJsonAndTableValue(int primaryID, String jsonNodeKey1) throws SQLException {
        String jsonValue = Context.getTestContext(LwaTestContext.class).getJsonObject().get(jsonNodeKey1).
                toString().replace("\"", "");
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey1);
        columnName = Introspector.decapitalize(columnName);
        String tableValue = null;
        tableValue = DBUtils.executeAndReturnString(String.format(
                "select %s from %s where %s = %d", columnName, TABLE_NAME, getFirstColumnName(TABLE_NAME), primaryID));
        return Map.of("jsonValue", jsonValue, "tableValue", tableValue);
    }

    private String getFirstColumnName(String tableName) throws SQLException {
        ResultSet rs = DBUtils.execute("show columns from " + tableName);
        rs.next();
        return rs.getString(1);
    }

    public boolean checkRowWithIDExist(int id) {
        return !DBUtils.executeAndReturnString(String.format("select * from %s where %s = '%d'", TABLE_NAME, TABLE_NAME + "ID", id)).isEmpty();
    }

    public boolean checkRowWithValueIsPresent(String columnName, String value) {
        return !DBUtils.executeAndReturnString(String.format("select * from %s where %s = '%s'", TABLE_NAME, columnName, value)).isEmpty();
    }

    public String verifyTableRowsQuantityDidNotChange() {
        logger.info(String.format("%s table - rows quantity before update: %d", TABLE_NAME, linesQuantity));
        logger.info(String.format("%s table - rows quantity after update: %d", TABLE_NAME, getRowsQuantity()));
        return Steps.verifyExpectedResults(linesQuantity, getRowsQuantity());
    }

    public String verifyNewRowCreated() {
        logger.info(String.format("%s table - rows quantity before update: %d", TABLE_NAME, linesQuantity));
        logger.info(String.format("%s table - rows quantity after update: %d", TABLE_NAME, getRowsQuantity()));
        if (getRowsQuantity() <= linesQuantity) {
            return String.format("No new records at the '%s' table found\n", TABLE_NAME);
        }
        return "";
    }

    public void encryptRows(List<String> encryptedColums) throws SQLException {
        ResultSet rs = getRows();
        while (rs.next()) {
            for (String columnName : encryptedColums) {
                String originalData = rs.getString(columnName);
                String encryptedData = DataUtils.encrypt(originalData);
                if (encryptedData != null && !encryptedData.isEmpty()) {
                    try {
                        rs.updateString(columnName, encryptedData);
                        rs.updateRow();
                        logger.info(String.format("Value '%s' from column '%s : %s' has been encrypted successfully", originalData, TABLE_NAME, columnName));
                    } catch (SQLException e) {
                        rs.cancelRowUpdates();
                        logger.info(String.format("Can't update value '%s' from table '%s' column '%s' because field isn't encrypted", originalData, TABLE_NAME, columnName));
                    }
                }
            }
            logger.info(String.format("%s table encryption complete", TABLE_NAME));
        }
    }

}
