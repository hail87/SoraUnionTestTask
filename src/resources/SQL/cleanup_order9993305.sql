delete from lwa_test_enc.resellerTransaction
where orderID=20827;

delete from lwa_test_enc.warehouseTransaction
where orderID=20827;

delete from lwa_test_enc.parcelLine
where parcelLineID in (34148,34798,34799);

delete FROM lwa_test_enc.orderLine
where OrderLineID in (32178,32179);

delete FROM lwa_test_enc.warehouseOrder
where warehouseOrderID = 23409;

delete from lwa_test_enc.orderStatusHistory
where orderID=20827;

delete from lwa_test_enc.orderItem
where orderID=20827;

delete FROM lwa_test_enc.orders
where orderID=20827;

delete from lwa_test_enc.orderCheckSum
where orderID=9993305;













