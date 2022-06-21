package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;
import statystech.aqaframework.elements.DropDown;

public class MainPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final WebDriver webDriver;
    By ddWarehousesButton = By.xpath("//*[@id=\"root\"]/header/div/div[1]/div[2]/div/div/div");
    By ddWarehousesOptions = By.xpath("//*[@id=\"menu-\"]/div[3]/ul");
    By activeOrders = By.xpath("//button[@id=\"0\"]");

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        waitForElementToLoad(ddWarehousesButton, webDriver);
        if (!webDriver.findElement(ddWarehousesButton).isDisplayed()) {
            logger.error("This is not the MAIN page");
            throw new IllegalStateException("This is not the MAIN page");
        }
    }

    public String getActiveOrders() {
        waitForElementToLoad(activeOrders, webDriver);
        String buttonText = webDriver.findElement(activeOrders).getText();
        int i = 0;
        while (buttonText.length() <= 7 || i <= 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buttonText = webDriver.findElement(activeOrders).getText();
            i++;
        }
        logger.info("Active orders : " + buttonText);
        return buttonText;
    }

    public MainPage chooseWarehouse(String warehouseName) {
        new Button(webDriver, ddWarehousesButton).click();
        DropDown ddWarehouse = new DropDown(webDriver, ddWarehousesOptions);
        ddWarehouse.selectByVisibleText(warehouseName);
        return this;
    }
}
