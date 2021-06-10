package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyerLicense{

	@JsonProperty("profession")
	private String profession;

	@JsonProperty("country")
	private String country;

	@JsonProperty("license_name")
	private String licenseName;

	@JsonProperty("specialty")
	private String specialty;

	@JsonProperty("license_expiry_date")
	private String licenseExpiryDate;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("state")
	private String state;

	@JsonProperty("title")
	private String title;

	@JsonProperty("middle_name")
	private String middleName;

	@JsonProperty("first_name")
	private String firstName;

	@Override
 	public String toString(){
		return 
			"BuyerLicense{" + 
			"profession = '" + profession + '\'' + 
			",country = '" + country + '\'' + 
			",license_name = '" + licenseName + '\'' + 
			",specialty = '" + specialty + '\'' + 
			",license_expiry_date = '" + licenseExpiryDate + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",state = '" + state + '\'' + 
			",title = '" + title + '\'' + 
			",middle_name = '" + middleName + '\'' + 
			",first_name = '" + firstName + '\'' + 
			"}";
		}
}