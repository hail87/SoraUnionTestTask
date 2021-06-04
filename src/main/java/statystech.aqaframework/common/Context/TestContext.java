package statystech.aqaframework.common.Context;

public interface TestContext {
    String getTestMethodName();
    <T extends TestContext> T getImplementation();
}
