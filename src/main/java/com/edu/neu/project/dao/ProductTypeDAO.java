package com.edu.neu.project.dao;

import com.edu.neu.project.entity.ProductType;
import org.springframework.stereotype.Repository;

@Repository
public class ProductTypeDAO extends AbstractHibernateDao<ProductType> {


    public ProductType add(String typeName) {
        return this.create(new ProductType().setTypeName(typeName));
    }

    public ProductType add(String typeName, String typeDescription) {
        return this.create(new ProductType().setTypeName(typeName).setTypeDescription(typeDescription));
    }
}
