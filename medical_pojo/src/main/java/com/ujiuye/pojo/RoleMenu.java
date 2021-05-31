package com.ujiuye.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表与菜单表的中间表,一个角色有多个菜单
 * </p>
 *
 * @author encan
 * @since 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_role_menu")
public class RoleMenu extends Model {

    private static final long serialVersionUID = 1L;

      private Integer roleId;

    private Integer menuId;


}
