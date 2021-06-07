package statystech.aqaframework.common.Context;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.common.ConnectionDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Getter
@Setter
public abstract class TestContext {

    private ConnectionDB connectionDB;
    private String testMethodName;
    private JsonObject jsonObject;
    private String jsonString;

    public Connection getConnection() throws SQLException, IOException {
        if (connectionDB == null) {
            connectionDB = new ConnectionDB();
            connectionDB.connectDB();
        }
        return connectionDB.getCurrentConnection();
    }
}
