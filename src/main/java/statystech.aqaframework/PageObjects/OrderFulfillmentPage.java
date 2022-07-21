package statystech.aqaframework.PageObjects;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.Button;
import statystech.aqaframework.elements.CheckBox;

public class OrderFulfillmentPage extends PageObject{

    private static final Logger logger = LoggerFactory.getLogger(OrderFulfillmentPage.class);
    private final WebDriver webDriver;

    public OrderFulfillmentPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
        waitForJStoLoad();
        waitForElementToLoad(btnCreateParcel);
        if (!webDriver.findElement(btnCreateParcel).isDisplayed()) {
            logger.error("This is not the OrderFulfillmentPage Pop Up");
            throw new IllegalStateException("This is not the OrderFulfillmentPage Pop Up");
        }
    }

    By btnCreateParcel = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/div/button");
    String checkBox = "//*[@id=\"root\"]/div[2]/div/div/div[2]/table/tbody/tr/td[1]/div/span[1]";
    By btnClose = By.xpath("//*[@id=\"root\"]/div[2]/header/div/div/div/div[2]/button[2]");

    public OrderFulfillmentPage clickCreateParcelButton() {
        Button button = new Button(webDriver, btnCreateParcel);
        button.click();
        waitForJStoLoad();
        waitForElementToBeNotClickable(btnCreateParcel);
        return this;
    }

    public OrderCardPopUpPage close() {
        Button button = new Button(webDriver, btnClose);
        button.click();
        waitForElementToDisappear(btnClose);
        waitForJStoLoad();
        return new OrderCardPopUpPage(webDriver);
    }

    public OrderFulfillmentPage checkProducts(int productNumberAtTheList) {
        new CheckBox(webDriver, By.xpath("(" + checkBox + ")" + "[" + productNumberAtTheList + "]")).check();
        return this;
    }
}
