package statystech.aqaframework.steps.UiSteps;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.PageObjects.OrderCardPopUpPage;
import statystech.aqaframework.steps.Steps;

public class OrderCardPopUpSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderCardPopUpSteps.class);

    private WebDriver webDriver;

    private OrderCardPopUpPage orderCardPopUpPage;

//    public OrderCardPopUpSteps(OrderCardPopUpPage orderCardPopUpPage) {
//        this.orderCardPopUpPage = orderCardPopUpPage;
//    }

    public OrderCardPopUpSteps(WebDriver webDriver) {
        this.webDriver = webDriver;
        orderCardPopUpPage = new OrderCardPopUpPage(webDriver);
    }

}
