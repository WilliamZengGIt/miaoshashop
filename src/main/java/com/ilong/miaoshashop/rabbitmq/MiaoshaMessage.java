package com.ilong.miaoshashop.rabbitmq;

import com.ilong.miaoshashop.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MiaoshaMessage {
	private User user;
	private long productId;

}
