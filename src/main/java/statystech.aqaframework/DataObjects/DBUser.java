package statystech.aqaframework.DataObjects;

import statystech.aqaframework.utils.DataUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class DBUser {

    public DBUser() throws IOException {
        DataUtils dataUtils = new DataUtils();
        this.name = dataUtils.getPropertyValue("DB.properties","name");
        this.password = dataUtils.getPropertyValue("DB.properties","pass");
        this.url = dataUtils.getPropertyValue("DB.properties","url");
    }

    private String name;
    private String password;
    private String url;

}
