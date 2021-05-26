package statystech.aqaframework.DataObjects.ProductJson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	@JsonProperty("product_url")
	private String productUrl;

	@JsonProperty("product_name_por")
	private String productNamePor;

	@JsonProperty("product_name_rus")
	private String productNameRus;

	@JsonProperty("product_s_desc_chi")
	private String productSDescChi;

	@JsonProperty("product_s_desc_spa")
	private String productSDescSpa;

	@JsonProperty("product_sku")
	private String productSku;

	@JsonProperty("product_s_desc_eng")
	private String productSDescEng;

	@JsonProperty("product_publish")
	private String productPublish;

	@JsonProperty("product_desc_chi")
	private String productDescChi;

	@JsonProperty("product_desc_spa")
	private String productDescSpa;

	@JsonProperty("product_desc_eng")
	private String productDescEng;

	@JsonProperty("product_id")
	private String productId;

	@JsonProperty("product_s_desc_por")
	private String productSDescPor;

	@JsonProperty("product_name_eng")
	private String productNameEng;

	@JsonProperty("product_desc_por")
	private String productDescPor;

	@JsonProperty("product_s_desc_rus")
	private String productSDescRus;

	@JsonProperty("product_name_chi")
	private String productNameChi;

	@JsonProperty("product_desc_rus")
	private String productDescRus;

	@JsonProperty("product_name_spa")
	private String productNameSpa;

	@JsonProperty("product_notes_eng")
	private String productNotesEng;

	@JsonProperty("product_notes_chi")
	private String productNotesChi;

	@JsonProperty("product_notes_spa")
	private String productNotesSpa;

	@JsonProperty("product_notes_rus")
	private String productNotesRus;

	@JsonProperty("items")
	private List<ItemsItem> items;

	@JsonProperty("product_notes_por")
	private String productNotesPor;

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"product_url = '" + productUrl + '\'' + 
			",product_name_por = '" + productNamePor + '\'' + 
			",product_name_rus = '" + productNameRus + '\'' + 
			",product_s_desc_chi = '" + productSDescChi + '\'' + 
			",product_s_desc_spa = '" + productSDescSpa + '\'' + 
			",product_sku = '" + productSku + '\'' + 
			",product_s_desc_eng = '" + productSDescEng + '\'' + 
			",product_publish = '" + productPublish + '\'' + 
			",product_desc_chi = '" + productDescChi + '\'' + 
			",product_desc_spa = '" + productDescSpa + '\'' + 
			",product_desc_eng = '" + productDescEng + '\'' + 
			",product_id = '" + productId + '\'' + 
			",product_s_desc_por = '" + productSDescPor + '\'' + 
			",product_name_eng = '" + productNameEng + '\'' + 
			",product_desc_por = '" + productDescPor + '\'' + 
			",product_s_desc_rus = '" + productSDescRus + '\'' + 
			",product_name_chi = '" + productNameChi + '\'' + 
			",product_desc_rus = '" + productDescRus + '\'' + 
			",product_name_spa = '" + productNameSpa + '\'' + 
			",product_notes_eng = '" + productNotesEng + '\'' + 
			",product_notes_chi = '" + productNotesChi + '\'' + 
			",product_notes_spa = '" + productNotesSpa + '\'' + 
			",product_notes_rus = '" + productNotesRus + '\'' + 
			",items = '" + items + '\'' + 
			",product_notes_por = '" + productNotesPor + '\'' + 
			"}";
		}
}