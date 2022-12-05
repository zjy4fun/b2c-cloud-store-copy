package org.example.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pojo.Product;
import org.example.pojo.Cart;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CartVo implements Serializable {
    private Integer id;
    private Integer productID;
    private String productName;
    private String productImg;
    private Double price;
    private Integer num;
    private Integer maxNum;
    private Boolean check = false;
    public CartVo(Product product, Cart cart){
        this.id = cart.getId();
        this.productID = product.getProductId();
        this.productName = product.getProductName();
        this.productImg = product.getProductPicture();
        this.price = product.getProductSellingPrice();
        this.num = cart.getNum();
        this.maxNum = product.getProductNum();
        this.check = false;
    }
}
