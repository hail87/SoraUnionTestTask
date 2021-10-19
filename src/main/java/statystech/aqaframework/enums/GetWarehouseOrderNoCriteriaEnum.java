package statystech.aqaframework.enums;

public enum GetWarehouseOrderNoCriteriaEnum {
    order6097147 ("GetWarehouseOrderNoCriteria1.json"),
    order6097800 ("GetWarehouseOrderNoCriteria2.json"),
    order6098207 ("GetWarehouseOrderNoCriteria3.json"),
    order6097621 ("GetWarehouseOrderNoCriteria4.json"),
    order6095793 ("GetWarehouseOrderNoCriteria5.json");

    private String title;

    GetWarehouseOrderNoCriteriaEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
