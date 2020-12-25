package com.edu.neu.project.controller;


import com.edu.neu.project.cartTool.ShoppingCartTool;
import com.edu.neu.project.model.ShoppingCart;
import com.edu.neu.project.service.OrderService;
import com.edu.neu.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    private ProductService productService;
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/addtocart")
    @ResponseBody
    public boolean addToCart(@RequestParam(value = "productID") int productID, @RequestParam(value = "quantity", defaultValue = "0") int quantity, HttpServletRequest request) {
        try {
            ShoppingCart sc = ShoppingCartTool.getShoppingCartFromSession(request);
            sc.add(productID, quantity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @GetMapping("/showshoppingcart")
    public ModelAndView showShoppingCart(HttpServletRequest request, ModelAndView mv) {
        mv.addObject("shoppingcart",
                ShoppingCartTool.getShoppingCartFromSession(request).getMap()
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> this.productService.getProductInfoByProductID(e.getKey()).setQuantity(e.getValue()))
                        .collect(Collectors.toList()))
                .setViewName("showshoppingcart");

        return mv;
    }

    @PostMapping(value = "/updateshoppingcart")
    public String updateCart(@RequestParam(value = "quantity[]", required = false) int[] p, @RequestParam(value = "id[]", required = false) int[] id, HttpServletRequest request) {
        if (p != null && id != null) {
            ShoppingCart sc = ShoppingCartTool.getShoppingCartFromSession(request);
            if (sc != null)
                for (int i = 0; i < id.length; i++)
                    sc.setQuantityByID(id[i], p[i]);
        }
        return "redirect:/customer/showshoppingcart";
    }

    @GetMapping("/checkout")
    public String Checkout() {
//        System.out.println("in get");
        return "checkout";
    }

    @PostMapping("/checkout")
    public ModelAndView CheckoutRes(ModelAndView mv, @RequestParam("result") String result, HttpServletRequest request, Principal principal) {
        if (result.equals("success")) {
            ShoppingCart shoppingCart = ShoppingCartTool.getShoppingCartFromSession(request);
            this.orderService.checkout(shoppingCart, principal.getName());
            mv.setViewName("redirect:/customer/showshoppingcart");
            return mv;
        } else {
            mv.setViewName("redirect:/customer/showshoppingcart?checkfail=true");
            return mv;
        }

    }

    @GetMapping("/orderhistory")
    public ModelAndView gotoOrderHistory(ModelAndView mv, Principal principal) {
        mv.addObject("ordermodel", this.orderService
                .getAllOrdersByUsernameOrderByDate(principal.getName())
                .stream()
                .map(OrderService::orderToOrderModel)
                .collect(Collectors.toList())
        );
        mv.setViewName("orderhistory");
        return mv;
    }

}
