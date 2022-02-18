package statystech.aqaframework.DataObjects.ProductBatch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductBatchResponse {

	@JsonProperty("product_batch_id")
	private int productBatchId;

	@JsonProperty("message")
	private String message;
}