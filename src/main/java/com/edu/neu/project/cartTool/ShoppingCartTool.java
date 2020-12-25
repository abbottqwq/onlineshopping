package com.edu.neu.project.cartTool;

import com.edu.neu.project.model.ShoppingCart;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ShoppingCartTool {
    public static ShoppingCart getShoppingCartFromSession(HttpServletRequest request) {
        return (ShoppingCart) Optional.ofNullable(request.getSession().getAttribute("shoppingCart"))
                .orElseGet(() -> {
                    ShoppingCart sc = new ShoppingCart();
                    request.getSession().setAttribute("shoppingCart", sc);
                    return sc;
                });
    }
}
