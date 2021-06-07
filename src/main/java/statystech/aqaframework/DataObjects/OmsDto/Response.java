package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response{

	@JsonProperty("order_status_cd")
	private String orderStatusCd;

	@JsonProperty("buyer_account_id")
	private int buyerAccountId;

	@JsonProperty("order_id")
	private int orderId;

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"order_status_cd = '" + orderStatusCd + '\'' + 
			",buyer_account_id = '" + buyerAccountId + '\'' + 
			",order_id = '" + orderId + '\'' + 
			"}";
		}
}