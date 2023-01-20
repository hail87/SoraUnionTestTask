package statystech.aqaframework.DataObjects.ProductJson.CatalogManagement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchResponse{

	@JsonProperty("products")
	private List<ProductItem> products;
}