package statystech.aqaframework.DataObjects.ProductJson;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProductItemsParsingException extends JsonProcessingException {

    private static final long serialVersionUID = 1L;

    public ProductItemsParsingException(String message) {
        super(message);
    }
}
