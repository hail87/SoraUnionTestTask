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

delete FROM lwa_sandbox.orderLine
where OrderLineID > 0;

delete from lwa_sandbox.preselectedBox
where preselectedBoxID > 0;

delete FROM lwa_sandbox.warehouseOrder
where warehouseOrderID > 0;

delete FROM lwa_sandbox.orderExceptionHistory
where orderExceptionHistoryID > 0;

delete FROM lwa_sandbox.orderStatusHistory
where orderStatusHistoryID > 0;

delete FROM lwa_sandbox.orderItem
where orderItemID > 0;

delete FROM lwa_sandbox.orders
where orderID > 0;

delete FROM lwa_sandbox.buyer
where buyerID > 0;

delete from lwa_sandbox.orderCheckSum
where checkSumID > 0;

delete from lwa_sandbox.shippingAddress
where shippingAddressID > 0;

delete from lwa_sandbox.shopperGroup
where shopperGroupID > 0;

delete from lwa_sandbox.warehouseBatchInventory
where warehouseBatchInventoryID > 0;

delete from lwa_sandbox.warehouseInventory
where warehouseInventoryID > 0;

delete from lwa_sandbox.productDescription
where productDescriptionID > 0;

delete from lwa_sandbox.productBatch
where productBatchID > 0;

delete from lwa_sandbox.product
where productID NOT in (2295, 2391) and productID > 0;

delete from lwa_sandbox.notification
where notificationID > 0;

delete from lwa_sandbox.stageOrder
where stageOrderID > 0;

delete from lwa_sandbox.stageProduct
where stageProductID > 0;

delete from lwa_sandbox.buyerAccountLicense
where buyerAccountLicenseID > 0;