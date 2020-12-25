package com.edu.neu.project.serviceimpl;

import com.edu.neu.project.dao.ProductDAO;
import com.edu.neu.project.dao.ProductImageInfoDAO;
import com.edu.neu.project.dao.ProductTypeDAO;
import com.edu.neu.project.entity.Product;
import com.edu.neu.project.entity.ProductImageInfo;
import com.edu.neu.project.entity.ProductType;
import com.edu.neu.project.model.ProductInfo;
import com.edu.neu.project.service.ProductService;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@Getter(AccessLevel.PROTECTED)
public class ProductServiceImpl implements ProductService {


    private ProductDAO productDAO;
    private ProductTypeDAO productTypeDAO;
    private ProductImageInfoDAO productImageInfoDAO;

    @Value("${PATH_TO_USERIMAGEFOLDER}")
    private @Getter(AccessLevel.PRIVATE)
    String userImageFolderPath;

    @Value("${IMGNOTFOUND}")
    private @Getter(AccessLevel.PRIVATE)
    String imgNotFoundFileName;

    static ProductInfo generateProductInfoByProduct(Product product) {

        return new ProductInfo(product.getProductID(),
                product.getName(),
                product.getPrice(),
                product.getProductType().getTypeID(),
                product.getDescription());
    }

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    public void setProductTypeDAO(ProductTypeDAO productTypeDAO) {
        this.productTypeDAO = productTypeDAO;
    }

    @Autowired
    public void setProductImageInfoDAO(ProductImageInfoDAO productImageInfoDAO) {
        this.productImageInfoDAO = productImageInfoDAO;
    }

    @Override
    public Product saveProduct(Product product) {
        return this.getProductDAO().create(product);
    }

    @Override
    public Product saveProduct(ProductInfo productInfo) {
        Product product = this.getProductDAO().create(generateProductByProductInfo(productInfo));
        productInfo.setProductID(product.getProductID());
        return product;
    }

    @Override
    public Product getProductByProductID(int productID) {
        return this.getProductDAO().findOne(productID);
    }

    @Override
    public List<Product> getAllProduct() {
        return this.productDAO.findAll();
    }

    @Override
    public List<Product> getAllProductByProductTypeID(Integer productTypeID) {
        return this.productTypeDAO.findOne(productTypeID).getProduct();
    }

    @Override
    public List<String> getAllProductNames() {
        return this.getAllProduct().stream().map(Product::getName).collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductByProductName(String name) {
        return this.productDAO.findAllBySimilarName(name);
    }

    @Override
    public ProductInfo getProductInfoByProductID(int productID) {
        Product product = this.getProductByProductID(productID);
        if (product == null) return null;
        return generateProductInfoByProduct(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return this.getProductDAO().update(product);

    }

    @Override
    public Product updateProductWithoutImgChange(ProductInfo productInfo) {
        return Optional.ofNullable(this.getProductByProductID(productInfo.getProductID()))
                .map(p -> p
                        .setName(productInfo.getName())
                        .setPrice(productInfo.getPrice())
                        .setProductType(productTypeDAO.findOne(productInfo.getProductTypeID()))
                        .setDescription(productInfo.getDescription())
                )
                .orElse(null);
    }

    @Override
    public void deleteProduct(Product product) {
        this.getProductDAO().delete(product);
    }

    @Override
    public void deleteProduct(int productID) {
        this.getProductDAO().deleteById(productID);
    }

    @Override
    public ProductType addProductType(String productTypeName) {
        return this.productTypeDAO.add(productTypeName);

    }

    @Override
    public ProductType addProductType(String productTypeName, String typeDescription) {
        return this.productTypeDAO.add(productTypeName, typeDescription);

    }

    @Override
    public Map getAllProductType() {

        return Optional
                .ofNullable(this.productTypeDAO.findAll())
                .orElseGet(Collections::emptyList)
                .stream()
                .sorted(Comparator.comparing(ProductType::getTypeID))
                .collect(Collectors
                        .toMap(ProductType::getTypeID,
                                ProductType::getTypeName,
                                (v1, v2) -> v2,
                                LinkedHashMap::new));

    }

    @Override
    public ProductImageInfo saveImageInfo(ProductImageInfo productImageInfo) {
        return this.productImageInfoDAO.create(productImageInfo);
    }

    @Override
    public ProductImageInfo getImageInfo(int productID) {
        return this.productImageInfoDAO.findOne(productID);
    }

    @Override
    public ProductImageInfo updateImageInfo(ProductImageInfo productImageInfo) {
        return this.productImageInfoDAO.update(productImageInfo);
    }

    @Override
    public void deleteImageInfo(ProductImageInfo productImageInfo) {
        this.productImageInfoDAO.delete(productImageInfo);
    }

    @Override
    public void deleteImageInfoByID(int productID) {
        this.productImageInfoDAO.deleteById(productID);
    }

    private void saveImgToFile(@NotNull CommonsMultipartFile file, String fileName, String dir) throws IOException {
        Path filepath = Paths.get(dir, fileName);
        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
            os.flush();
        }
    }

    @Override
    public void saveImgToFileAndLinkToProduct(CommonsMultipartFile file, Product product, Function<Product, String> fileNameGenerator) throws IOException {
        String imgFileName = fileNameGenerator.apply(product);
        if (imgFileName.equals("")) imgFileName = this.getImgNotFoundFileName();
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
//        System.out.println(imgFileName + "  " + suffix);
        this.saveImgToFile(file, imgFileName + suffix, this.userImageFolderPath);
        ProductImageInfo productImageInfo = new ProductImageInfo().setFilename(imgFileName).setSuffix(suffix).setProduct(product);
        //product.setProductImageInfo(productImageInfo);
        this.saveImageInfo(productImageInfo);
        product.setProductImageInfo(productImageInfo);
    }

    @Override
    public void updateImgToFileAndLinkToProduct(CommonsMultipartFile file, Product product, Function<Product, String> fileNameGenerator) throws IOException {
        String imgFileName = fileNameGenerator.apply(product);
        if (imgFileName.equals("")) imgFileName = this.getImgNotFoundFileName();
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
//        System.out.println(imgFileName + "  " + suffix);
        this.saveImgToFile(file, imgFileName + suffix, this.userImageFolderPath);
        product.getProductImageInfo().setFilename(imgFileName).setSuffix(suffix).setProduct(product);
    }

    @Override
    public byte[] loadImgFromFile(Integer productID) throws IOException {
        try {
            ProductImageInfo productImageInfo = this.productImageInfoDAO.findOne(productID);

            Path path = Paths.get(this.getUserImageFolderPath(), productImageInfo.getFilename() + productImageInfo.getSuffix());
//            System.out.println(path);
            InputStream inputStream = Files.newInputStream(path);
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            Path path = Paths.get(this.getUserImageFolderPath(), this.getImgNotFoundFileName());
            InputStream inputStream = Files.newInputStream(path);
            return IOUtils.toByteArray(inputStream);
        }
    }

    public void printFibonacciSequence(int length) {
        System.out.println(
                Stream.iterate(new long[]{0, 1}, pair -> new long[]{pair[1], pair[0] + pair[1]})
                        .limit(length)
                        .map(pair -> Long.toString(pair[1]))
                        .collect(Collectors.joining(", ")));
    }

    private Product generateProductByProductInfo(ProductInfo productInfo) {
        return new Product().setName(productInfo.getName())
                .setPrice(productInfo.getPrice())
                .setProductType(productTypeDAO.findOne(productInfo.getProductTypeID()))
                .setDescription(productInfo.getDescription());
    }

}
