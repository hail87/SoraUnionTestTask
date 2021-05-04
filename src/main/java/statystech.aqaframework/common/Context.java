package statystech.aqaframework.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Context {

    public static Set<TestContext> context;

    public static TestContext getTestContext(String testMethodName){
        return context.stream().filter(tc -> tc.getId().equalsIgnoreCase(testMethodName)).findFirst().orElse(null);
    }

}
