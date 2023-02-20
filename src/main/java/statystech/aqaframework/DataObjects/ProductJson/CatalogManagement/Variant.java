package statystech.aqaframework.DataObjects.ProductJson.CatalogManagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Variant {

	@JsonProperty("variant_id")
	private int variantId;

	@JsonProperty("is_active")
	private boolean isActive;

	@JsonProperty("variantName")
	private String variantName;

	@JsonProperty("sku")
	private String sku;

	@JsonProperty("is_brand")
	private boolean isBrand;
}