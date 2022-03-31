package statystech.aqaframework.common;

public enum MyPath {

    RESOURCES_PATH ("src/resources/"),
    JSON_PATH (RESOURCES_PATH.getPath() + "json/"),
    SQL_SCRIPTS_PATH (RESOURCES_PATH.getPath() + "SQL/"),
    TEST_RAIL ("https://statystechqa.testrail.io/"),
    LOG_FILE ("target/logs/test1.log");

    private final String path;

    MyPath(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
