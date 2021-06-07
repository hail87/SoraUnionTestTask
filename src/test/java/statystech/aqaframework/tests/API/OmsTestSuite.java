package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.common.Context.Context;
import statystech.aqaframework.common.Context.LwaTestContext;
import statystech.aqaframework.common.Context.OmsTestContext;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestRailReportExtension.class)
public class OmsTestSuite extends TestClass {

    @BeforeEach
    public void setTestContext(TestInfo testInfo) throws SQLException, IOException {
        OmsTestContext omsTestContext = new OmsTestContext(testInfo.getTestMethod().get().getName());
        omsTestContext.getConnection();
        Context.addTestContext(omsTestContext);
    }

    @TestRailID(id = 7743)
    @ParameterizedTest
    @ValueSource(strings = {"submitOrder-newBuyer.json"})
    public void submitOrderNoBuyerAccountId(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(new OmsApiSteps().sendPostRequestAndSaveResponseToContext(jsonFilename, testInfo));
        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
    }


}
