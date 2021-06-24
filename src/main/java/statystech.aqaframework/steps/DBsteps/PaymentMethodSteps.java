package statystech.aqaframework.steps.DBsteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.TableObjects.PaymentMethodTable;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.steps.Steps;

public class PaymentMethodSteps extends Steps {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodSteps.class);

    public PaymentMethodTable paymentMethodTable = new PaymentMethodTable();

    public String checkLineCreated(LwaTestContext lwaTestContext){
        if (!paymentMethodTable.checkRowWithIDExist(lwaTestContext.getPaymentMethodID())){
            return "There is no line with PaymentMethodID " + lwaTestContext.getPaymentMethodID();
        }
        paymentMethodTable.setTableRowsQuantity();
        return "";
    }

}
