package com.edu.neu.project.controller;

import com.edu.neu.project.exception.UsernameAlreadyExistException;
import com.edu.neu.project.model.UserAccountInfo;
import com.edu.neu.project.service.ProductService;
import com.edu.neu.project.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
public class HomeController {
    private ProductService productService;

    private UserService userService;
    @Value("${PATH_TO_USERIMAGEFOLDER}")
    private @Getter(AccessLevel.PRIVATE)
    String userImageFolderPath;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home"})
    public String homePage() {
//        System.out.println(AuthorityEnum.Customer);
        return "redirect:/listproduct";
    }

    @GetMapping("/test")
    public String test(Principal principal) {
//        System.out.println(principal.toString());
        return "_test";
    }

    @GetMapping("/listproduct")
    public ModelAndView listProduct(ModelAndView mv) throws IOException {
        mv.setViewName("productlist");
        mv.addObject("producttype", this.productService.getAllProductType());
        return mv;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @GetMapping("/registration")
    public ModelAndView userRegistration(ModelAndView mv, UserAccountInfo userAccountInfo) {
        mv.setViewName("registration");
        mv.addObject("useraccountinfo", userAccountInfo);
        return mv;
    }

    @PostMapping("/registration")
    public ModelAndView registration(ModelAndView mv, @ModelAttribute("useraccountinfo") @Valid UserAccountInfo userAccountInfo, BindingResult result) {
        System.out.println("userAccountInfo = " + userAccountInfo.toString());
        if (result.hasErrors()) {
//            System.out.println("haserror");
            mv.setViewName("registration");
            return mv;
        }
        try {
            this.userService.registerCustomer(userAccountInfo);
        } catch (UsernameAlreadyExistException usernameAlreadyExistException) {
            usernameAlreadyExistException.printStackTrace();
            mv.addObject("useraccountinfo", userAccountInfo);
            mv.addObject("usernameexist", true);
            mv.setViewName("registration");
            return mv;
        }
        mv.setViewName("redirect:/home");
        return mv;
    }


}
