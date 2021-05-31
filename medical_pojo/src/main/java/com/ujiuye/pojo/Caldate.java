package com.ujiuye.pojo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JuBoxin
 * @date 2021/5/24 - 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Caldate extends Model {
    private Integer date;
    private Integer number;
    private Integer reservation;
}
