package org.example.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.pojo.Order;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderVo extends Order {
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_picture")
    private String productPicture;
}
