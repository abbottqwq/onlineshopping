package com.edu.neu.project.controller;

import com.edu.neu.project.entity.Product;
import com.edu.neu.project.model.ProductInfo;
import com.edu.neu.project.service.ProductService;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Controller
@Transactional
@RequestMapping("/productmanager")
public class ProductManagerController {

    private ProductService productService;
    private @Getter(AccessLevel.PRIVATE)
    Function<Product, String> productImgFileNameGenerator;
    @Value("${PATH_TO_USERIMAGEFOLDER}")
    private @Getter(AccessLevel.PRIVATE)
    String userImageFolderPath;
    private Map productTypeMap;

    @Autowired
    public void setProductService(ProductService productService) {
//        System.out.println("productService");
        this.productService = productService;
        this.productTypeMap = productService.getAllProductType();
    }

    @Autowired
    public void setProductImgFileNameGenerator(Function<Product, String> productImgFileNameGenerator) {
        this.productImgFileNameGenerator = productImgFileNameGenerator;
    }

    @GetMapping("/upload")
    public ModelAndView gotoPage(ModelAndView mv) {
//        System.out.println("in get");
        mv.setViewName("uploadproduct");
        mv.addObject("producttype", this.productTypeMap);
        mv.addObject("productinfoform", new ProductInfo());
        return mv;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView uploadProduct(@ModelAttribute("productinfoform") @Valid ProductInfo productInfo, BindingResult result,
                                      ModelAndView mv) throws IOException {
//        System.out.println("in post");
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            mv.addObject("producttype", this.productTypeMap);
            mv.addObject("productinfoform", productInfo);
            mv.setViewName("uploadproduct");
        } else {

            Product product = productService.saveProduct(productInfo);
//            System.out.println(product);
            CommonsMultipartFile cmf = productInfo.getImgFile();
            productService.saveImgToFileAndLinkToProduct(cmf, product, this.getProductImgFileNameGenerator());
            mv.setViewName("redirect:/account/accountinfo?addproductsuccessfully=true");//addproductsuccessfully
        }
        return mv;
    }

    @GetMapping(value = "/manageproduct")
    public ModelAndView getManageProduct(@RequestParam("id") int productId, ModelAndView mv) {
        Optional.ofNullable(productService.getProductInfoByProductID(productId))
                .ifPresentOrElse(p -> {
                            mv.addObject("producttype", this.productService.getAllProductType());
                            mv.addObject("productinfo", p);
                            mv.setViewName("manageproduct");
                        },
                        () -> {
                            mv.addObject("error", "no such product");
                            mv.setViewName("redirect:/listproduct");
                        }
                );
        return mv;
    }

    @PostMapping(value = "/manageproduct", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ModelAndView manageProduct(ModelAndView mv, @ModelAttribute("productinfo") @Valid ProductInfo productInfo, BindingResult result,
                                      @RequestParam("isimagechanged") boolean isImageChanged) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            mv.addObject("producttype", this.productService.getAllProductType());
            mv.setViewName("manageproduct");

        } else
            Optional.ofNullable(this.productService.updateProductWithoutImgChange(productInfo))
                    .ifPresentOrElse(product -> {
                                if (isImageChanged) {
                                    try {
                                        CommonsMultipartFile cmf = productInfo.getImgFile();

                                        productService.updateImgToFileAndLinkToProduct(cmf, product, this.getProductImgFileNameGenerator());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mv.setViewName("redirect:/listproduct?updatesuccessfully=true");
                            },
                            () -> {
                                mv.addObject("producttype", this.productService.getAllProductType());
                                mv.addObject("updatefail", true);
                                mv.setViewName("manageproduct");
                            });
        return mv;
    }

}
