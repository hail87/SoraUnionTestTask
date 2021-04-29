package statystech.aqaframework.TableObjects;

import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Util;
import statystech.aqaframework.DataObjects.Product;
import statystech.aqaframework.common.ConnectionDB;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.utils.DBUtils;

import java.beans.Introspector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class TableObject {

    private static final Logger logger = LoggerFactory.getLogger(TableObject.class);

    public int getPrimaryID() throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return Integer.parseInt(new DBUtils().select(tableName, 1));
    }

    public int getPrimaryID(String columnName, String value) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        ResultSet resultSet = new DBUtils().execute(String.format(
                "select * from %s where %s = \"%s\" ORDER by createdDate DESC LIMIT 1", tableName, columnName, value));
        resultSet.next();
        return Integer.parseInt(resultSet.getString(1));
    }

    public String getRowColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        resultSet.next();
        return resultSet.getString(columnName);
    }

    public String getColumnValueByPrimaryID(int primaryID, String columnName) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return getProperRow(tableName, primaryID).getString(columnName);
    }

    public String getColumnValueByProductName(Product product, String columnName) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where productName = '%s' ORDER by createdDate DESC LIMIT 1", tableName, product.getProductName()));
        rs.next();
        return rs.getString(columnName);
    }

    public String getLastRowColumnValue(String columnName) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return getLastRow(tableName).getString(columnName);
    }

    protected ResultSet getLastRow(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d ORDER by createdDate DESC LIMIT 1", tableName, tableName, getPrimaryID()));
        rs.next();
        return rs;
    }

    protected ResultSet getProperRow(String tableName, int id) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, tableName, id));
        rs.next();
        return rs;
    }

    public boolean deleteRow(int primaryID) {
        boolean deleted = false;
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        try {
            deleted = ConnectionDB.connection.createStatement().execute(
                    "DELETE FROM " + tableName + " WHERE stageOrderID = '" + primaryID + "'");
            logger.info("!!!Row with id [" + primaryID + "] has been deleted!!!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            logger.error("!!!Row with id [" + primaryID + "] hasn't been deleted!!!");
        }
        return deleted;
    }

//    public int getProductIDbyProductAllSysID(String productAllSysID) throws SQLException {
//        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
//        String tableName = fullTableName[fullTableName.length - 1];
//        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
//        return Integer.parseInt(
//                new DBUtils().executeAndReturnString(String.format(
//                        "select %sID from %s where %s = %s", tableName, tableName,"productAllSysID", productAllSysID))
//        );
//    }

    public Map<String, String> getJsonAndTableValue(int primaryID, String jsonNodeKey1, String jsonNodeKey2) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));

        String jsonValue = "";
        try {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonObject(jsonNodeKey1).get(jsonNodeKey2).
                    toString().replace("\"", "");
        } catch (ClassCastException e) {
            jsonValue = TestContext.JSON_OBJECT.getAsJsonArray(jsonNodeKey1).get(0).getAsJsonObject().get(jsonNodeKey2)
                    .toString().replace("\"", "");
        }
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey2);
        columnName = Introspector.decapitalize(columnName);
        String tableValue = new DBUtils().executeAndReturnString(String.format(
                "select %s from %s where %s.%s = %d", columnName, tableName, tableName, getFirstColumnName(tableName), primaryID));
        return Map.of("jsonValue", jsonValue, "tableValue", tableValue);
    }

    public Map<String, String> getJsonAndTableValue(int primaryID, String jsonNodeKey1) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));

        String jsonValue = TestContext.JSON_OBJECT.get(jsonNodeKey1).
                toString().replace("\"", "");
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey1);
        columnName = Introspector.decapitalize(columnName);
        String tableValue = new DBUtils().executeAndReturnString(String.format(
                "select %s from %s where %s.%s = %d", columnName, tableName, tableName, getFirstColumnName(tableName), primaryID));
        return Map.of("jsonValue", jsonValue, "tableValue", tableValue);
    }

    private String getFirstColumnName(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute("show columns from " + tableName);
        rs.next();
        return rs.getString(1);
    }

}
