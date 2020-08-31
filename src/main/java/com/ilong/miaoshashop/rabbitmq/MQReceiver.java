package com.ilong.miaoshashop.rabbitmq;

import com.ilong.miaoshashop.entity.OrderMiaosha;
import com.ilong.miaoshashop.entity.User;
import com.ilong.miaoshashop.entity.vo.ProductVo;
import com.ilong.miaoshashop.redis.RedisService;
import com.ilong.miaoshashop.service.MiaoshaService;
import com.ilong.miaoshashop.service.OrderService;
import com.ilong.miaoshashop.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
		RedisService redisService;

		@Autowired
		ProductService productService;
		
		@Autowired
		OrderService orderService;
		
		@Autowired
		MiaoshaService miaoshaService;
		
		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			MiaoshaMessage mm  = RedisService.stringToBean(message, MiaoshaMessage.class);
			User user = mm.getUser();
			long productId = mm.getProductId();

			ProductVo product = productService.getProductVoByProductId(productId);
			int stock = product.getProductStock();
	    	if(stock <= 0) {
	    		return;
	    	}

	    	//判断是否已经秒杀到了
			OrderMiaosha order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), productId);
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	miaoshaService.miaosha(user, product);
		}

	/*	@RabbitListener(queues=MQConfig.QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
		}*/

		@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
	public void receiveTopic1(String message) {
		log.info(" topic  queue1 message:"+message);
		}

		@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
	public void receiveTopic2(String message) {
			log.info(" topic  queue2 message:"+message);
	}

		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
	public void receiveHeaderQueue(byte[] message) {
			log.info(" header  queue message:"+new String(message));
		}

		
}
