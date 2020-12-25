package com.edu.neu.project.dao;

import com.edu.neu.project.entity.Product;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class ProductDAO extends AbstractHibernateDao<Product> {

//    public Product create(ProductInfo productInfo){
//        Product product = new Product()
//                .setName(productInfo.getName())
//                .setPrice(productInfo.getPrice())
//                .setProductType(this.getCurrentSession()
//                        .getReference(ProductType.class, productInfo.getProductTypeID()))
//                .setDescription(productInfo.getDescription());
//        return this.create(product);
//    }


    public List findAllBySimilarName(String name) {
        Session session = this.getCurrentSession();
        Criteria crit = session.createCriteria(Product.class);
        crit.add(Restrictions.ilike("name", "%" + name + "%"));
        return crit.list();
    }

//    public List findAllByProductTypeID(Integer productTypeID){
//        CriteriaQuery cq = this.getCriteriaQuery();
//        Root root = this.getRoot();
//
//    }


}
