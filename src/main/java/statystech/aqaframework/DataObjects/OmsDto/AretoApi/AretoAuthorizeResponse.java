package statystech.aqaframework.DataObjects.OmsDto.AretoApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AretoAuthorizeResponse{

	@JsonProperty("code")
	private int code;

	@JsonProperty("description")
	private String description;

	@JsonProperty("detail")
	private Detail detail;

	@Override
 	public String toString(){
		return 
			"AretoAuthorizeResponse{" + 
			"code = '" + code + '\'' + 
			",description = '" + description + '\'' + 
			",detail = '" + detail + '\'' + 
			"}";
		}
}