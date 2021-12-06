package statystech.aqaframework.DataObjects.OrderJackson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {

	@JsonProperty("order_item_id")
	private String orderItemId;

	private int productID; //from productBatch table

	@JsonProperty("ff_centers")
	private List<FfCenter> warehouses;

	@JsonProperty("product_item_price")
	private String productItemPrice;

	@JsonProperty("product_id")
	private String productAllSysID;

	@JsonProperty("SKU")
	private String SKU;

	@JsonProperty("product_name")
	private String productName;

	@JsonProperty("product_quantity")
	private String productQuantity;

	@JsonProperty("batches")
	private String batches;

	@Override
 	public String toString(){
		return 
			"OrderItemsItem{" + 
			"order_item_id = '" + orderItemId + '\'' + 
			",ff_centers = '" + warehouses + '\'' +
			",product_item_price = '" + productItemPrice + '\'' + 
			",product_id = '" + productAllSysID + '\'' +
			",sKU = '" + SKU + '\'' +
			",product_name = '" + productName + '\'' + 
			",product_quantity = '" + productQuantity + '\'' + 
			"}";
		}
}