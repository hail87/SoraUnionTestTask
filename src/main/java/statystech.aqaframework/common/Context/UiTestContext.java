package statystech.aqaframework.common.Context;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;

@Setter
@Getter
public class UiTestContext extends TestContext{

    public UiTestContext(String testMethodName) {
        setTestMethodName(testMethodName);
    }

    public WebDriver getWebDriver(){
        return super.getWebDriver();
    }

    private String testMethodName;

    @Override
    public int hashCode() {
        return getTestMethodName().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        MainTestContext mainTestContext = (MainTestContext) object;
        return this.testMethodName.equalsIgnoreCase(mainTestContext.getTestMethodName());
    }

}
