package com.ilong.miaoshashop.base;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    @Column
    private Long id;
    @CreatedDate
    @Column
    private Date created;
    @CreatedDate
    @Column
    private Date updated;

}
