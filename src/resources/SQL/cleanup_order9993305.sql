delete FROM lwa_test_enc.resellerTransaction
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete from lwa_test_enc.warehouseTransaction
where orderID= (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete from lwa_test_enc.parcelLine where orderLineID in (
SELECT orderLineID FROM lwa_test_enc.orderLine WHERE warehouseOrderID in (
SELECT warehouseOrderID FROM lwa_test_enc.warehouseOrder WHERE orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305)));

delete FROM lwa_test_enc.orderLine
where warehouseOrderID in (SELECT warehouseOrderID from lwa_test_enc.warehouseOrder
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305));

delete FROM lwa_test_enc.tracking
where parcelID in (SELECT parcelID from lwa_test_enc.parcel
where warehouseOrderID in (SELECT warehouseOrderID from lwa_test_enc.warehouseOrder
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305)));

delete FROM lwa_test_enc.parcel
where warehouseOrderID in (SELECT warehouseOrderID from lwa_test_enc.warehouseOrder
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305));

delete FROM lwa_test_enc.inventoryTransaction
where warehouseOrderID in (SELECT warehouseOrderID from lwa_test_enc.warehouseOrder
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305));

delete FROM lwa_test_enc.warehouseOrder
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete from lwa_test_enc.orderStatusHistory
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete from lwa_test_enc.orderItem
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete from lwa_test_enc.orderExceptionHistory
where orderID = (SELECT orderID from lwa_test_enc.orders where orderAllSysID = 9993305);

delete FROM lwa_test_enc.orderItem
where orderID =
(select orderID FROM lwa_test_enc.orders where parentOrderID = (SELECT orderID FROM lwa_test_enc.orders where orderAllSysID = 9993305)) ;

delete FROM lwa_test_enc.orders where parentOrderID =
(SELECT a.orderID FROM (select orderID from lwa_test_enc.orders where orderAllSysID = 9993305 limit 1) as a);

delete FROM lwa_test_enc.orders
where orderAllSysID = 9993305;

delete from lwa_test_enc.orderCheckSum
where orderID=9993305;