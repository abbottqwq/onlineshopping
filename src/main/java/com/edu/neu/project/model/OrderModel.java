package com.edu.neu.project.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class OrderModel {
    private Set<OrderProductModel> orderProducts;
    private Date modifyDate;

    public OrderModel(Set<OrderProductModel> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderModel() {
        this.orderProducts = new HashSet<>();
    }

    public List<OrderProductModel> getOrderProductModelsOrderByProductID() {
        return this.orderProducts.stream()
                .sorted(Comparator.comparing(OrderProductModel::getProductId))
                .collect(Collectors.toList());
    }

    public OrderModel add(OrderProductModel orderProduct) {
        this.orderProducts.add(orderProduct);
        return this;
    }

    public OrderModel remove(OrderProductModel orderProduct) {
        this.orderProducts.remove(orderProduct);
        return this;
    }

    public BigDecimal getTotalAmount() {
        return this.orderProducts.stream()
                .map(OrderProductModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
