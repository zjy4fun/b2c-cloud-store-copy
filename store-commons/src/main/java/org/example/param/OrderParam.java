package org.example.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.vo.CartVo;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderParam implements Serializable {

    public static final Long serialVersionUID = 1L;

    @JsonProperty("user_id")
    private Integer userId;
    private List<CartVo> products;
}
