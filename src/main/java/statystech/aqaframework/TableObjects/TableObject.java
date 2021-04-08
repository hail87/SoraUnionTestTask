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

    public String getPrimaryID() throws SQLException {
        return new DBUtils().select(Util.getCallingClass().getName(), 1);
    }

    public String getColumnValue(ResultSet resultSet, String columnName) throws SQLException {
        resultSet.next();
        return resultSet.getString(columnName);
    }

    public String getTableName() {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        return Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
    }

    public Map<String, String> getJsonAndTableValue(int primaryID, String jsonNodeKey1, String jsonNodeKey2) throws SQLException {
        String[] fullTableName = Util.getCallingClass().getName().split("\\.");
        String tableName = fullTableName[fullTableName.length - 1];
        tableName = Introspector.decapitalize(tableName.substring(0, tableName.length() - 5));
        String jsonValue = TestContext.JSON_OBJECT.getAsJsonObject(jsonNodeKey1).get(jsonNodeKey2).toString().replace("\"", "");
        String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, jsonNodeKey2);
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
