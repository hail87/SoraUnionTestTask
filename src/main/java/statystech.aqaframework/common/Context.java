package statystech.aqaframework.common;

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

    public static TestContext getTestContext(String testMethodName){
        return suiteContext.stream().filter(tc -> tc.getTestMethodName()==testMethodName).findFirst().orElse(null);
    }

    public static TestContext getTestContext(int testMethodNumber){
        String testMethodName = Thread.currentThread().getStackTrace()[testMethodNumber].getMethodName();
        return Context.getTestContext(testMethodName);
    }

    public static TestContext getTestContext(){
        String testMethodName = Arrays.stream(
                Thread.currentThread().getStackTrace()).filter(m -> m.getFileName().contains("Test")).findFirst().get().getMethodName();
        return Context.getTestContext(testMethodName);
    }

    public static void cleanContext(){
        suiteContext = new HashSet<>();
    }

}
