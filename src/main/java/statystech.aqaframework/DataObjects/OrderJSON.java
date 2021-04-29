
package statystech.aqaframework.DataObjects;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OrderJSON {

    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("billing_address")
    private BillingAddress billingAddress;
    @SerializedName("currency_conversion")
    private String currencyConversion;
    @SerializedName("delivered_at")
    private Object deliveredAt;
    @SerializedName("order_currency")
    private String orderCurrency;
    @SerializedName("order_date")
    private String orderDate;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("order_items")
    private List<Product> orderItems;
    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("replacement_order_id")
    private String replacementOrderId;
    @SerializedName("seller_id")
    private String sellerId;
    @SerializedName("seller_username")
    private String sellerUsername;
    @SerializedName("shipped_at")
    private Object shippedAt;
    @SerializedName("shipping_address")
    private ShippingAddress shippingAddress;
    @SerializedName("shipping_notes")
    private String shippingNotes;
    @SerializedName("shopper_group_name")
    private String shopperGroupName;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("website_name")
    private String websiteName;
}
