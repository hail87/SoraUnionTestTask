package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetails{

	@JsonProperty("payment_method_type")
	private String paymentMethodType;

	@JsonProperty("payment_token")
	private String paymentToken;

	@JsonProperty("payment_method_description")
	private String paymentMethodDescription;

	@JsonProperty("payment_transaction_id")
	private String paymentTransactionId;

	@JsonProperty("payment_mid")
	private int paymentMid;

	@Override
 	public String toString(){
		return 
			"PaymentDetails{" + 
			"payment_method_type = '" + paymentMethodType + '\'' + 
			",payment_token = '" + paymentToken + '\'' + 
			",payment_method_description = '" + paymentMethodDescription + '\'' + 
			",payment_transaction_id = '" + paymentTransactionId + '\'' + 
			",payment_mid = '" + paymentMid + '\'' + 
			"}";
		}
}