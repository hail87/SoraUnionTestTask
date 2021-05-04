package statystech.aqaframework.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import statystech.aqaframework.common.Context;
import statystech.aqaframework.common.TestContext;

import java.util.HashSet;

public abstract class Test {

    @BeforeAll
    static void createContext(){
        Context.context = new HashSet<>();
    }

    @BeforeEach
    public void setTestContext(){
        String methodName=null;
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stacktrace.length; i++) {
            if(stacktrace[i].getMethodName().equals("method")) {
                methodName = stacktrace[i+1].getMethodName();
                break;
            }
        }
        //Can be replaced with TestRailID value instead of testMethodName, but it need m ore performance
        TestContext testContext = new TestContext(methodName);
        Context.context.add(testContext);
    }
}
