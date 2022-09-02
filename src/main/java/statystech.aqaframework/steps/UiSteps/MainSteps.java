package statystech.aqaframework.steps.UiSteps;

import lombok.Getter;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.PageObjects.OrderCardDetailsPopUp;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.UiTestContext;
import statystech.aqaframework.elements.OrderCard;
import statystech.aqaframework.steps.Steps;

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

    public MainSteps(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public MainSteps(WebDriver webDriver) {
        this.webDriver = webDriver;
        mainPage = new MainPage(webDriver);
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
        if (mainPage.getNewOrderCard(orderCardIndex).isCancellationRequested()) {
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
        for (OrderCard orderCard : mainPage.getOrderCards()) {
            if (orderCard.getIndex() == orderNumber)
                orderCard.click();
        }
        return new OrderCardDetailsPopUp(webDriver);
    }

    public void clickResetOrderCardAndConfirm(int number) {
        OrderCard orderCard = mainPage.getOrderCardsInProgress().get(number);
        orderCard.clickResetBtn();
        mainPage.getMessageMoveToNewOrder().confirm();
    }

}
