package statystech.aqaframework.steps.UiSteps;

import statystech.aqaframework.PageObjects.MainPage;
import statystech.aqaframework.steps.Steps;

public class MainSteps extends Steps {

    MainPage mainPage;

    public MainSteps(MainPage mainPage){
        this.mainPage = mainPage;
    }

    public MainPage chooseWarehouse(String warehouse){
        return mainPage.chooseWarehouse(warehouse);
    }
}
