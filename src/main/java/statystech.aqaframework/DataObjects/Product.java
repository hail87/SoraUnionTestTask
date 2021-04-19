package statystech.aqaframework.DataObjects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Product {
    int productID;
    int productAllSysID;
    String productName;
    String productSKU;
    String productItemPrice;
    String productQuantity;
    List<Warehouse> warehouses;
}
