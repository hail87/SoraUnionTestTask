package statystech.aqaframework.DataObjects.ProductJson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImport{

	@JsonProperty("is_active")
	private boolean isActive;

	@JsonProperty("catalog_category")
	private String catalogCategory;

	@JsonProperty("temperature_details")
	private String temperatureDetails;

	@JsonProperty("is_generic")
	private boolean isGeneric;

	@JsonProperty("is_licensed")
	private boolean isLicensed;

	@JsonProperty("description")
	private String description;

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
}