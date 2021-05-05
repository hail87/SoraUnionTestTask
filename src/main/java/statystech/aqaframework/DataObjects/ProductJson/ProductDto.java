package statystech.aqaframework.DataObjects.ProductJson;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import statystech.aqaframework.DataObjects.ProductJson.ProductDeserializer;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(using = ProductDeserializer.class)
public class ProductDto {

    @JsonProperty("product_id")
    private Integer id;

    @JsonProperty("items")
    private List<Item> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class Item {

        @JsonProperty("product_id")
        private Integer id;

        @JsonProperty("product_name_eng")
        private String nameEng;

        @JsonProperty("product_sku")
        private String sku;

        @JsonProperty("product_weight")
        private Double weight;

        @JsonProperty("product_weight_uom")
        private String weightUom;

        @JsonProperty("product_width")
        private Double width;

        @JsonProperty("product_height")
        private Double height;

        @JsonProperty("product_lwh_uom")
        private String lwhUom;

        @JsonProperty("attr_dosage_value")
        private String dosageValue;

        @JsonProperty("attr_dosage_type")
        private String dosageType;

        @JsonProperty("attr_qty_value")
        private String quantityValue;

        @JsonProperty("attr_qty_type")
        private String quantityType;

        @JsonProperty("product_unavailable")
        private String unavailable;

        @JsonProperty("assigned_qty")
        private String assignedQuantity;

        @JsonProperty("batches")
        private JsonNode jsonNodeBatches;

        @JsonIgnore
        private List<Batch> batches;

        @SneakyThrows
        public void evaluateBatch(ObjectMapper mapper) {
            if (jsonNodeBatches.isArray()) {
                batches = Arrays.asList(mapper.treeToValue(jsonNodeBatches, Batch[].class));
                return;
            }
            batches = Collections.emptyList();
        }
    }

    @Getter
    @Setter
    public static class Batch {

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("qty")
        private Integer quantity;

        @JsonProperty("number")
        private String number;

        @JsonProperty("ff_center_name")
        private String centerName;

        @JsonProperty("exp_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expirationDate;
    }
}
