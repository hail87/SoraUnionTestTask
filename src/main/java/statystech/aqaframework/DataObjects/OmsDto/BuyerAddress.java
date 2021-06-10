package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyerAddress{

	@JsonProperty("country")
	private String country;

	@JsonProperty("city")
	private String city;

	@JsonProperty("address_1")
	private String address1;

	@JsonProperty("address_2")
	private String address2;

	@JsonProperty("postcode")
	private String postcode;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("title")
	private String title;

	@JsonProperty("middle_name")
	private String middleName;

	@JsonProperty("phone_number_1")
	private String phoneNumber1;

	@JsonProperty("phone_number_2")
	private String phoneNumber2;

	@JsonProperty("company")
	private String company;

	@JsonProperty("state")
	private String state;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("email")
	private String email;

	@Override
 	public String toString(){
		return 
			"BuyerAddress{" + 
			"country = '" + country + '\'' + 
			",city = '" + city + '\'' + 
			",address_1 = '" + address1 + '\'' + 
			",address_2 = '" + address2 + '\'' + 
			",postcode = '" + postcode + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",title = '" + title + '\'' + 
			",middle_name = '" + middleName + '\'' + 
			",phone_number_1 = '" + phoneNumber1 + '\'' + 
			",phone_number_2 = '" + phoneNumber2 + '\'' + 
			",company = '" + company + '\'' + 
			",state = '" + state + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}