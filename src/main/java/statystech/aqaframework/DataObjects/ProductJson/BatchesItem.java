package statystech.aqaframework.DataObjects.ProductJson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchesItem{

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("qty")
	private Integer quantity;

	@JsonProperty("number")
	private String number;

	@JsonProperty("ff_center_name")
	private String centerName;

//	@JsonProperty("exp_date")
//	@JsonFormat(pattern = "yyyy-MM-dd")
//	private LocalDate expirationDate;

	@Override
 	public String toString(){
		return 
			"BatchesItem{" + 
			"ff_center_name = '" + centerName + '\'' +
			",number = '" + number + '\'' + 
			",qty = '" + quantity + '\'' +
//			",exp_date = '" + expirationDate + '\'' +
			",id = '" + id + '\'' + 
			"}";
		}
}