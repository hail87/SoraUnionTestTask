package statystech.aqaframework.DataObjects.WebsiteSearch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class GetWebSitesResponse{

	@JsonProperty("websites")
	private List<WebsitesItem> websites;
}