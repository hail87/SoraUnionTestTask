package statystech.aqaframework.PageObjects;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;

public class OrderCardDetailsPopUp extends PageObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderCardDetailsPopUp.class);
    private final WebDriver webDriver;
    @Getter
    String url;
    private final static By locator = By.xpath("//html/body/div[2]/div[3]");

    public OrderCardDetailsPopUp(WebDriver webDriver) {
        super.webDriver = webDriver;
        this.webDriver = webDriver;
        waitForJStoLoad();
        waitForElementToLoad(txtOrderNumber);
        url = webDriver.getCurrentUrl();
        if (!webDriver.findElement(txtOrderNumber).isDisplayed()) {
            logger.error("This is not the OrderCard Pop Up");
            throw new IllegalStateException("This is not the OrderCard Pop Up");
        }
    }

    By txtOrderNumber = By.xpath("/html/body/div[2]/div[3]/div/div/div[1]/div[2]/div/h6");
    By btnStartOrderFulfillment = By.xpath("/html/body/div[2]/div[3]/div/div/div[3]/div/div/button");
    By btnClose = By.xpath("/html/body/div[2]/div[3]/div/div/div[1]/div[1]/button");

    By btnOptions = By.xpath(".//div/div/div[1]/div[2]/div/button");

    By optnPutOnHold = By.xpath("/html/body/div[3]/div[3]/ul/li[1]/div[2]");
    By optnMoveToNewOrders = By.xpath("/html/body/div[3]/div[3]/ul/li[2]/div[2]");
    By msgMoveToNewOrder = By.xpath(".//div/div");
    By btnCancelMoveToNewOrder = By.xpath("/html/body/div[3]/div[3]/div/div[2]/div[2]/button[1]");
    By btnConfirmMoveToNewOrder = By.xpath("/html/body/div[3]/div[3]/div/div[2]/div[2]/button[2]");
    By optnPrintOrderSummary = By.xpath("/html/body/div[3]/div[3]/ul/li[3]/div[2]");
    By orderStatus = By.xpath("/html/body/div[2]/div[3]/div/div/div[1]/div[2]/div/span/div/span");

    public OrderFulfillmentPage startOrderFulfillment() {
        new Button(webDriver, btnStartOrderFulfillment).click();
        waitForElementToDisappear(txtOrderNumber);
        return new OrderFulfillmentPage(webDriver);
    }

    public String getStartOrderFulfillmentButtonLabel() {
        return new Button(webDriver, btnStartOrderFulfillment).getText();
    }

    public String getOrderStatus() {
        return new Button(webDriver, orderStatus).getText();
    }

    public OrderCardDetailsPopUp clickOptionButton() {
        new Button(webDriver, getChildLocator(locator, btnOptions)).click();
        waitForElementToLoad(optnPutOnHold);
        return this;
    }

    public MainPage clickPutOnHold() {
        new Button(webDriver, optnPutOnHold).click();
        MainPage mainPage = new MainPage(webDriver);
        mainPage.waitForElementToLoad(mainPage.btnCalendarPutOnHold);
        return mainPage;
    }

    public OrderCardDetailsPopUp clickMoveToNew() {
        new Button(webDriver, optnMoveToNewOrders).click();
        waitForElementToLoad(getChildLocator(locator, msgMoveToNewOrder));
        return this;
    }

    public MainPage clickConfirmMoveToNew() {
        new Button(webDriver, btnConfirmMoveToNewOrder).click();
        return new MainPage(webDriver);
    }

    public OrderCardDetailsPopUp clickCancelMoveToNew() {
        new Button(webDriver, btnCancelMoveToNewOrder).click();
        waitForElementToLoad(locator);
        return this;
    }

    public MainPage close() {
        Button button = new Button(webDriver, btnClose);
        button.click();
        waitForElementToDisappear(btnClose);
        waitForJStoLoad();
        return new MainPage(webDriver);
    }
}
