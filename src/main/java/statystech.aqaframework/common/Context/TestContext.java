package statystech.aqaframework.common.Context;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import statystech.aqaframework.common.ConnectionDB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class TestContext {

    private ConnectionDB connectionDB;
    private String testMethodName;
    private JsonObject jsonObject;
    private String jsonString;
    private WebDriver driver;

    public Connection getConnection() throws SQLException, IOException {
        if (connectionDB == null) {
            connectionDB = new ConnectionDB();
            connectionDB.connectDB();
        }
        return connectionDB.getCurrentConnection();
    }

    public void closeDbConnection() throws SQLException, IOException {
        if (connectionDB != null)
            connectionDB.getCurrentConnection().close();
    }

    public void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/HAiL/IdeaProjects/aqa/lib/chromedriver");
        WebDriver driver = new ChromeDriver();

//        WebDriver driver = null;
//        try {
//            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), new ChromeOptions());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get("https://d11h187w8zd0az.cloudfront.net/");
        driver.manage().window().maximize();
        this.driver = driver;
    }

    public void closeWebDriverConnection() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
