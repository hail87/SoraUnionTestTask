package statystech.aqaframework.common;

public enum Path {

    RESOURCES_PATH ("src/resources/"),
    JSON_PATH (RESOURCES_PATH.getPath() + "json/"),
    SQL_SCRIPTS_PATH (RESOURCES_PATH.getPath() + "SQL/");

    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
