package statystech.aqaframework.PageObjects;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;

public class OrderCardPopUpPage extends PageObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderCardPopUpPage.class);
    private final WebDriver webDriver;
    @Getter
    String url;

    public OrderCardPopUpPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
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

    public OrderFulfillmentPage startOrderFulfillment() {
        new Button(webDriver, btnStartOrderFulfillment).click();
        waitForElementToDisappear(txtOrderNumber);
        return new OrderFulfillmentPage(webDriver);
    }

    public MainPage close() {
        Button button = new Button(webDriver, btnClose);
        button.click();
        waitForElementToDisappear(btnClose);
        waitForJStoLoad();
        return new MainPage(webDriver);
    }
}
