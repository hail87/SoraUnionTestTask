package statystech.aqaframework.DataObjects.OrderJackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingAddress{

	@JsonProperty("zip")
	private String zip;

	@JsonProperty("phone_2")
	private Object phone2;

	@JsonProperty("country")
	private String country;

	@JsonProperty("phone_1")
	private String phone1;

	@JsonProperty("address_type")
	private String addressType;

	@JsonProperty("city")
	private String city;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("title")
	private String title;

	@JsonProperty("middle_name")
	private String middleName;

	@JsonProperty("address_line_1")
	private String addressLine1;

	@JsonProperty("company")
	private String company;

	@JsonProperty("state")
	private String state;

	@JsonProperty("address_line_2")
	private String addressLine2;

	@JsonProperty("user_info_id")
	private String userInfoId;

	@JsonProperty("first_name")
	private String firstName;

	@Override
 	public String toString(){
		return 
			"BillingAddress{" + 
			"zip = '" + zip + '\'' + 
			",phone_2 = '" + phone2 + '\'' + 
			",country = '" + country + '\'' + 
			",phone_1 = '" + phone1 + '\'' + 
			",address_type = '" + addressType + '\'' + 
			",city = '" + city + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",title = '" + title + '\'' + 
			",middle_name = '" + middleName + '\'' + 
			",address_line_1 = '" + addressLine1 + '\'' + 
			",company = '" + company + '\'' + 
			",state = '" + state + '\'' + 
			",address_line_2 = '" + addressLine2 + '\'' + 
			",user_info_id = '" + userInfoId + '\'' + 
			",first_name = '" + firstName + '\'' + 
			"}";
		}
}