
package statystech.aqaframework.DataObjects;

import java.util.List;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;
import statystech.aqaframework.DataObjects.ProductJson.ProductDeserializer;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;



@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @SerializedName("account_number")
    @JsonProperty()
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
