package com.ujiuye.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * <p>
 * 员工表
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDate birthday;

    private String gender;

    private String username;

    private String password;

    private String remark;

    private String station;

    private String telephone;


}
