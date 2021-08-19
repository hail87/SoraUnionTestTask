package statystech.aqaframework.DataObjects.OmsDto.AretoApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Detail{

	@JsonProperty("amount")
	private double amount;

	@JsonProperty("orderId")
	private String orderId;

	@JsonProperty("internalOrderId")
	private String internalOrderId;

	@JsonProperty("panStrip")
	private String panStrip;

	@JsonProperty("mid")
	private int mid;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("state")
	private String state;

	@JsonProperty("transactionId")
	private String transactionId;

	@JsonProperty("token")
	private String token;

	@Override
 	public String toString(){
		return 
			"Detail{" + 
			"amount = '" + amount + '\'' + 
			",orderId = '" + orderId + '\'' + 
			",internalOrderId = '" + internalOrderId + '\'' + 
			",panStrip = '" + panStrip + '\'' + 
			",mid = '" + mid + '\'' + 
			",currency = '" + currency + '\'' + 
			",state = '" + state + '\'' + 
			",transactionId = '" + transactionId + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}