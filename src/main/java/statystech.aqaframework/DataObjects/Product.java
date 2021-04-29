package statystech.aqaframework.DataObjects;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Product {
    @SerializedName("order_item_id")
    private String orderItemId;
    private int productID; //from productBatch table
    @SerializedName("product_id")
    private int productAllSysID;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("SKU")
    private String productSKU;
    @SerializedName("product_item_price")
    private String productItemPrice;
    @SerializedName("product_quantity")
    private String productQuantity;
    @SerializedName("ff_centers")
    private List<Warehouse> warehouses;
}
