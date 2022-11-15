package org.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user") //mybatis-plus 指定实体类对应的表名
public class User implements Serializable {
    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @JsonProperty("user_id")//jackson注解，用于进行属性格式化
    private Integer userId;

    private String userName;

    // 忽略属性，不生成 json，不接受 json 数据 @JsonIgnore
    // @JsonInclude(JsonInclude.Include.NOT_NULL) + null 这个值不为 null 的时候生成 json，为 null 不生成
    // 不影响接受 json
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userPhonenumber;
}
