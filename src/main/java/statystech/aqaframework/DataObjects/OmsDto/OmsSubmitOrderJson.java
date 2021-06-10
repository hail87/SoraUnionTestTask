package statystech.aqaframework.DataObjects.OmsDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmsSubmitOrderJson{

	@JsonProperty("discounts")
	private Discounts discounts;

	@JsonProperty("shipping")
	private Shipping shipping;

	@JsonProperty("payment_details")
	private PaymentDetails paymentDetails;

	@JsonProperty("order_header")
	private OrderHeader orderHeader;

	@JsonProperty("line_items")
	private List<LineItemsItem> lineItems;

	@JsonProperty("buyer")
	private Buyer buyer;

	@Override
 	public String toString(){
		return 
			"OmsSubmitOrderJson{" + 
			"discounts = '" + discounts + '\'' + 
			",shipping = '" + shipping + '\'' + 
			",payment_details = '" + paymentDetails + '\'' + 
			",order_header = '" + orderHeader + '\'' + 
			",line_items = '" + lineItems + '\'' + 
			",buyer = '" + buyer + '\'' + 
			"}";
		}
}