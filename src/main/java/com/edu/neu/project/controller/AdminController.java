package com.edu.neu.project.controller;

import com.edu.neu.project.authority.AuthorityEnum;
import com.edu.neu.project.exception.UsernameAlreadyExistException;
import com.edu.neu.project.model.UserAccountInfo;
import com.edu.neu.project.service.ProductService;
import com.edu.neu.project.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Transactional
@RequestMapping("/admin")
public class AdminController {
    private ProductService productService;

    private UserService userService;
    @Value("${PATH_TO_USERIMAGEFOLDER}")
    private @Getter(AccessLevel.PRIVATE)
    String userImageFolderPath;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/registerproductmanager")
    public ModelAndView gotoregisterproductmanager(ModelAndView mv) {
        mv.setViewName("registerproductmanager");
        return mv;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/registerproductmanager")
    public ModelAndView registration(ModelAndView mv, @ModelAttribute("useraccountinfo") @Valid UserAccountInfo userAccountInfo, BindingResult result) {
//        System.out.println("userAccountInfo = " + userAccountInfo.toString());
        if (result.hasErrors()) {
//            System.out.println("haserror");
            mv.setViewName("registration");
            return mv;
        }
        try {
            this.userService.registerProductManager(userAccountInfo);
        } catch (UsernameAlreadyExistException usernameAlreadyExistException) {
            usernameAlreadyExistException.printStackTrace();
            mv.addObject("useraccountinfo", userAccountInfo);
            mv.addObject("usernameexist", true);
            mv.setViewName("registration");
            return mv;
        }
        mv.setViewName("redirect:/account/accountinfo?rpms=true");
        return mv;
    }

    @GetMapping(value = "/getproductmanagerusernamelist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getProductManagerUsernameList() {
//        System.out.println("in getproductmanagerusernamelist");
        return userService.getAllProductManagerUsername();
    }

    @PostMapping(value = "/deleteproductmanager", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> deleteProductManagerbyUsername(@RequestParam("productmanagerusername") String name) {
        Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(this.userService.getUserAccountByUsername(name)).ifPresentOrElse(
                userAccount -> {
                    if (!userAccount.getAuthority().equals(AuthorityEnum.Manager)) {
                        map.put("error", "Authority Error");
                    } else {
                        try {
                            this.userService.deleteUserAccount(userAccount);
                            map.put("success", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            map.put("error", "Delete Error");
                        }
                    }
                },
                () -> {
                    map.put("error", "No Such User");
                }
        );
//        System.out.println(map.toString());
        return map;
    }

}
