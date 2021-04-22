package statystech.aqaframework.tests.TestRail;

import statystech.aqaframework.tests.DB.DbTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestSuiteID {

    Map<String, Class<?>> classMap = new HashMap<>();

    public TestSuiteID(){
        classMap.put("1", DbTest.class);
    }

    public Class<?> getTestClass(String id){
        Set<Map.Entry<String, Class<?>>> entries = classMap.entrySet();
        return entries.stream().filter(e -> e.getKey().equalsIgnoreCase(id)).findFirst().orElse(null).getValue();
    }
}
