package statystech.aqaframework.common.Context;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.common.ConnectionDB;

@Setter
@Getter
public class OmsTestContext extends TestContext{

    public OmsTestContext(String testMethodName) {
        setTestMethodName(testMethodName);
    }

    private ConnectionDB connectionDB;

    private String testMethodName;

    private String orderId;
    private String buyerAccountId;
    private String orderStatusCd;

}
