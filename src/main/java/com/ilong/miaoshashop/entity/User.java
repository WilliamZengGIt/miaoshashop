package com.ilong.miaoshashop.entity;

import com.ilong.miaoshashop.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * TOOD
 *
 * @author long
 * @date 2020-01-14 19:36
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_user")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String userName;

    // 微信唯一标识
    @Column
    private String openId;

    //用户角色   。是否允许后台登陆，10001，普通用户 2，会员。10003，管理员
    @Column
    private Integer userRole;

    @Column
    private String userPwd;

    @Column
    private  String userSalt;//用户校验码

    // 用户手机号码。  用户后台管理登陆账号
    @Column
    private String userTel;

    @Column
    private String wxName;

    @Column
    private String wxAvatarurl;

    // 性别  0位置 1男 2女
    @Column
    private Integer wxGender;

    @Column
    private String wxProvince;

    @Column
    private String wxCity;

    @Column
    private String wxCountry;

    @Column
    private Integer fkCompanyId;

    @CreatedDate
    @Column
    private Date created;
    @CreatedDate
    @Column
    private Date updated;


}
