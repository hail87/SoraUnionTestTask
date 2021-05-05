package statystech.aqaframework.common;

import com.google.gson.JsonObject;
import statystech.aqaframework.DataObjects.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

    public static void cleanContext(){
        context = new HashSet<>();
    }

}
