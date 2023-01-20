package statystech.aqaframework.DataObjects.ProductJson.CatalogManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem {

	@JsonProperty("is_active")
	private boolean isActive;

	@JsonProperty("catalog_category")
	private String catalogCategory;

	@JsonProperty("variant_count")
	private int variantCount;

	@JsonProperty("product_id")
	private int productId;

	@JsonProperty("is_licensed")
	private boolean isLicensed;

	@JsonProperty("brand_name")
	private String brandName;

	@JsonProperty("is_cold")
	private boolean isCold;

	@JsonProperty("product_name")
	private String productName;

	@JsonProperty("manufacturer")
	private String manufacturer;

	@JsonProperty("hs_code")
	private String hsCode;

	@JsonProperty("cooling_details")
	private String coolingDetails;
}