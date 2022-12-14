package org.example.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddressListParam {
    @NotNull
    @JsonProperty("user_id")  //接收的时候指定属性 user_id
    private Integer userId;
}
