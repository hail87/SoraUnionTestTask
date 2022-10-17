package statystech.aqaframework.steps.UiSteps;

import lombok.Getter;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.PageObjects.OrderCardDetailsPopUp;
import statystech.aqaframework.PageObjects.OrderFulfillmentPage;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.elements.OrderCard;
import statystech.aqaframework.steps.Steps;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Getter
public class MainSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(MainSteps.class);

    private WebDriver webDriver;

    private MainPage mainPage;

    public MainSteps(MainPage mainPage, TestInfo testInfo) {
        this.mainPage = mainPage;
        webDriver = Context.getTestContext(testInfo.getTestMethod().get().getName(), UiTestContext.class).getWebDriver();
    }

    public MainPage setDateFrom(String date) {
        mainPage.setDateFrom(date);
        mainPage.clickApplyButton();
        return mainPage;
    }

    public MainPage chooseDateFromFirst() {
        mainPage.clickDateFromCalendarButton();
        mainPage.chooseDateAtTheCalendar(" 1, 2022");
        mainPage.waitCalendarToDisappear();
        mainPage.clickApplyButton();
        return mainPage;
    }

    public MainPage setDateTo(String date) {
        mainPage.setDateTo(date);
        mainPage.clickApplyButton();
        return mainPage;
    }

    public MainPage chooseDateTo28() {
        mainPage.clickDateToCalendarButton();
        mainPage.chooseDateAtTheCalendar(" 28, 2022");
        mainPage.clickApplyButton();
        return mainPage;
    }

    public MainPage chooseWarehouse(String warehouseName) {
        mainPage.expandWarehouseDropdown();
        mainPage.selectWarehouseDropdownOption(warehouseName);
        mainPage.waitForJStoLoad();
        return mainPage;
    }

    public MainPage chooseDestination(String destination) {
        mainPage.expandDestination();
        mainPage.selectDropdownOptionByText(destination);
        clickApplyButton();
        mainPage.waitForJStoLoad();
        return mainPage;
    }

    public MainPage chooseOrderStatus(String orderStatus) {
        mainPage.expandOrderStatus();
        mainPage.selectDropdownOptionByText(orderStatus);
        clickApplyButton();
        mainPage.waitForActiveOrdersToUpdate();
        mainPage.waitForJStoLoad();
        return mainPage;
    }

    public MainPage cancelOrderStatusChoice() {
        mainPage.clickCancelOrderStatusChoice();
        mainPage.clickApplyButton();
        mainPage.waitForActiveOrdersToUpdate();
        mainPage.waitForJStoLoad();
        logger.info("cancelOrderStatusChoice");
        return mainPage;
    }

    public MainPage checkApplyButtonDisabled() {
        assertFalse(mainPage.isApplyButtonEnabled(), "Apply Button is Enabled, but shouldn't be!");
        return mainPage;
    }

    public MainPage clickApplyButton() {
        assertTrue(mainPage.isApplyButtonEnabled(), "Apply Button is Disabled, but shouldn't be!");
        mainPage.clickApplyButton();
        checkApplyButtonDisabled();
        logger.info("clickApplyButton\n");
        return mainPage;
    }

    public String checkPrintPageAndReturnToMain() {
        mainPage.waitForJStoLoad();
        mainPage.delay(1000);
        mainPage.clickFirstOrderNumber();
        mainPage.clickPrintOrders();
        mainPage.switchTab(2);
        String errorMessage = mainPage.waitForTabsSize(2);
        mainPage.switchTab(1);
        return errorMessage;
    }

    public void putOnHold() {
        mainPage.clickPutOnHold();
        mainPage.chooseTodayPutOnHoldCalendar();
        mainPage.confirmPutOnHold();
        assertFalse(mainPage.getOnHoldDate().equalsIgnoreCase("onHoldDate element is not visible"));
    }

    public void cancelHold() {
        mainPage.clickCancelHold();
        String errorMessage = mainPage.verifyOnHoldDateIsDisappear();
        assertTrue(errorMessage.isEmpty(), errorMessage);
    }

    public String search(String text) {
        mainPage.search(text);
        mainPage.waitForFirstOrderNumberToLoad();
        delay(500);
        String actualOrderNumber = mainPage.getFirstOrderNumber().substring(1);
        if (!text.equalsIgnoreCase(actualOrderNumber))
            return String.format("Actual order number '%s' and expected '%s' isn't the same", actualOrderNumber, text);
        return "";
    }

    public MainPage cancelSearch() {
        mainPage.cancelSearch();
        mainPage.waitForFirstOrderNumberToLoad();
        return mainPage;
    }

    public String getActiveOrdersAfterUpdate() {
        mainPage.waitForActiveOrdersToUpdate();
        return mainPage.getActiveOrders();
    }

    public void clickAndVerifyOrdersOnHold() {
        mainPage.clickShowOnlyOrdersOnHoldOrAll();
        mainPage.waitForJStoLoad();
        mainPage.getNewOrderCards().stream().forEach(orderCard -> assertTrue(!orderCard.getExpirationDate().isEmpty(), "\nOrder Card has no Expiration date, but should\n"));
    }

    public String requestCancellation(int orderCardIndex, String reason) {
        mainPage.clickRequestCancellation(orderCardIndex);
        mainPage.fillInCancellationReason(reason);
        mainPage.submitCancellationReason();
        if (mainPage.getOrderCards().get(orderCardIndex - 1).isCancellationRequested()) {
            return "";
        } else {
            return "\nFor OrderCard Cancellation is NOT Requested, but should\n";
        }
    }

    public String requestCancellation(String reason) {
        int i = 1;
        for (OrderCard orderCard : mainPage.getNewOrderCards()) {
            if (!orderCard.isCancellationRequested()) {
                break;
            } else {
                i++;
            }
        }
        return requestCancellation(i, reason);
    }

    public OrderCardDetailsPopUp clickNewOrderCard(int number) {
        mainPage.getNewOrderCards().get(number).click();
        return new OrderCardDetailsPopUp(webDriver);
    }

    public OrderCardDetailsPopUp clickOrderCardInProgress(int number) {
        --number;
        if (number < 0) {
            logger.error("Card number should be >= 1");
            return new OrderCardDetailsPopUp(webDriver);
        }

        mainPage.getOrderCardsInProgress().get(number).click();
        return new OrderCardDetailsPopUp(webDriver);
    }

    public OrderCardDetailsPopUp clickOrderCard(int orderNumber) {
        OrderCard orderCard = findOrderCard(orderNumber);
        int i = 0;
        while (orderCard == null & i < 30) {
            delay(1000);
            mainPage.refreshPage();
            orderCard = findOrderCard(orderNumber);
            i++;
        }
        orderCard.click();
        return new OrderCardDetailsPopUp(webDriver);
    }

    private boolean closeErrorMessage(String errorMessage) {
        boolean isShown = mainPage.isTextShownAtThePage(errorMessage);
        if (isShown) {
            mainPage.clickOkButton();
            mainPage.waitForJStoLoad();
            mainPage.refreshPage();
        }
        return isShown;
    }

    public boolean searchOrder(int orderNumber) {
        logger.info("Waiting for search order to appear : " + orderNumber);
        int i = 0;
        boolean orderFound = false;
        while (!orderFound && i < 30) {
            if (mainPage.isTextShownAtThePage("User does not have permission. Please contact support at")) {
                mainPage.clickOkButton();
                mainPage.waitForJStoLoad();
                mainPage.refreshPage();
                search(String.valueOf(orderNumber));
            } else {
                search(String.valueOf(orderNumber));
            }
            mainPage.waitForJStoLoad();
            if (mainPage.isTextShownAtThePage("User does not have permission. Please contact support at")) {
                mainPage.clickOkButton();
                mainPage.waitForJStoLoad();
                mainPage.refreshPage();
                mainPage.delay(1000);
                i++;
                orderFound = search(String.valueOf(orderNumber)).isEmpty();
            } else {
                orderFound = true;
            }
            mainPage.waitForJStoLoad();
        }
        return waitForNewOrderCardToBeProcessed(orderNumber);
    }

    public boolean waitForNewOrderCardToBeProcessed(int orderNumber) {
        logger.info("Waiting for order to appear : " + orderNumber);
        OrderCard orderCard = findOrderCard(orderNumber);
        int i = 0;
        while (orderCard == null && i < 30) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainPage.refreshPage();
            orderCard = findOrderCard(orderNumber);
            i++;
        }
        if (orderCard != null) {
            logger.info("Order found : " + orderNumber);
        } else {
            logger.error("Order was NOT found : " + orderNumber);
        }
        return orderCard != null;
    }

    public OrderCard waitForNewOrderCard(int orderNumber) {
        OrderCard orderCard = findOrderCard(orderNumber);
        int i = 0;
        while (orderCard == null && i < 30) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainPage.refreshPage();
            orderCard = findOrderCard(orderNumber);
            i++;
        }
        return orderCard;
    }

    private OrderCard findOrderCard(int orderNumber) {
        List<OrderCard> orderCards = mainPage.getOrderCards();
        int i = 0;
        OrderCard orderCard;
        while (i < orderCards.size()) {
            orderCard = orderCards.get(i);
            if (orderCard.getIndex() == orderNumber) {
                return orderCard;
            }
            i++;
        }
        return null;
    }

    public void createParcelWithAllItems(int orderNumber) {
        OrderCardDetailsPopUp orderCardDetailsPopUp = clickOrderCard(orderNumber);
        OrderFulfillmentPage orderFulfillmentPage = orderCardDetailsPopUp.startOrderFulfillment();
        new OrderFulfillmentSteps(orderFulfillmentPage).createParcelWithAllItems();
    }

    public void clickResetOrderCardAndConfirm(int orderPosition) {
        OrderCard orderCard = mainPage.getOrderCardsInProgress().get(orderPosition);
        orderCard.clickResetBtn();
        mainPage.getMessageMoveToNewOrder().confirm();
    }

    public void resetOrder(int orderNumber) {
        OrderCard orderCard = waitForNewOrderCard(orderNumber);
        orderCard.clickResetBtn();
        mainPage.getMessageMoveToNewOrder().confirm();
    }

    public Integer getActiveNewOrdersAfterRefresh() {
        mainPage.delay(1500);
        mainPage.refreshPage();
        mainPage.waitForJStoLoad();
        return mainPage.getActiveNewOrders();
    }

    public void shipOrderToInProgress(int orderNumber) {
        OrderCardDetailsPopUp orderCardDetailsPopUp = clickOrderCard(orderNumber);
        OrderFulfillmentPage orderFulfillmentPage = orderCardDetailsPopUp.startOrderFulfillment();
        OrderFulfillmentSteps orderFulfillmentSteps = new OrderFulfillmentSteps(orderFulfillmentPage);
        orderFulfillmentSteps.createParcel(1, 1);
        orderFulfillmentSteps.closeOrderFulfillmentPage();
        orderCardDetailsPopUp.close();
    }

    public String shipOrderWithAllParcels(int orderNumber) {
        OrderCardDetailsPopUp orderCardDetailsPopUp = clickOrderCard(orderNumber);
        OrderFulfillmentSteps orderFulfillmentSteps = new OrderFulfillmentSteps(orderCardDetailsPopUp.startOrderFulfillment());
        orderFulfillmentSteps.createParcelWithAllItems();
        StringBuilder errorMessage = new StringBuilder(orderFulfillmentSteps.shipParcelExternallyWithAllFieldsFilled(1));

        orderCardDetailsPopUp = orderFulfillmentSteps.closeOrderFulfillmentPage();

        delay(1000);
        errorMessage.append(orderFulfillmentSteps.verifyExpectedResults(
                orderCardDetailsPopUp.getOrderStatus(), "Shipped"));
        errorMessage.append(orderFulfillmentSteps.verifyExpectedResults(
                orderCardDetailsPopUp.getStartOrderFulfillmentButtonLabel(), "Order fulfillment details"));

        orderCardDetailsPopUp.close();

        return errorMessage.toString();
    }

    public void clickBottomMessageIfVisible() {
        mainPage.waitForJStoLoad();
        WebElement webElement = mainPage.getBottomNotificationElement();
        while (webElement.isDisplayed()) {
            webElement.click();
            delay(500);
        }
    }

}
