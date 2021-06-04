package statystech.aqaframework.common.Context;

import org.junit.jupiter.api.TestInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Context {

    private static Set<TestContext> suiteContext;

    public static void initialize(){
        suiteContext = new HashSet<>();
    }

    public static void addTestContext(TestContext testContext){
        suiteContext.add(testContext);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TestContext> T getTestContext(String testMethodName, Class<T> type){
        return type.cast(suiteContext.stream().filter(tc -> tc.getTestMethodName().equals(testMethodName)).findFirst().orElse(null));
    }

    @SuppressWarnings("unchecked")
    public static <T extends TestContext> T getTestContext(TestInfo testInfo, Class<T> type){
        return (T) getTestContext(testInfo.getTestMethod().get().getName(), type);
    }

    public static TestContext getTestContext(TestInfo testInfo){
        return getTestContext(testInfo.getTestMethod().get().getName());
    }

    public static <T extends TestContext> T getTestContext(Class<T> type){
        String testMethodName = Arrays.stream(
                Thread.currentThread().getStackTrace()).filter(m -> m.getFileName().contains("Test")).findFirst().get().getMethodName();
        return Context.getTestContext(testMethodName, type);
    }

    public static TestContext getTestContext(String testMethodName){
        return suiteContext.stream().filter(tc -> tc.getTestMethodName().equals(testMethodName)).findFirst().orElse(null);
    }

    public static void updateTestContext(TestContext testContext){
        TestContext oldTestContext = getTestContext(testContext.getTestMethodName());
        suiteContext.remove(oldTestContext);
        addTestContext(testContext);
    }

    public static void deleteTestContext(TestContext testContext){
        suiteContext.remove(testContext);
    }

    public static void deleteTestContext(TestInfo testInfo){
        TestContext testContext = getTestContext(testInfo);
        suiteContext.remove(testContext);
    }

    public static void cleanContext(){
        suiteContext = new HashSet<>();
    }

}
