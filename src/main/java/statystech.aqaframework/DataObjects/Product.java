package statystech.aqaframework.DataObjects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    int productID;
    int productAllSysID;
    String productName;
    String productSKU;
    String batchNumber;
    String productItemPrice;
    String productQuantity;

}
