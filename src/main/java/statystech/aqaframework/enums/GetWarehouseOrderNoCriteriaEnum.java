package statystech.aqaframework.enums;

public enum GetWarehouseOrderNoCriteriaEnum {
    order1 ("GetWarehouseOrderNoCriteria1.json"),
    order2 ("GetWarehouseOrderNoCriteria2.json"),
    order3 ("GetWarehouseOrderNoCriteria3.json"),
    order4 ("GetWarehouseOrderNoCriteria4.json"),
    order5 ("GetWarehouseOrderNoCriteria5.json");

    private String title;

    GetWarehouseOrderNoCriteriaEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
