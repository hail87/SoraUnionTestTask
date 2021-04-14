package statystech.aqaframework.TableObjects;

import com.google.common.base.CaseFormat;
import org.slf4j.helpers.Util;
import statystech.aqaframework.common.TestContext;
import statystech.aqaframework.utils.DBUtils;

import java.beans.Introspector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public abstract class TableObject {

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
        return Integer.parseInt(new DBUtils().executeAndReturnString(String.format("select * from %s where %s = \"%s\"", tableName, columnName, value)));
    }

    public String getColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        resultSet.next();
        return resultSet.getString(columnName);
    }

//    @Deprecated
//    public String getTableName() {
//        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
//        String tableName = fullTableName[fullTableName.length - 1];
//        return Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
//    }

    public String getColumnValue (String columnName) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return getLastRow(tableName).getString(columnName);
    }

    public String getColumnValue (int primaryID, String columnName) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return getProperRow(tableName, primaryID).getString(columnName);
    }

    protected ResultSet getLastRow(String tableName) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, tableName, getPrimaryID()));
        rs.next();
        return rs;
    }

    protected ResultSet getProperRow(String tableName, int id) throws SQLException {
        ResultSet rs = new DBUtils().execute(String.format(
                "select * from %s where %sID = %d", tableName, tableName, id));
        rs.next();
        return rs;
    }

    public int getProductIDbyProductAllSysID(String productAllSysID) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        return Integer.parseInt(
                new DBUtils().executeAndReturnString(String.format(
                        "select %sID from %s where %s = %s", tableName, tableName,"productAllSysID", productAllSysID))
        );
    }

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
