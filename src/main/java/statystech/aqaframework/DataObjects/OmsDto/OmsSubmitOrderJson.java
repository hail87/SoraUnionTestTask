package statystech.aqaframework.DataObjects.OmsDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
public class OmsSubmitOrderJson{

	@JsonProperty("discounts")
	private Discounts discounts;

	@JsonProperty("shipping")
	private Shipping shipping;

	@JsonProperty("order_header")
	private OrderHeader orderHeader;

	@JsonProperty("line_items")
	private List<LineItemsItem> lineItems;

	@JsonProperty("buyer")
	private Buyer buyer;

}