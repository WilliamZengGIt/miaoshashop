package com.ilong.miaoshashop.entity.vo;

import com.ilong.miaoshashop.validator.IsMobile;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 20:46
 */
@Data
@NoArgsConstructor
public class RegisterVo {
    @NotNull
    @IsMobile
    private String userTel;

    @NotNull
    @Length(min=8)
    private String userPwd;
}
