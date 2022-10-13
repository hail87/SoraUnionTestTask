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
        logger.info("Parcels quantity before creating new parcel : " + parcelQuantity);
        orderFulfillmentPage.clickCreateParcelButton();
        delay(2000);
        int updatedParcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        logger.info("Parcels quantity after creating new parcel : " + updatedParcelQuantity);
        if (updatedParcelQuantity == parcelQuantity) {
            logger.error("Parcels quantity didn't changed after creating new one");
        }

        if (orderFulfillmentPage.isRowElementEnabled(row)) {
            logger.error("\nProduct line is still enable, but shouldn't be!\n");
        }

        return orderFulfillmentPage;
    }

    public String createParcelWithAllItems() {
        String errorMessage = "";
        orderFulfillmentPage.checkProducts();
        delay(1000);
        selectAndSaveAllBatchNumbers();
        int parcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        orderFulfillmentPage.clickCreateParcelButton();
        delay(2000);
        int updatedParcelQuantity = orderFulfillmentPage.getParcelsElements().size();
        if (updatedParcelQuantity == parcelQuantity) {
            errorMessage = "Parcels quantity didn't changed after creating new one";
            logger.error("Parcels quantity didn't changed after creating new one");
        }

        return errorMessage;
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

    public OrderFulfillmentPage clickParcel(int index) {
        orderFulfillmentPage.getParcelsElement(index).click();
        return orderFulfillmentPage;
    }

    public String checkPrintPackingSlipEnabled(boolean enabled) {
        String errorMessage = "";
        if (enabled) {
            if (!orderFulfillmentPage.getBtnPrintPackingSlip().isEnabled())
            errorMessage = "\nPrint Packing Slip button is disabled, but shouldn't be!\n";
        } else {
            if (orderFulfillmentPage.getBtnPrintPackingSlip().isEnabled())
                errorMessage = "\nPrint Packing Slip button is enabled, but shouldn't be!\n";
        }
        return errorMessage;
    }

    public String clickPrintPackingSlipButton() {
        orderFulfillmentPage.getBtnPrintPackingSlip().click();
        orderFulfillmentPage.switchTab(2);
        String errorMessage = orderFulfillmentPage.waitForTabsSize(2);
        return errorMessage;
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

    public OrderFulfillmentPage shipParcelExternallyWithLocalPickup(int number) {

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
        delay(3000);
        shipmentInformationPopUp.clickCompleteFulfillment();
        delay(3000);
        if (btnShipExternally.isEnabled()) {
            logger.error("\n Button 'Ship externally' is still enabled, but shouldn't be!\n");
        }

        if (!orderFulfillmentPage.isPartiallyShippedLabelVisible()) {
            logger.error("\n Label 'Partially Shipped' is not visible, but should be!\n");
        }
        return orderFulfillmentPage;
    }

    public String shipParcelExternallyWithAllFieldsFilled(int number) {

        Button btnShipExternally = orderFulfillmentPage.getShipExternallyButton();
        if (btnShipExternally.getWebElement().isEnabled()) {
            logger.error("\n Button 'Ship externally' enabled, but shouldn't be!\n");
        }
        delay(1000);
        orderFulfillmentPage.getParcelsElement(number).click();
        btnShipExternally.waitForElementToBeClickable();
        if (!btnShipExternally.getWebElement().isEnabled()) {
            logger.error("\n Button 'Ship externally' disabled, but shouldn't be!\n");
        }
        btnShipExternally.click();

        ShipmentInformationPopUp shipmentInformationPopUp = orderFulfillmentPage.getPopUpShipmentInfo();
        shipmentInformationPopUp.fillTrackingNumber("123456");
        shipmentInformationPopUp.chooseShippingAccount(1);
        shipmentInformationPopUp.fillShippingCost("100");
        shipmentInformationPopUp.chooseCurrency("USD");

        shipmentInformationPopUp.clickCompleteFulfillment();

        delay(3000);

        if (btnShipExternally.isEnabled()) {
            logger.error("\n Button 'Ship externally' is still enabled, but shouldn't be!\n");
        }

        StringBuilder errorMessage = new StringBuilder();

        if(!orderFulfillmentPage.isParcelCompleteCheckmarkVisible(number))
            errorMessage.append("\n'Parcel Complete Checkmark' is not visible, but should be!\n");

        errorMessage.append(verifyExpectedResults(orderFulfillmentPage.getOrderStatus(), "Shipped"));

        return errorMessage.toString();
    }

    private OrderFulfillmentPage chooseBatchNumberFromDd(int row, int ddOptionPosition) {
        orderFulfillmentPage.expandBatchNumberDropDown(row);
        orderFulfillmentPage.getBatchNumberOptions().get(ddOptionPosition - 1).click();
        return orderFulfillmentPage;
    }

}
