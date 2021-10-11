package statystech.aqaframework.DataObjects.WarehouseSearch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersItem{

	@JsonProperty("order_date")
	private String orderDate;

	@JsonProperty("warehouse_orders")
	private List<WarehouseOrdersItem> warehouseOrders;

	@JsonProperty("order_status_id")
	private int orderStatusId;

	@JsonProperty("allsys_order_id")
	private int allsysOrderId;

	@JsonProperty("shipping_address_country")
	private String shippingAddressCountry;

	@Override
 	public String toString(){
		return 
			"OrdersItem{" + 
			"order_date = '" + orderDate + '\'' + 
			",warehouse_orders = '" + warehouseOrders + '\'' + 
			",order_status_id = '" + orderStatusId + '\'' + 
			",allsys_order_id = '" + allsysOrderId + '\'' + 
			",shipping_address_country = '" + shippingAddressCountry + '\'' + 
			"}";
		}
}