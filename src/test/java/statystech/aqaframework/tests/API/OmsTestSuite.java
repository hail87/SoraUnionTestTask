package statystech.aqaframework.tests.API;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import statystech.aqaframework.steps.APIsteps.OmsApiSteps;
import statystech.aqaframework.tests.TestClass;
import statystech.aqaframework.tests.TestRail.TestRailID;
import statystech.aqaframework.tests.TestRail.TestRailReportExtension;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestRailReportExtension.class)
public class OmsTestSuite extends TestClass {

    //@TestRailID(id = 7743)
//    @ParameterizedTest
//    @ValueSource(strings = {"submitOrder-newBuyer.json"})
//    public void submitOrderNoBuyerAccountId(String jsonFilename, TestInfo testInfo) throws IOException, SQLException {
//        StringBuilder errorMessage = new StringBuilder();
//        errorMessage.append(new OmsApiSteps().submitWebsiteOrder(jsonFilename));
//        assertTrue(errorMessage.isEmpty(), errorMessage.toString());
//    }


}
