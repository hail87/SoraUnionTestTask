package statystech.aqaframework.TableObjects;


import java.sql.SQLException;

public interface Table {

    final String TABLE_NAME = "shopperGroup";

    public String getColumnValue (String columnName) throws SQLException;

}
