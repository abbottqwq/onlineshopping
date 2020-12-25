package com.edu.neu.project.service;

import com.edu.neu.project.entity.Order;
import com.edu.neu.project.entity.OrderProduct;
import com.edu.neu.project.entity.Product;
import com.edu.neu.project.model.OrderModel;
import com.edu.neu.project.model.OrderProductModel;
import com.edu.neu.project.model.ShoppingCart;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface OrderService {
    static OrderModel orderToOrderModel(Order order) {
        return new OrderModel().setOrderProducts(order.getOrderProducts()
                .stream()
                .map(OrderService::orderProductToOrderProductModel)
                .collect(Collectors.toSet())
        ).setModifyDate(order.getModifyDate());
    }

    static OrderProductModel orderProductToOrderProductModel(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        return new OrderProductModel()
                .setName(product.getName())
                .setProductId(product.getProductID())
                .setPrice(product.getPrice())
                .setQuantity(orderProduct.getQuantity());
    }

    Order saveOrder(Order order, String username);

    Order checkout(ShoppingCart shoppingCart, String username);

    Order generateOrder(ShoppingCart shoppingCart);

    Set<Order> getAllOrdersByUserAccountID(int accountID);

    Set<Order> getAllOrdersByUsername(String userName);

    List<Order> getAllOrdersByUserAccountIDOrderByDate(int accountID);

    ;

    List<Order> getAllOrdersByUsernameOrderByDate(String userName);

    ;

}
