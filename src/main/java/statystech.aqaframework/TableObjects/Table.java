package statystech.aqaframework.TableObjects;


import java.sql.SQLException;

public interface Table {

    public String getColumnValue (String columnName) throws SQLException;

}
