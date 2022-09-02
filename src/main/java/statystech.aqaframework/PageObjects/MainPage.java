package statystech.aqaframework.PageObjects;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final WebDriver webDriver;
    By ddWarehousesButton = By.xpath("//*[@id=\"root\"]/header/div/div[1]/div[2]/div/div/div");
    By ddOptions = By.xpath("//*[@id=\"menu-\"]/div[3]/ul");
    //By ddWarehousesButton = By.xpath("(//div[@role=\"button\"])[1]");
    By ddDestinationsButton = By.xpath("(//div[@role=\"button\"])[2]");
    By ddOrderStatusButton = By.xpath("(//div[@role=\"button\"])[3]");
    By btnCancelOrderStatusChoice = By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div/div[2]/div/div[2]/button");
    By tabActive = By.id("0");//By.xpath("//button[@id=\"0\"]");
    By txtActiveNewOrders = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div[1]/p[1]/span");
    By txtActiveInProgressOrders = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[2]/div[1]/p[1]/span");
    By tabShipped = By.id("1");
    By btnApply = By.xpath("//button[text()=\"Apply\"]");
    By dateFrom = By.xpath("(//input[@placeholder = 'mm/dd/yyyy'])[1]");
    By calendar = By.xpath("(//div[contains(@class, 'MuiCalendarPicker')])[1]");
    By dateTo = By.xpath("(//input[@placeholder = 'mm/dd/yyyy'])[2]");
    By btnCalendarFrom = By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div/div[3]/div/div[1]/div/div/button");
    By btnCalendarTo = By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div/div[3]/div/div[2]/div/div/button");
    By btnTodayDay = By.xpath("//button[contains(@class,\"today\")]");
    By btnFirstOrderNumber = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div[2]/div/div/div/div[2]/div/div/p");
    By btnSelectAllNewOrders = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div[1]/p[2]");
    By btnSelectAllInProgress = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[2]/div[1]/p[2]");
    By btnShowOnlyOrdersOnHold = By.xpath("//*[@id=\"root\"]/div[3]/div/p");

    By btnPrintOrders = By.xpath("//*[@id=\"root\"]/div[3]/div/div/div[1]/p");
    By btnPutOnHold = By.xpath("//*[@id=\"root\"]/div[3]/div/div/div[3]/p");
    By btnCancelHold = By.xpath("//*[@id=\"root\"]/div[3]/div/div/div[4]/p");
    By btnUncheckAll = By.xpath("//*[@id=\"root\"]/div[3]/div/div/p");

    By popupPutOnHold = By.xpath("/html/body/div[2]/div[3]/div/div");
    By btnCalendarPutOnHold = By.xpath("/html/body/div[2]/div[3]/div/div/div[2]/div/div/div[1]/div/div/button");
    By btnConfirmPutOnHold = By.xpath("/html/body/div[2]/div[3]/div/div/div[2]/div/div/div[3]/button[2]");

    By onHoldDate = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div[2]/div/div/div/div[2]/div/div/p[2]/span");

    @Getter
    By popupCancelOrderBy = By.xpath("/html/body/div[2]/div[3]/div/div");
    By popupCancelOrderTxtCancellationReason = By.xpath("/html/body/div[2]/div[3]/div/div/div[2]/div/div[1]/div/textarea[1]");
    By popupCancelOrderBtnSubmitRequest = By.xpath("/html/body/div[2]/div[3]/div/div/div[2]/div/div[2]/button");

    By inputSearch = By.xpath("//input[contains(@placeholder, 'Search by order number')]");
    By btnCancelSearch = By.xpath("//*[@id=\"root\"]/header/div/div[2]/div[1]/div/div/div/div/div[2]/button");

    By orderCardsBy = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[1]/div");
    String orderCardsLctr = "//*[@id=\"root\"]/div[4]/div/div/div[1]/div";

    By orderCardsInProgressBy = By.xpath("//*[@id=\"root\"]/div[4]/div/div/div[2]/div");
    String orderCardsInProgressLctr = "//*[@id=\"root\"]/div[4]/div/div/div[2]/div";
    By orderCards = By.xpath("//*[@id=\"root\"]/div[4]/div/div//div[contains(@class, 'MuiCardContent-root Order_orderContent')]");

    By msgMoveToNewOrder = By.xpath("/html/body/div[2]/div[3]/div/div");


    Button applyButton;

    @Getter
    String url;

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
        waitForJStoLoad();
        waitForElementToLoad(ddWarehousesButton);
        url = webDriver.getCurrentUrl();
        if (!webDriver.findElement(ddWarehousesButton).isDisplayed()) {
            logger.error("This is not the MAIN page");
            throw new IllegalStateException("This is not the MAIN page");
        }
    }

    public MainPage search(String text) {
        new TextField(webDriver, inputSearch).fillInAndSubmit(text);
        return this;
    }

    public MainPage cancelSearch() {
        new Button(webDriver, btnCancelSearch).click();
        return this;
    }

    public MainPage clickDateFromCalendarButton() {
        new Button(webDriver, btnCalendarFrom).click();
        waitForElementToLoad(calendar);
        return this;
    }

    public MainPage waitCalendarToDisappear() {
        waitForElementToDisappear(webDriver.findElement(calendar));
        return this;
    }

    public MainPage clickDateToCalendarButton() {
        new Button(webDriver, btnCalendarTo).click();
        waitForElementToLoad(calendar);
        return this;
    }

    public MainPage selectDayPriorToToday(int days) {
        int todayDate = Integer.parseInt(webDriver.findElement(btnTodayDay).getAttribute("aria-label").substring(4, 5).trim());
        int diff = todayDate - days;
        if (diff > 0) {
            webDriver.findElement(By.xpath("//button[contains(@aria-label, '" + diff + "')]")).click();
            clickApplyButton();
        }
        logger.error("Choose less prior days");
        return this;
    }

    public MainPage setDateFrom(String date) {
        new TextField(webDriver, dateFrom).fillInAndSubmit(date);
        return this;
    }

    public MainPage chooseDateAtTheCalendar(String date) {
        webDriver.findElement(calendar).findElement(By.xpath(".//button[contains(@aria-label, '" + date + "')]")).click();
        return this;
    }

    public MainPage setDateTo(String date) {
        webDriver.findElement(dateTo).sendKeys(date);
        return this;
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

    public MainPage expandOrderStatus() {
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

    public MainPage clickShippedTab() {
        new Button(webDriver, tabShipped).click();
        return this;
    }

    public MainPage clickActiveTab() {
        new Button(webDriver, tabActive).click();
        return this;
    }

    public boolean waitForActiveOrdersToUpdate() {
        waitForElementToLoad(tabActive);
        return waitForElementToUpdate(webDriver, orderCardsBy);
    }

    public String getActiveOrders() {
        waitForElementToLoad(tabActive);
        String buttonText = webDriver.findElement(tabActive).getText();
        int i = 0;
        while (buttonText.length() <= 7 & i <= 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buttonText = webDriver.findElement(tabActive).getText();
            i++;
        }
        logger.info("Active orders : " + buttonText);
        return buttonText;
    }

    public Integer getActiveNewOrders() {
        waitForElementToLoad(txtActiveNewOrders);
        waitForJStoLoad();
        String buttonText = webDriver.findElement(txtActiveNewOrders).getText();
        Integer activeNewOrders = Integer.valueOf(buttonText.replaceAll("\\D+", ""));
        logger.info("Active New orders : " + activeNewOrders);
        return activeNewOrders;
    }

    public Integer getActiveInProgressOrders() {
        waitForElementToLoad(tabActive);
        String buttonText = webDriver.findElement(txtActiveInProgressOrders).getText();
        Integer activeNewOrders = Integer.valueOf(buttonText.replaceAll("\\D+", ""));
        logger.info("Active New orders In progress: " + activeNewOrders);
        return activeNewOrders;
    }

    public String getShippedOrders() {
        waitForElementToLoad(tabShipped);
        String buttonText = webDriver.findElement(tabShipped).getText();
        int i = 0;
        while (buttonText.length() <= 7 & i <= 3) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buttonText = webDriver.findElement(tabShipped).getText();
            i++;
        }
        logger.info("Shipped orders : " + buttonText);
        return buttonText;
    }

    public MainPage clickCancelOrderStatusChoice() {
        new Button(webDriver, btnCancelOrderStatusChoice).click();
        logger.info("cancelOrderStatusChoice button clicked");
        return this;
    }

    public boolean isApplyButtonEnabled() {
        if (applyButton == null)
            applyButton = new Button(webDriver, btnApply);
        return applyButton.isDisabled();
    }

    public void clickApplyButton() {
        if (applyButton == null)
            applyButton = new Button(webDriver, btnApply);
        applyButton.click();
    }

    public void waitForFirstOrderNumberToLoad() {
        waitForElementToLoad(btnFirstOrderNumber);
    }

    public void clickFirstOrderNumber() {
        new Button(webDriver, btnFirstOrderNumber).click();
    }

    public String getFirstOrderNumber() {
        return new Button(webDriver, btnFirstOrderNumber).getText();
    }

    public void clickPrintOrders() {
        new Button(webDriver, btnPrintOrders).click();
    }

    public void clickCancelHold() {
        waitForElementToBeClickable(btnCancelHold);
        new Button(webDriver, btnCancelHold).click();
    }

    public void clickRequestCancellation(int orderCardIndex) {
        getNewOrderCard(orderCardIndex).requestCancellation();
    }

    public void clickPutOnHold() {
        new Button(webDriver, btnPutOnHold).click();
        waitForElementToLoad(popupPutOnHold);
    }

    public void selectAllNewOrders() {
        new Button(webDriver, btnSelectAllNewOrders).click();
        waitForElementToLoad(btnUncheckAll);
    }

    public void selectAllInProgress() {
        new Button(webDriver, btnSelectAllInProgress).click();
        waitForElementToLoad(btnUncheckAll);
    }

    public void clickShowOnlyOrdersOnHoldOrAll() {
        new Button(webDriver, btnShowOnlyOrdersOnHold).click();
        waitForJStoLoad();
    }

    public void clickUncheckAll() {
        Button button = new Button(webDriver, btnUncheckAll);
        button.click();
        waitForElementToDisappear(button.getWebElement());
    }

    public void chooseTodayPutOnHoldCalendar() {
        new Button(webDriver, btnCalendarPutOnHold).click();
        waitForElementToLoad(calendar);
        new Button(webDriver, btnTodayDay).click();
        waitCalendarToDisappear();
    }

    public void confirmPutOnHold() {
        new Button(webDriver, btnConfirmPutOnHold).click();
    }

    public String getOnHoldDate() {
        waitForElementToLoad(onHoldDate);
        WebElement webElement = webDriver.findElement(onHoldDate);
        if (!webElement.isDisplayed()) {
            return "onHoldDate element is not visible";
        }
        return webElement.getText();
    }

    public String verifyOnHoldDateIsDisappear() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return verifyElementIsDisappear(onHoldDate);
    }

    public List<OrderCard> getNewOrderCards() {
        int cardsQuantity = webDriver.findElements(orderCardsBy).size() - 1;
        int startPosition = 2;
        ArrayList<OrderCard> orderCards = new ArrayList<>();
        while (cardsQuantity > 0) {
            orderCards.add(new OrderCard(webDriver, By.xpath(orderCardsLctr + "[" + startPosition + "]")));
            startPosition++;
            cardsQuantity--;
        }
        return orderCards;
    }

    public List<OrderCard> getOrderCardsInProgress() {
        waitForJStoLoad();
        int cardsQuantity = webDriver.findElements(orderCardsInProgressBy).size() - 1;
        if (cardsQuantity == 0) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cardsQuantity = webDriver.findElements(orderCardsInProgressBy).size() - 1;
        }
        int startPosition = 2;
        ArrayList<OrderCard> orderCards = new ArrayList<>();
        while (cardsQuantity > 0) {
            orderCards.add(new OrderCard(webDriver, By.xpath(orderCardsInProgressLctr + "[" + startPosition + "]")));
            startPosition++;
            cardsQuantity--;
        }

        if (orderCards.size() == 0)
            return getOrderCardsInProgress();
        return orderCards;
    }

    public List<OrderCard> getOrderCards() {
        waitForJStoLoad();
        List<WebElement> webElements = webDriver.findElements(orderCards);
        int cardsQuantity = webElements.size();
        int i = 30;
        while (cardsQuantity == 0 && i > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webElements = webDriver.findElements(orderCards);
            cardsQuantity = webElements.size();
        }
        return webElements.stream().map(p -> new OrderCard(p, webDriver)).collect(Collectors.toList());
    }

    public OrderCard getNewOrderCard(int index) {
        return getNewOrderCards().get(index - 1);
    }

    public void fillInCancellationReason(String reason) {
        waitForElementToLoad(popupCancelOrderBy);
        new TextField(webDriver, popupCancelOrderTxtCancellationReason).fillIn(reason);
    }

    public void submitCancellationReason() {
        waitForElementToLoad(popupCancelOrderBy);
        new Button(webDriver, popupCancelOrderBtnSubmitRequest).click();
        waitForElementToDisappear(popupCancelOrderBy);
        waitForJStoLoad();
    }

    public Message getMessageMoveToNewOrder() {
        return new Message(webDriver, msgMoveToNewOrder);
    }

}
