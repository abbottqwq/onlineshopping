package com.edu.neu.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "product_image_info")
@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageInfo implements Serializable {

    @Id
    @Column(name = "product_id", nullable = false)
    @GeneratedValue(generator = "foreign")
    @GenericGenerator(name = "foreign", strategy = "foreign", parameters = {
            @Parameter(name = "property", value = "product")
    })
    private Integer productID;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "suffix")
    private String suffix = ".jpg";

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProductImageInfo{");
        sb.append("productID=").append(productID);
        sb.append(", filename='").append(filename).append('\'');
        sb.append(", suffix='").append(suffix).append('\'');
        sb.append(", productid=").append(Optional.ofNullable(this.getProduct()).map(Product::getProductID).orElse(null));
        sb.append('}');
        return sb.toString();
    }
}
