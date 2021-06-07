package statystech.aqaframework.common.Context;

import lombok.Getter;
import lombok.Setter;
import statystech.aqaframework.DataObjects.OmsDto.Response;
import statystech.aqaframework.common.ConnectionDB;

@Setter
@Getter
public class OmsTestContext extends TestContext{

    public OmsTestContext(String testMethodName) {
        setTestMethodName(testMethodName);
    }

    private ConnectionDB connectionDB;

    private String testMethodName;

    private int orderId;
    private int buyerAccountId;
    private String orderStatusCd;

    private Response response;

}
