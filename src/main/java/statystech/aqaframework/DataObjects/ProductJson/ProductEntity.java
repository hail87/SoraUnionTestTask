//package statystech.aqaframework.DataObjects.ProductJson;
//package com.lwa.common.dao.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import java.util.List;
//
//@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
//@Builder
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "product")
//public class ProductEntity extends AbstractAuditingEntity {
//
//    @Id
//    @Column(name = "productID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ToString.Include
//    @EqualsAndHashCode.Include
//    @Column(name = "productAllSysID", nullable = false)
//    private Integer allSysId;
//
//    @ToString.Include
//    @EqualsAndHashCode.Include
//    @Column(name = "productName", nullable = false)
//    private String name;
//
//    @ToString.Include
//    @EqualsAndHashCode.Include
//    @Column(name = "productSku")
//    private String sku;
//
//    @Column(name = "productWeight")
//    private Double weight;
//
//    @Column(name = "productWeightUOM")
//    private String weightUom;
//
//    @Column(name = "productHeight")
//    private Double height;
//
//    @Column(name = "productWidth")
//    private Double width;
//
//    @Column(name = "productLength")
//    private Double length;
//
//    @Column(name = "productLwhUOM")
//    private String lwhUom;
//
//    @Column(name = "dosageValue")
//    private String dosageValue;
//
//    @Column(name = "dosageType")
//    private String dosageType;
//
//    @Column(name = "itemsInPack")
//    private String itemsInPack;
//
//    @Column(name = "packType")
//    private String packType;
//
//    @Column(name = "isCold")
//    private Boolean isCold;
//
//    @Column(name = "productUnavailable")
//    private Integer productUnavailable;
//
//    @Column(name = "productShortDescription")
//    private String productShortDescription;
//
//    @Column(name = "productLongDescription")
//    private String productLongDescription;
//
//    @Column(name = "brandName")
//    private String brandName;
//
//    @Column(name = "categoryType")
//    private String categoryType;
//
//    @Column(name = "isLicensed")
//    private boolean isLicensed;
//
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private List<ProductBatchEntity> productBatchEntity;
//
//    @EqualsAndHashCode.Exclude
//    @ToString.Exclude
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    private List<WarehouseInventoryEntity> warehouseInventoryEntities;
//}
