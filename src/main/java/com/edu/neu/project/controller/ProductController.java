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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@Transactional
@RequestMapping(value = "/product")
public class ProductController {


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


    @GetMapping(value = "/image/{id}")
    @ResponseBody
    public byte[] img(HttpServletResponse response, @PathVariable Integer id) throws IOException {
        //System.out.println("in image controller");
        return this.productService.loadImgFromFile(id);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ProductInfo> listAllProduct(@RequestParam int productTypeID) {
        List<Product> productList;
        if (productTypeID < 0)
            productList = this.productService.getAllProduct();
        else
            productList = this.productService.getAllProductByProductTypeID(productTypeID);
        return productList.stream()
                .sorted(Comparator.comparing(Product::getModifyDate).reversed())
                .map(p -> new ProductInfo()
                        .setName(p.getName())
                        .setProductID(p.getProductID())
                        .setPrice(p.getPrice())
                        .setProductTypeName(p.getProductType().getTypeName())
                        .setDescription(p.getDescription())
                        .setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(p.getModifyDate()))
                )
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/getproductnamelist")
    @ResponseBody
    public List<String> listAllProductName() {
        //this.productService.getAllProductNames().forEach(System.out::println);
        return this.productService.getAllProductNames();
    }

    @GetMapping(value = "/searchbyproductname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Product> searchByProductName(@RequestParam String name) {
        return this.productService.getProductByProductName(name);
    }

}
