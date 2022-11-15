package org.example.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 地址移除参数
 */
@Data
public class AddressRemoveParam {

    @NotNull
    private Integer id;
}
