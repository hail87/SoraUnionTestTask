package statystech.aqaframework.DataObjects.ParcelLines;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchNumbers{

	@JsonProperty("batch_numbers")
	private List<BatchNumbersItem> batchNumbers;
}