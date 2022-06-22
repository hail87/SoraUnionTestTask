package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;
import statystech.aqaframework.elements.DropDown;

public class MainPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final WebDriver webDriver;
    By ddWarehousesButton = By.xpath("//*[@id=\"root\"]/header/div/div[1]/div[2]/div/div/div");
    By ddOptions = By.xpath("//*[@id=\"menu-\"]/div[3]/ul");
    By ddDestinationsButton = By.xpath("(//div[@role=\"button\"])[1]");
    //By ddWarehousesButton = By.xpath("(//div[@role=\"button\"])[2]");
    By ddOrderStatusButton = By.xpath("(//div[@role=\"button\"])[3]");
    By btnCancelOrderStatusChoice = By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div/div[2]/div/div[2]/button");
    By txtActiveOrders = By.xpath("//button[@id=\"0\"]");
    By btnApply = By.xpath("//button[text()=\"Apply\"]");


    Button applyButton;

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
        waitForElementToLoad(ddWarehousesButton, webDriver);
        if (!webDriver.findElement(ddWarehousesButton).isDisplayed()) {
            logger.error("This is not the MAIN page");
            throw new IllegalStateException("This is not the MAIN page");
        }
    }

    public MainPage expandWarehouseDropdown() {
        new Button(webDriver, ddWarehousesButton).click();
        return this;
    }

    public MainPage selectWarehouseDropdownOption(String warehouseName) {
        DropDown ddWarehouse = new DropDown(webDriver, ddOptions);
        ddWarehouse.selectByVisibleText(warehouseName);
        return this;
    }

    public MainPage expandDestination() {
        new Button(webDriver, ddDestinationsButton).click();
        logger.info("Click All destinations button");
        return this;
    }

    public MainPage expandorderStatus() {
        new Button(webDriver, ddOrderStatusButton).click();
        logger.info("Click order status button");
        return this;
    }

    public MainPage selectDropdownOptionByText(String text) {
        WebElement dropDownElement = webDriver.findElement(ddOptions).findElement(By.xpath(".//span[text()=\"" + text + "\"]"));
        dropDownElement.click();
        logger.info("Option clicked at the Destination DropDown : " + text);
        unfocus();
        return this;
    }

    public String getActiveOrders() {
        waitForElementToLoad(txtActiveOrders, webDriver);
        String buttonText = webDriver.findElement(txtActiveOrders).getText();
        int i = 0;
        while (buttonText.length() <= 7 || i <= 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buttonText = webDriver.findElement(txtActiveOrders).getText();
            i++;
        }
        logger.info("Active orders : " + buttonText);
        return buttonText;
    }

    public MainPage clickCancelOrderStatusChoice() {
        new Button(webDriver, btnCancelOrderStatusChoice).click();
        logger.info("cancelOrderStatusChoice button clicked");
        return this;
    }

    public boolean isApplyButtonEnabled() {
        if(applyButton == null)
            applyButton = new Button(webDriver, btnApply);
        return applyButton.isEnabled();
    }

    public void clickApplyButton() {
        if(applyButton == null)
            applyButton = new Button(webDriver, btnApply);
        applyButton.click();
    }

}
