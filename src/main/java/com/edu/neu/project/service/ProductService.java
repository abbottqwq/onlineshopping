package com.edu.neu.project.service;

import com.edu.neu.project.entity.Product;
import com.edu.neu.project.entity.ProductImageInfo;
import com.edu.neu.project.entity.ProductType;
import com.edu.neu.project.model.ProductInfo;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public interface ProductService {

// setproducttypeid


    Product saveProduct(Product product);

    Product saveProduct(ProductInfo productInfo);

    Product getProductByProductID(int productID);

    List<Product> getAllProduct();

    List<Product> getAllProductByProductTypeID(Integer productTypeID);

    List<String> getAllProductNames();

    List<Product> getProductByProductName(String name);

    ProductInfo getProductInfoByProductID(int productID);

    Product updateProduct(Product product);

    Product updateProductWithoutImgChange(ProductInfo productInfo);

    void deleteProduct(Product product);

    void deleteProduct(int productID);

    ProductType addProductType(String productTypeName);

    ProductType addProductType(String productTypeName, String typeDescription);


    Map getAllProductType();

    ProductImageInfo saveImageInfo(ProductImageInfo productImageInfo);

    ProductImageInfo getImageInfo(int productID);

    ProductImageInfo updateImageInfo(ProductImageInfo productImageInfo);

    void deleteImageInfo(ProductImageInfo productImageInfo);

    void deleteImageInfoByID(int productID);

    //void saveImgToFile(CommonsMultipartFile file, String fileName, String dir) throws IOException;

    void saveImgToFileAndLinkToProduct(CommonsMultipartFile file, Product product, Function<Product, String> fileNameGenerator) throws IOException;

    void updateImgToFileAndLinkToProduct(CommonsMultipartFile file, Product product, Function<Product, String> fileNameGenerator) throws IOException;

    byte[] loadImgFromFile(Integer productID) throws IOException;


}
