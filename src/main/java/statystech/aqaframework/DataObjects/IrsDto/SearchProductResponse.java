package statystech.aqaframework.DataObjects.IrsDto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)

public class SearchProductResponse {

    @JsonProperty("record_count")
    private int recordCount;

    @JsonProperty("product_records")
    private List<ProductRecordsItem> productRecords;
}