package statystech.aqaframework.DataObjects;

import lombok.Getter;
import lombok.Setter;


import java.io.IOException;

@Getter
@Setter
public class DBUser {

    public DBUser() {
        this.name = "";
        this.password = "";
        this.url = "";
    }

    public DBUser(String propertyFileName) {
        this.name = "";
        this.password = "";
        this.url = "";
    }

    private String name;
    private String password;
    private String url;

}
