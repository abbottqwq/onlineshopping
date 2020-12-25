package com.edu.neu.project.model;

import com.edu.neu.project.validationannotation.NoXSSString;
import com.edu.neu.project.validationannotation.ValidNameInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {
    private int productID;
    @ValidNameInput
    private String name;
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 20, fraction = 2)
    @NotNull
    private BigDecimal price;
    private int productTypeID;
    @NoXSSString
    private String description;
    @NotNull
    private CommonsMultipartFile imgFile;
    private int quantity = 0;
    private String productTypeName;
    private String lastModifiedDate;


    public ProductInfo(int productID, String name, BigDecimal price, int productTypeID, String description) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.productTypeID = productTypeID;
        this.description = description;
    }

    public ProductInfo(String name, BigDecimal price, int productTypeID, String description) {
        this.productID = -1;
        this.name = name;
        this.price = price;
        this.productTypeID = productTypeID;
        this.description = description;
    }


}
