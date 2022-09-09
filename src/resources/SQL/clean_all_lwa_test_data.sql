delete from lwa_sandbox.trackingEvent
where trackingEventID > 0;

delete from lwa_sandbox.tracking
where trackingID > 0;

delete from lwa_sandbox.inventoryTransaction
where inventoryTransactionID > 0;

delete from lwa_sandbox.parcelLine
where parcelLineID > 0;

delete from lwa_sandbox.parcel
where parcelID > 0;

delete FROM lwa_sandbox.resellerTransaction
where OrderLineID > 0;

delete FROM lwa_sandbox.orderLine
where OrderLineID > 0;
-- NOT in (select orderLineID from lwa_sandbox.resellerTransaction);

delete from lwa_sandbox.preselectedBox
where preselectedBoxID > 0;

delete FROM lwa_sandbox.warehouseOrder
where warehouseOrderID > 0;
-- not in (select warehouseOrderID from lwa_sandbox.orderLine)

delete FROM lwa_sandbox.orderExceptionHistory
where orderExceptionHistoryID > 0;

delete FROM lwa_sandbox.orderStatusHistory
where orderStatusHistoryID > 0;

delete FROM lwa_sandbox.orderItem
where orderItemID > 0;

delete FROM lwa_sandbox.orders
where orderID > 0;
-- not in (select orderID from lwa_sandbox.warehouseOrder)

delete FROM lwa_sandbox.buyer
where buyerID > 0;

delete from lwa_sandbox.orderCheckSum
where checkSumID > 0;

delete from lwa_sandbox.shippingAddress
where shippingAddressID > 0;

delete from lwa_sandbox.shopperGroup
where shopperGroupID > 0;

delete from lwa_sandbox.warehouseBatchInventory
where warehouseBatchInventoryID NOT in (8057, 8058) and warehouseBatchInventoryID > 0;

delete from lwa_sandbox.warehouseInventory
where warehouseInventoryID NOT in (4188, 4189) and warehouseInventoryID > 0;

delete from lwa_sandbox.productDescription
where productDescriptionID > 0;

delete from lwa_sandbox.productBatch
where productBatchID NOT in (6207, 6208, 6875) and productBatchID > 0;

delete from lwa_sandbox.product
where productID NOT in (2295, 2391, 2392, 2753, 3508) and productID > 0;

delete from lwa_sandbox.notification
where notificationID > 0;

delete from lwa_sandbox.stageOrder
where stageOrderID > 0;

delete from lwa_sandbox.stageProduct
where stageProductID > 0;

delete from lwa_sandbox.buyerAccountLicense
where buyerAccountLicenseID NOT in (1526, 1528, 1529, 1530, 1531) and buyerAccountLicenseID > 0;

delete from lwa_sandbox.paymentMethod
where buyerAccountID NOT in (416) and buyerAccountID > 0;

delete from lwa_sandbox.accountAddress
where accountAddressID > 0;

delete from lwa_sandbox.address
where addressID > 0;

delete from lwa_sandbox.buyerAccount
where buyerAccountID NOT in (1526, 1528, 1529, 1531, 1894) and buyerAccountID > 0;