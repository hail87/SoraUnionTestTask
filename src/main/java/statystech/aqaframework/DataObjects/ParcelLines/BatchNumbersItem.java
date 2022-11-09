package statystech.aqaframework.DataObjects.ParcelLines;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchNumbersItem{

	@JsonProperty("warehouse_batch_inventory_id")
	private int warehouseBatchInventoryId;

	@JsonProperty("quantity")
	private int quantity;

	@JsonProperty("batch_number")
	private String batchNumber;

	@JsonProperty("exp_date")
	private String expDate;
}