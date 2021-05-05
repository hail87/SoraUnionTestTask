//package statystech.aqaframework.DataObjects.ProductJson;
//
//import statystech.aqaframework.DataObjects.ProductJson.ProductBatchEntity;
//import statystech.aqaframework.DataObjects.ProductJson.ProductEntity;
//import statystech.aqaframework.DataObjects.ProductJson.ProductDto;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class ProductMapper {
//
//    private static final Map<String, Integer> productUnavailableFlags = Map.of("N", 0, "n", 0, "Y", 1, "y", 1);
//
//    public static List<ProductEntity> getProducts(ProductDto productDto) {
//        return productDto.getItems().isEmpty() ? Collections.emptyList() :
//                productDto.getItems().stream()
//                        .map(p -> ProductEntity.builder()
//                                .allSysId(p.getId())
//                                .name(p.getNameEng())
//                                .sku(p.getSku())
//                                .weight(p.getWeight())
//                                .weightUom(p.getWeightUom())
//                                .width(p.getWidth())
//                                .length(p.getHeight())
//                                .lwhUom(p.getLwhUom())
//                                .dosageType(p.getDosageType())
//                                .dosageValue(p.getDosageValue())
//                                .itemsInPack(p.getQuantityValue())
//                                .packType(p.getQuantityType())
//                                .isCold(null) // TODO: what is the condition to flip that value
//                                .productUnavailable(convertProductUnavailability(p.getUnavailable()))
//                                .build())
//                        .collect(Collectors.toList());
//    }
//
//    public static List<ProductBatchEntity> getProductBatches(ProductDto productDto, List<ProductEntity> lwaProducts) {
//        return productDto.getItems().stream()
//                .filter(item -> !item.getBatches().isEmpty())
//                .flatMap(item -> item.getBatches().stream()
//                        .map(batch -> ProductBatchEntity.builder()
//                                .product(getRelatedProduct(item, lwaProducts))
//                                .allSysBatchId(batch.getId())
//                                .batchNumber(batch.getNumber())
//                                .freeStock(batch.getQuantity())
//                                .warehouseUniqueName(batch.getCenterName())
//                                .expireDate(batch.getExpirationDate())
//                                .build()))
//                .collect(Collectors.toList());
//    }
//
//    private static ProductEntity getRelatedProduct(ProductDto.Item item, List<ProductEntity> lwaProducts) {
//        return lwaProducts.stream().filter(lwaProduct -> lwaProduct.getAllSysId().equals(item.getId()))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Couldn't find lwa product_id for for product with all_sys_id: " + item.getId()));
//    }
//
//    private static Integer convertProductUnavailability(String flag) {
//        return Objects.nonNull(flag) ? productUnavailableFlags.getOrDefault(flag, 0) : 0;
//    }
//}
//
