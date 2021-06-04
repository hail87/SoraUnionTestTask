package statystech.aqaframework.DataObjects.OrderJackson;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Order {

	@JsonProperty("account_number")
	private String accountNumber;

	@JsonProperty("shipped_at")
	private Object shippedAt;

	@JsonProperty("csr_assigned_username")
	private String csrAssignedUsername;

	@JsonProperty("order_currency")
	private String orderCurrency;

	@JsonProperty("shopper_group_name")
	private String shopperGroupName;

	@JsonProperty("billing_address")
	private BillingAddress billingAddress;

	@JsonProperty("shipping_notes")
	private String shippingNotes;

	@JsonProperty("replacement_order_id")
	private String replacementOrderId;

	@JsonProperty("website_name")
	private String websiteName;

	@JsonProperty("order_status")
	private String orderStatus;

	@JsonProperty("seller_username")
	private String sellerUsername;

	@JsonProperty("order_date")
	private String orderDate;

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("currency_conversion")
	private String currencyConversion;

	@JsonProperty("csr_assigned_id")
	private String csrAssignedId;

	@JsonProperty("shipping_address")
	private ShippingAddress shippingAddress;

	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("delivered_at")
	private Object deliveredAt;

	@JsonProperty("seller_id")
	private String sellerId;

	@JsonProperty("order_items")
	private List<OrderItem> orderItems;

}