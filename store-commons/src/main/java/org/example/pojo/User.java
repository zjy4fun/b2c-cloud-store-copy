package org.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user") //mybatis-plus 指定实体类对应的表名
public class User implements Serializable {
    public static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String password;
    private String userPhoneNumber;
}
