package statystech.aqaframework.DataObjects.IrsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProductRecordsItem{

	@JsonProperty("tiers")
	private Object tiers;

	@JsonProperty("product_dosage_value")
	private String productDosageValue;

	@JsonProperty("product_pack_type")
	private String productPackType;

	@JsonProperty("item_price")
	private Object itemPrice;

	@JsonProperty("is_cold")
	private boolean isCold;

	@JsonProperty("product_name")
	private String productName;

	@JsonProperty("total_available")
	private String totalAvailable;

	@JsonProperty("product_coo")
	private Object productCoo;

	@JsonProperty("item_currency")
	private Object itemCurrency;

	@JsonProperty("is_license")
	private Object isLicense;

	@JsonProperty("product_id")
	private int productId;

	@JsonProperty("product_dosage_type")
	private String productDosageType;

	@JsonProperty("product_manufacture")
	private Object productManufacture;

	@JsonProperty("product_brand")
	private Object productBrand;

	@JsonProperty("product_items_in_pack")
	private String productItemsInPack;
}