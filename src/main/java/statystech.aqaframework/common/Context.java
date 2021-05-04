package statystech.aqaframework.common;

import java.util.HashSet;
import java.util.Set;

public class Context {

    private static Set<TestContext> context;

    public static void initialize(){
        context = new HashSet<>();
    }

    public static void addTestContext(TestContext testContext){
        context.add(testContext);
    }

    public static TestContext getTestContext(int testID){
        return context.stream().filter(tc -> tc.getId()==testID).findFirst().orElse(null);
    }

}
