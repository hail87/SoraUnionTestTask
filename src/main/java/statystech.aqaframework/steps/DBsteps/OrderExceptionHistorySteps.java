package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.OrderExceptionHistoryTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

import java.sql.SQLException;

public class OrderExceptionHistorySteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(OrderExceptionHistorySteps.class);

    OrderExceptionHistoryTable orderExceptionHistoryTable;

    public OrderExceptionHistorySteps(){
        orderExceptionHistoryTable = new OrderExceptionHistoryTable();
        super.tableObject = orderExceptionHistoryTable;
    }

    public String verifyRowWithOrderIdExist(LwaTestContext lwaTestContext) {
        boolean rowIsPresent = false;
        int i = 0;
        while(!orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(lwaTestContext.getApiOrderId())) && i < 3)
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        rowIsPresent = orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(lwaTestContext.getApiOrderId()));

        if (!rowIsPresent) {
            return "There is no row with orderID : " + lwaTestContext.getApiOrderId();
        }
        return "";
    }


    public String verifyRowWithOrderIdNotExist(int orderID) {
        boolean rowIsPresent = false;
        int i = 0;
        while(!orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderID)) && i < 3)
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        rowIsPresent = orderExceptionHistoryTable.checkRowWithValueIsPresent("orderID", String.valueOf(orderID));

        if (rowIsPresent) {
            return "There is a row with orderID : '" + orderID + "', but should NOT!";
        }
        return "";
    }

    public String verifyOrderExceptionTypeID(LwaTestContext lwaTestContext, int expectedType) {
        int orderExceptionHistoryID;
        int actualType = 0;
        try {
            orderExceptionHistoryID = orderExceptionHistoryTable.getPrimaryID("orderID", String.valueOf(lwaTestContext.getApiOrderId()));
            actualType = Integer.parseInt(orderExceptionHistoryTable.getColumnValueByPrimaryID(orderExceptionHistoryID, "orderExceptionTypeID"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "\nCan't get orderExceptionHistoryID or orderExceptionTypeID\n";
        }

        if (expectedType != actualType) {
            return String.format("\nExpected orderExceptionTypeID '%s' and actual '%s' are NOT the same!", expectedType, actualType);
        }
        return "";
    }

    public String verifyOrderExceptionTypeIDIsNot(LwaTestContext lwaTestContext, int expectedType) {
        int orderExceptionHistoryID;
        int actualType = 0;
        try {
            orderExceptionHistoryID = orderExceptionHistoryTable.getPrimaryID("orderID", String.valueOf(lwaTestContext.getApiOrderId()));
            actualType = Integer.parseInt(orderExceptionHistoryTable.getColumnValueByPrimaryID(orderExceptionHistoryID, "orderExceptionTypeID"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "\nCan't get orderExceptionHistoryID or orderExceptionTypeID\n";
        }

        if (expectedType == actualType) {
            return String.format("\nExpected orderExceptionTypeID '%s' and actual '%s' are the same, but should NOT be\n", expectedType, actualType);
        }
        return "";
    }


}
