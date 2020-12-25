package com.edu.neu.project.serviceimpl;

import com.edu.neu.project.dao.OrderDAO;
import com.edu.neu.project.dao.OrderProductDAO;
import com.edu.neu.project.dao.ProductDAO;
import com.edu.neu.project.dao.UserAccountDAO;
import com.edu.neu.project.entity.Order;
import com.edu.neu.project.entity.OrderProduct;
import com.edu.neu.project.entity.UserAccount;
import com.edu.neu.project.exception.NoSuchProductID;
import com.edu.neu.project.exception.NoSuchUserAccountID;
import com.edu.neu.project.exception.NoSuchUsername;
import com.edu.neu.project.model.ShoppingCart;
import com.edu.neu.project.service.OrderService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Getter(AccessLevel.PROTECTED)
public class OrderServiceImpl implements OrderService {

    private ProductDAO productDAO;
    private UserAccountDAO userAccountDAO;
    private OrderDAO orderDAO;
    private OrderProductDAO orderProductDAO;

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    public void setUserAccountDAO(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Autowired
    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Autowired
    public void setOrderProductDAO(OrderProductDAO orderProductDAO) {
        this.orderProductDAO = orderProductDAO;
    }

    @Override
    public Order generateOrder(ShoppingCart shoppingCart) {
        Order order = new Order().setOrderProducts(shoppingCart.getMap()
                .entrySet()
                .stream()
                .map(entry -> new OrderProduct()
                        .setProduct(
                                Optional.ofNullable(
                                        this.productDAO.findOne(entry.getKey())
                                ).orElseThrow(NoSuchProductID::new)
                        )
                        .setQuantity(entry.getValue()))
                .collect(Collectors.toSet())
        );
        order.getOrderProducts().forEach(orderProduct -> orderProduct.setOrder(order));
        return order;
    }

    @Override
    public Order saveOrder(Order order, String username) {
        return this.orderDAO.create(order.setUserAccount(this.userAccountDAO.findByUsername(username)));
    }

    @Override
    public Order checkout(ShoppingCart shoppingCart, String username) {
        if(shoppingCart.getMap().isEmpty()) return null;
        Order order = this.saveOrder(generateOrder(shoppingCart), username);
        shoppingCart.empty();
        return order;
    }

    @Override
    public Set<Order> getAllOrdersByUserAccountID(int accountID) {
        return Optional.ofNullable(this.userAccountDAO.findOne(accountID))
                .map(UserAccount::getOrders)
                .orElseThrow(NoSuchUserAccountID::new);
    }

    @Override
    public Set<Order> getAllOrdersByUsername(String userName) {
        return Optional.ofNullable(this.userAccountDAO.findByUsername(userName))
                .map(UserAccount::getOrders)
                .orElseThrow(NoSuchUsername::new);

    }

    @Override
    public List<Order> getAllOrdersByUserAccountIDOrderByDate(int accountID) {
        return this.getAllOrdersByUserAccountID(accountID)
                .stream()
                .sorted(Comparator.comparing(Order::getModifyDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllOrdersByUsernameOrderByDate(String userName) {
        return this.getAllOrdersByUsername(userName)
                .stream()
                .sorted(Comparator.comparing(Order::getModifyDate).reversed())
                .collect(Collectors.toList());
    }


}
