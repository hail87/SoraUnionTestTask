package statystech.aqaframework.DataObjects.ProductJson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsItem{

	private String productIdFromDB; //productID from product Table

	private String warehouseIDFromDB;

	@JsonProperty("product_height")
	private String productHeight;

	@JsonProperty("attr_dosage_value")
	private String attrDosageValue;

	@JsonProperty("attr_qty_value")
	private String attrQtyValue;

	@JsonProperty("product_weight_uom")
	private String productWeightUom;

	@JsonProperty("product_weight")
	private String productWeight;

	@JsonProperty("product_width")
	private String productWidth;

	@JsonProperty("product_name_chi")
	private String productNameChi;

	@JsonProperty("product_name_spa")
	private String productNameSpa;

	@JsonProperty("product_name_por")
	private String productNamePor;

	@JsonProperty("product_name_rus")
	private String productNameRus;

	@JsonProperty("product_lwh_uom")
	private String productLwhUom;

	@JsonProperty("product_sku")
	private String productSku;

	@JsonProperty("product_unavailable")
	private String productUnavailable;

	@JsonProperty("batches")
	private JsonNode jsonNodeBatches;

	@JsonIgnore
	private List<BatchesItem> batches;

	@SneakyThrows
	public void evaluateBatch(ObjectMapper mapper) {
		if (jsonNodeBatches.isArray()) {
			batches = Arrays.asList(mapper.treeToValue(jsonNodeBatches, BatchesItem[].class));
			return;
		}
		batches = Collections.emptyList();
	}

	@JsonProperty("product_publish")
	private String productPublish;

	@JsonProperty("attr_qty_type")
	private String attrQtyType;

	@JsonProperty("attr_dosage_type")
	private String attrDosageType;

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("product_length")
	private String productLength;

	@JsonProperty("product_name_eng")
	private String productNameEng;

	@Override
 	public String toString(){
		return 
			"ItemsItem{" + 
			"product_height = '" + productHeight + '\'' + 
			",attr_dosage_value = '" + attrDosageValue + '\'' + 
			",attr_qty_value = '" + attrQtyValue + '\'' + 
			",product_weight_uom = '" + productWeightUom + '\'' + 
			",product_weight = '" + productWeight + '\'' + 
			",product_width = '" + productWidth + '\'' + 
			",product_name_chi = '" + productNameChi + '\'' + 
			",product_name_spa = '" + productNameSpa + '\'' + 
			",product_name_por = '" + productNamePor + '\'' + 
			",product_name_rus = '" + productNameRus + '\'' + 
			",product_lwh_uom = '" + productLwhUom + '\'' + 
			",product_sku = '" + productSku + '\'' + 
			",product_unavailable = '" + productUnavailable + '\'' + 
			",batches = '" + batches + '\'' + 
			",product_publish = '" + productPublish + '\'' + 
			",attr_qty_type = '" + attrQtyType + '\'' + 
			",attr_dosage_type = '" + attrDosageType + '\'' + 
			",product_id = '" + productId + '\'' + 
			",product_length = '" + productLength + '\'' + 
			",product_name_eng = '" + productNameEng + '\'' + 
			"}";
		}

}