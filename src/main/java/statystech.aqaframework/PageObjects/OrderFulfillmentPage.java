package statystech.aqaframework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.*;

import java.util.List;

public class OrderFulfillmentPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(OrderFulfillmentPage.class);
    private final WebDriver webDriver;
    By btnCreateParcel = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/div/button");
    String checkBox = "//*[@id=\"root\"]/div[2]/div/div/div[2]/table/tbody/tr/td[1]/div/span[1]";
    By btnClose = By.xpath("//*[@id=\"root\"]/div[2]/header/div/div/div/div[2]/button[2]");
    String rowProduct1 = "//*[@id=\"root\"]/div[2]/div/div/div[2]/table/tbody/tr[";
    String rowProduct2 = "]"; ///td[3]";
    By btnSplit = By.xpath(".//span/button");
    By btnConfirm = By.xpath(".//td[3]/span/span[3]/button[2]");
    By checkbox = By.xpath(".//div/span[1]/input");
    By ddBatchNumber = By.xpath(".//*[@id=\"mui-component-select-batchInventoryId\"]");
    By ddBatchNumberOptions = By.xpath("//*[@id=\"menu-batchInventoryId\"]/div[3]/ul/li");
    String btnSaveBatchNumber = "]/td[2]/div/div[2]/span/button";
    By productsRows = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/table/tbody/tr");
    By parcels = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[1]/div");
    By parcelDelete = By.xpath(".//button");
    By btnShipExternally = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/div/div[2]/button");
    By btnPrintPackingSlip = By.xpath("//*[@id=\"root\"]/div[2]/div/div/div[2]/div/div[1]/button");

    By popUpShipmentInfo = By.xpath("/html/body/div[2]/div[3]");
    By msgPartiallyShipped = By.xpath("//*[contains(text(), \"Partially Shipped\")]");
    By btnDeleteCompletedParcel = By.xpath("//button[contains(text(), \"Delete\")]");

    By parcelCompleteCheckmark = By.xpath("//*[@data-testid='CheckCircleIcon']");
    By orderStatus = By.xpath("//*[@id=\"root\"]/div[2]/header/div/div/div/div[1]/div[1]/span");

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

    public void clickDeleteCompleteParcel() {
        new Button(webDriver, btnDeleteCompletedParcel).click();
    }

    public ShipmentInformationPopUp getPopUpShipmentInfo() {
        return new ShipmentInformationPopUp(webDriver, popUpShipmentInfo);
    }

    public boolean isPartiallyShippedLabelVisible() {
        Element element = new Element(webDriver, msgPartiallyShipped);
        element.waitForElementToLoad();
        return element.isVisible();
    }

    public boolean isParcelCompleteCheckmarkVisible(int number) {
        boolean isVisible = false;
        try {
            isVisible = getParcelsElements().get(number-1).findElement(parcelCompleteCheckmark).isDisplayed();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return isVisible;
    }

    public Button getBtnPrintPackingSlip() {
        return new Button(webDriver, btnPrintPackingSlip);
    }

    public String getOrderStatus() {
        return new Button(webDriver, orderStatus).getText();
    }

    private By getRowProductLocator(int rowNumber) {
        return By.xpath(rowProduct1 + rowNumber + rowProduct2);
    }

    private WebElement getRowElement(int rowNumber) {
        return webDriver.findElement(getRowProductLocator(rowNumber));
    }

    public Button getShipExternallyButton() {
        return new Button(webDriver, btnShipExternally);
    }

    public boolean isRowElementEnabled(int rowNumber) {
        return !new Element(webDriver, (getRowProductLocator(rowNumber))).isRowDisabled();
    }

    public List<WebElement> getProductsElements() {
        waitForJStoLoad();
        return webDriver.findElements(productsRows);
    }

    public List<WebElement> getParcelsElements() {
        waitForJStoLoad();
        return webDriver.findElements(parcels);
    }

    public Button getParcelsElement(int index) {
        waitForJStoLoad();
        return new Button(webDriver, getParcelsElements().get(index-1));
    }

    public boolean clickFirstParcelElement() {
        WebElement webElement = getParcelsElements().get(0);
        new Button(webDriver, webElement).click();
        delay(1000);
        return webElement.getAttribute("class").contains(" ParcelsList_isActive__");
    }

    public boolean unclickFirstParcelElement() {
        WebElement webElement = getParcelsElements().get(0);
        webElement.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return !webElement.getAttribute("class").contains(" ParcelsList_isActive__");
    }

    public void deleteFirstParcelElement() {
        WebElement webElement = getParcelsElements().get(0).findElement(parcelDelete);
        webElement.click();
    }

    public OrderFulfillmentPage hoverProductRowAndClickSplit(int rowNumber) {
        Element element = new Element(webDriver, getRowProductLocator(rowNumber));
        element.waitForElementToLoad();
        element.hover();
        element.hoverAndClickRelatedElement(btnSplit);
        waitForJStoLoad();
        return this;
    }

    public OrderFulfillmentPage confirmSplit(int rowNumber) {
        waitForJStoLoad();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webDriver.findElement(getRowProductLocator(rowNumber)).findElement(btnConfirm).click();
        waitForJStoLoad();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public OrderFulfillmentPage expandBatchNumberDropDown(int rowNumber) {
        waitForJStoLoad();
        WebElement webElement = webDriver.findElement(getRowProductLocator(rowNumber)).findElement(ddBatchNumber);
        waitForElementToLoad(webElement);
        webElement.click();
        return this;
    }

    public List<WebElement> getBatchNumberOptions() {
        waitForJStoLoad();
        return webDriver.findElements(ddBatchNumberOptions);
    }

    public OrderFulfillmentPage clickCreateParcelButton() {
        Button button = new Button(webDriver, btnCreateParcel);
        button.click();
        waitForJStoLoad();
        waitForElementToBeNotClickable(btnCreateParcel);
        return this;
    }

    public OrderCardDetailsPopUp close() {
        new Button(webDriver, btnClose).click();
        waitForElementToDisappear(btnClose);
        waitForJStoLoad();
        return new OrderCardDetailsPopUp(webDriver);
    }

    public OrderFulfillmentPage selectProduct(int rowNumber) {
        webDriver.findElement(getRowProductLocator(rowNumber)).findElement(checkbox).click();
        return this;
    }

    public OrderFulfillmentPage checkProduct(int productNumberAtTheList) {
        new CheckBox(webDriver, By.xpath("(" + checkBox + ")" + "[" + productNumberAtTheList + "]")).check();
        return this;
    }

    public OrderFulfillmentPage checkProducts() {
        List<WebElement> webElements = webDriver.findElements(By.xpath(checkBox));
        for (WebElement webElement : webElements) {
            new CheckBox(webDriver, webElement).check();
        }
        return this;
    }

    public OrderFulfillmentPage clickSaveBatchNumber(int rowNumber) {
        try {
            new Button(webDriver, By.xpath(rowProduct1 + rowNumber + btnSaveBatchNumber)).click();
            waitForJStoLoad();
        } catch (Exception e) {
            delay(1000);
            new Button(webDriver, By.xpath(rowProduct1 + rowNumber + btnSaveBatchNumber)).click();
            waitForJStoLoad();
        }
        return this;
    }
}