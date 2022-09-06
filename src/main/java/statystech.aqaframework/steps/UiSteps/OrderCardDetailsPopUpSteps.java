package statystech.aqaframework.steps.UiSteps;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.PageObjects.OrderCardDetailsPopUp;
import statystech.aqaframework.steps.Steps;

public class OrderCardDetailsPopUpSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderCardDetailsPopUpSteps.class);

    private OrderCardDetailsPopUp orderCardDetailsPopUp;

    public OrderCardDetailsPopUpSteps(OrderCardDetailsPopUp orderCardDetailsPopUp) {
        this.orderCardDetailsPopUp = orderCardDetailsPopUp;
    }

    public MainPage putOnHold() {
        orderCardDetailsPopUp.clickOptionButton();
        return orderCardDetailsPopUp.clickPutOnHold();
    }

    public void moveToNewOrder(boolean confirm) {
        orderCardDetailsPopUp.clickOptionButton();
        orderCardDetailsPopUp.clickMoveToNew();
        if (confirm) {
            orderCardDetailsPopUp.clickConfirmMoveToNew();
        } else {
            orderCardDetailsPopUp.clickCancelMoveToNew();
        }
    }

    public void continueOrderFulfillment() {
        orderCardDetailsPopUp.startOrderFulfillment();
    }

}
