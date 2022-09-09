package statystech.aqaframework.steps.UiSteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.PageObjects.OrderCardDetailsPopUp;
import statystech.aqaframework.PageObjects.OrderFulfillmentPage;
import statystech.aqaframework.elements.Button;
import statystech.aqaframework.elements.ShipmentInformationPopUp;
import statystech.aqaframework.steps.Steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderFulfillmentSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderFulfillmentSteps.class);

    private OrderFulfillmentPage orderFulfillmentPage;

    public OrderFulfillmentSteps(OrderFulfillmentPage orderFulfillmentPage) {
        this.orderFulfillmentPage = orderFulfillmentPage;
    }

    @Deprecated
    public OrderCardDetailsPopUp createParcelWithFirstItem() {
        createParcel(1, 1);
        return orderFulfillmentPage.close();
    }

    public OrderCardDetailsPopUp closeOrderFulfillmentPage() {
        return orderFulfillmentPage.close();
    }

    public String splitAndConfirm(int rowNumber) {
        String errorMessage = "";

        int productsQuantity = getProductsQuantity();
        logger.info("\nProducts quantity at the page before split : '" + productsQuantity + "'\n");

        orderFulfillmentPage.hoverProductRowAndClickSplit(rowNumber);
        logger.info("\nHover at product line " + rowNumber + " and click split completed\n");
        orderFulfillmentPage.confirmSplit(rowNumber);

        int updatedProductsQuality = getProductsQuantity();
        logger.info("\nProducts quantity at the page after split : '" + updatedProductsQuality + "'\n");
        int i = 0;
        while (updatedProductsQuality == productsQuantity && i < 3) {
            i++;
            updatedProductsQuality = getProductsQuantity();
        }

        if (updatedProductsQuality == productsQuantity) {
            errorMessage = "\nProducts quantity didn't changed after split";
        }

        return errorMessage;
    }

    public int getProductsQuantity() {
        orderFulfillmentPage.waitForJStoLoad();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return orderFulfillmentPage.getProductsElements().size();
    }

    public OrderFulfillmentPage createParcel(int row, int ddOptionPosition) {
        orderFulfillmentPage.checkProduct(row);
        chooseBatchNumberFromDd(row, ddOptionPosition);
        orderFulfillmentPage.clickSaveBatchNumber(row);

        int parcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        orderFulfillmentPage.clickCreateParcelButton();
        int updatedParcelQuantity = orderFulfillmentPage.getParcelsElements().size();

        if (updatedParcelQuantity == parcelQuantity) {
            logger.error("Parcels quantity didn't changed after creating new one");
        }

        if (orderFulfillmentPage.isRowElementEnabled(row)) {
            logger.error("\nProduct line is still enable, but shouldn't be!\n");
        }

        return orderFulfillmentPage;
    }

    public OrderCardDetailsPopUp createParcelWithAllItems() {
        orderFulfillmentPage.checkProducts();
        selectAndSaveAllBatchNumbers();
        int parcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        orderFulfillmentPage.clickCreateParcelButton();
        int updatedParcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        if (updatedParcelQuantity == parcelQuantity) {
            logger.error("Parcels quantity didn't changed after creating new one");
        }

        return orderFulfillmentPage.close();
    }

    public void selectAndSaveAllBatchNumbers() {
        int rows = getProductsQuantity();
        int i = 1;
        while (i <= rows) {
            chooseBatchNumberFromDd(i, 1);
            orderFulfillmentPage.clickSaveBatchNumber(i);
            i++;
        }
    }

    public OrderFulfillmentPage deleteFirstParcel() {
        int parcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        logger.info("\nParcels on the page : " + parcelQuantity);
        orderFulfillmentPage.clickFirstParcelElement();
        orderFulfillmentPage.deleteFirstParcelElement();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (orderFulfillmentPage.getParcelsElements().size() == parcelQuantity) {
            logger.error("Parcels quantity didn't changed after deleting one");
        }

        return orderFulfillmentPage;
    }

    public String deleteCompletedParcel() {
        StringBuilder errorMessage = new StringBuilder();
        int parcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        logger.info("\nParcels on the page : " + parcelQuantity);
        if (!orderFulfillmentPage.clickFirstParcelElement()) {
            orderFulfillmentPage.clickFirstParcelElement();
        }
        orderFulfillmentPage.deleteFirstParcelElement();
        orderFulfillmentPage.clickDeleteCompleteParcel();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (orderFulfillmentPage.getParcelsElements().size() == parcelQuantity) {
            logger.error("Parcels quantity didn't changed after deleting one");
            errorMessage.append("Parcels quantity didn't changed after deleting one");
        } else {
            logger.info("Parcels item been successfully deleted");
        }

        return errorMessage.toString();
    }

    public OrderFulfillmentPage shipParcelExternally(int number) {

        Button btnShipExternally = orderFulfillmentPage.getShipExternallyButton();
        if (btnShipExternally.getWebElement().isEnabled()) {
            logger.error("\n Button 'Ship externally' enabled, but shouldn't be!\n");
        }
        orderFulfillmentPage.getParcelsElements().get(number - 1).click();
        btnShipExternally.waitForElementToBeClickable();
        if (!btnShipExternally.getWebElement().isEnabled()) {
            logger.error("\n Button 'Ship externally' disabled, but shouldn't be!\n");
        }
        btnShipExternally.click();

        ShipmentInformationPopUp shipmentInformationPopUp = orderFulfillmentPage.getPopUpShipmentInfo();
        shipmentInformationPopUp.checkboxCheck();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shipmentInformationPopUp.clickCompleteFulfillment();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (btnShipExternally.isEnabled()) {
            logger.error("\n Button 'Ship externally' is still enabled, but shouldn't be!\n");
        }

        if (!orderFulfillmentPage.isPartiallyShippedLabelVisible()) {
            logger.error("\n Label 'Partially Shipped' is not visible, but should be!\n");
        }
        return orderFulfillmentPage;
    }

    private OrderFulfillmentPage chooseBatchNumberFromDd(int row, int ddOptionPosition) {
        orderFulfillmentPage.expandBatchNumberDropDown(row);
        orderFulfillmentPage.getBatchNumberOptions().get(ddOptionPosition - 1).click();
        return orderFulfillmentPage;
    }

}
