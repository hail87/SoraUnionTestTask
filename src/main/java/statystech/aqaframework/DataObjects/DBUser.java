package statystech.aqaframework.DataObjects;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.utils.DataUtils;

import java.io.IOException;

@Getter
@Setter
public class DBUser {

    public DBUser() throws IOException {
        this.name = DataUtils.getPropertyValue("DB.properties", "name");
        this.password = DataUtils.getPropertyValue("DB.properties", "pass");
        this.url = DataUtils.getPropertyValue("DB.properties", "url");
    }

    private String name;
    private String password;
    private String url;

}
