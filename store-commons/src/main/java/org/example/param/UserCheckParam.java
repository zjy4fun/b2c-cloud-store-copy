package org.example.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 接收前端参数的 param
 * TODO：要使用 jsr 303 的注解进行校验
 *
 * @NotBlank    字符串不能为 null 和 空字符串 ""
 * @NotNull     字符串不能为 null
 * @NotEmpty    集合长度不能为 0
 */
@Data
public class UserCheckParam {

    @NotBlank//不允许为空
    private String userName; //注意：参数名称要等于前端传递的 JSON key 的名称

}
