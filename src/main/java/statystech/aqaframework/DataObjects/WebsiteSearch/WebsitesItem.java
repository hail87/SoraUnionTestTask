package statystech.aqaframework.DataObjects.WebsiteSearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class WebsitesItem{

	@JsonProperty("website_name")
	private String websiteName;

	@JsonProperty("website_id")
	private Integer websiteId;

	@JsonProperty("website_currency")
	private String websiteCurrency;
}