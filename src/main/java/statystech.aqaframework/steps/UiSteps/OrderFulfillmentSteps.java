package statystech.aqaframework.steps.UiSteps;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.OrderCardPopUpPage;
import statystech.aqaframework.PageObjects.OrderFulfillmentPage;
import statystech.aqaframework.steps.Steps;

public class OrderFulfillmentSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderFulfillmentSteps.class);

    private WebDriver webDriver;

    private OrderFulfillmentPage orderFulfillmentPage;

//    public OrderFulfillmentSteps(WebDriver webDriver) {
//        this.webDriver = webDriver;
//        orderFulfillmentPage = new OrderFulfillmentPage(webDriver);
//    }

    public OrderFulfillmentSteps(OrderFulfillmentPage orderFulfillmentPage) {
        this. orderFulfillmentPage = orderFulfillmentPage;
    }

    public OrderCardPopUpPage createParcelWithFirstItemInIt(){
        orderFulfillmentPage.checkProducts(1);
        orderFulfillmentPage.clickCreateParcelButton();
        return orderFulfillmentPage.close();
    }

}
