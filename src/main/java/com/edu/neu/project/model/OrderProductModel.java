package com.edu.neu.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductModel {
    private int productId;
    private String name;
    private BigDecimal price;
    private int quantity;

    public BigDecimal getAmount() {
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }
}
