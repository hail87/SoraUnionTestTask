package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Buyer{

	@JsonProperty("buyer_license")
	private BuyerLicense buyerLicense;

	@JsonProperty("buyer_address")
	private BuyerAddress buyerAddress;

	@JsonProperty("buyer_ip_address")
	private String buyerIpAddress;

	@Override
 	public String toString(){
		return 
			"Buyer{" + 
			"buyer_license = '" + buyerLicense + '\'' + 
			",buyer_address = '" + buyerAddress + '\'' + 
			",buyer_ip_address = '" + buyerIpAddress + '\'' + 
			"}";
		}
}