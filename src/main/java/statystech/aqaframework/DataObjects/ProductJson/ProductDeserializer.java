package statystech.aqaframework.DataObjects.ProductJson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
import statystech.aqaframework.DataObjects.ProductJson.ProductDto.Item;
import statystech.aqaframework.DataObjects.ProductJson.ProductItemsParsingException;

public class ProductDeserializer extends JsonDeserializer<ProductDto> {

    @Override
    public ProductDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        JsonNode itemsNode = node.get("items");
        if (itemsNode.isBoolean()) {
            throw new ProductItemsParsingException("Incorrect product items format: " + itemsNode.asText());
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForListOf(Item.class);

        ProductDto product = new ProductDto();
        product.setId(node.get("product_id").asInt());
        product.setItems(reader.readValue(itemsNode));

        return product;
    }

}
