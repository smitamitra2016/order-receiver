package com.example.splitter;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.beans.Order;

@Component
public class OrderSplitter {
	
	private static final Logger log = LoggerFactory.getLogger(OrderSplitter.class);
	
	public Order splitBody(String body) {
		log.info(body);
		String[] parts = body.split(":");// order:isDrink:count:table
		Order order = new Order(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), new Date());
		order.set_id(order.getTimeOfOrder());
		return order;
	}
}
