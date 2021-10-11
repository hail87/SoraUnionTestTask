package statystech.aqaframework.DataObjects.WarehouseSearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseOrdersItem{

	@JsonProperty("total_value")
	private Object totalValue;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("user_name")
	private Object userName;

	@JsonProperty("modified_date")
	private String modifiedDate;

	@JsonProperty("warehouse_order_id")
	private int warehouseOrderId;

	@JsonProperty("processing_status_cd")
	private Object processingStatusCd;

	@JsonProperty("total_parcels")
	private int totalParcels;

	@JsonProperty("warehouse_name")
	private String warehouseName;

	@JsonProperty("warehouse_order_status_id")
	private int warehouseOrderStatusId;

	@JsonProperty("total_units")
	private int totalUnits;

	@JsonProperty("warehouse_order_status_name")
	private String warehouseOrderStatusName;

	@JsonProperty("total_products")
	private int totalProducts;

	@JsonProperty("warehouse_id")
	private int warehouseId;

	@Override
 	public String toString(){
		return 
			"WarehouseOrdersItem{" + 
			"total_value = '" + totalValue + '\'' + 
			",comments = '" + comments + '\'' + 
			",user_name = '" + userName + '\'' + 
			",modified_date = '" + modifiedDate + '\'' + 
			",warehouse_order_id = '" + warehouseOrderId + '\'' + 
			",processing_status_cd = '" + processingStatusCd + '\'' + 
			",total_parcels = '" + totalParcels + '\'' + 
			",warehouse_name = '" + warehouseName + '\'' + 
			",warehouse_order_status_id = '" + warehouseOrderStatusId + '\'' + 
			",total_units = '" + totalUnits + '\'' + 
			",warehouse_order_status_name = '" + warehouseOrderStatusName + '\'' + 
			",total_products = '" + totalProducts + '\'' + 
			",warehouse_id = '" + warehouseId + '\'' + 
			"}";
		}
}