package statystech.aqaframework.steps.UiSteps;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.steps.Steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Getter
public class MainSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(MainSteps.class);

    private WebDriver webDriver;

    private MainPage mainPage;

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
        return mainPage;
    }

    public MainPage chooseDestination(String destination) {
        mainPage.expandDestination();
        mainPage.selectDropdownOptionByText(destination);
        return mainPage;
    }

    public MainPage chooseOrderStatus(String orderStatus) {
        mainPage.expandorderStatus();
        mainPage.selectDropdownOptionByText(orderStatus);
        return mainPage;
    }

    public MainPage cancelOrderStatusChoice() {
        mainPage.clickCancelOrderStatusChoice();
        mainPage.clickApplyButton();
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
        assertTrue(mainPage.verifyOnHoldDateIsDisappear().isEmpty());
    }


}
