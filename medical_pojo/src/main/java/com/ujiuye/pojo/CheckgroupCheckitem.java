package com.ujiuye.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 检查组与检查项的中间表,一个检查组有多个检查项
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_checkgroup_checkitem")
public class CheckgroupCheckitem extends Model {

    private static final long serialVersionUID = 1L;

    private Integer checkgroupId;

    private Integer checkitemId;


}
