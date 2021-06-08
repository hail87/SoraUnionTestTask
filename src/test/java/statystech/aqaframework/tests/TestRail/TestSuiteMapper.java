package statystech.aqaframework.tests.TestRail;

import statystech.aqaframework.tests.API.OmsTestSuite;
import statystech.aqaframework.tests.DB.LwaTestSuite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestSuiteMapper {

    Map<String, Class<?>> classMap = new HashMap<>();

    public TestSuiteMapper(){
        classMap.put("1", LwaTestSuite.class);
        classMap.put("2170", OmsTestSuite.class);
    }

    public Class<?> getTestClass(String id){
        Set<Map.Entry<String, Class<?>>> entries = classMap.entrySet();
        return entries.stream().filter(e -> e.getKey().equalsIgnoreCase(id)).findFirst().orElse(null).getValue();
    }
}
