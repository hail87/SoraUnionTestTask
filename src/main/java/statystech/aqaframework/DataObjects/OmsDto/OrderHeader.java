package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHeader{

	@JsonProperty("website_order_id")
	private String websiteOrderId;

	@JsonProperty("order_date")
	private String orderDate;

	@JsonProperty("total")
	private double total;

	@JsonProperty("shipping_total")
	private double shippingTotal;

	@JsonProperty("prices_include_tax")
	private boolean pricesIncludeTax;

	@JsonProperty("currency")
	private String currency;

	@Override
 	public String toString(){
		return 
			"OrderHeader{" + 
			"website_order_id = '" + websiteOrderId + '\'' + 
			",order_date = '" + orderDate + '\'' + 
			",total = '" + total + '\'' + 
			",shipping_total = '" + shippingTotal + '\'' + 
			",prices_include_tax = '" + pricesIncludeTax + '\'' + 
			",currency = '" + currency + '\'' + 
			"}";
		}
}