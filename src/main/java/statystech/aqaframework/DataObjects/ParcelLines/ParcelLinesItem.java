package statystech.aqaframework.DataObjects.ParcelLines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelLinesItem{

	@JsonProperty("ci_weight")
	private String ciWeight;

	@JsonProperty("warehouse_batch_inventory_id")
	private int warehouseBatchInventoryId;

	@JsonProperty("quantity")
	private int quantity;

	@JsonProperty("product_descriptionid")
	private int productDescriptionid;

	@JsonProperty("is_ci")
	private Object isCi;

	@JsonProperty("order_lineid")
	private int orderLineid;

	@JsonProperty("parcel_id")
	private int parcelId;

	@JsonProperty("is_cold")
	private Object isCold;

	@JsonProperty("product_name")
	private String productName;

	@JsonProperty("hs_code")
	private Object hsCode;

	@JsonProperty("carrier_id")
	private Object carrierId;

	@JsonProperty("ci_country_origin_code")
	private Object ciCountryOriginCode;

	@JsonProperty("parcel_lineid")
	private int parcelLineId;

	@JsonProperty("ci_item_price")
	private Object ciItemPrice;

	@JsonProperty("parcel_status")
	private Object parcelStatus;

	@JsonProperty("product_batch_name")
	private String productBatchName;

	@JsonProperty("product_id")
	private int productId;

	@JsonProperty("ci_quantity")
	private int ciQuantity;

	@JsonProperty("product_description")
	private String productDescription;

	@JsonProperty("carrier_name")
	private Object carrierName;
}