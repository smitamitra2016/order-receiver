package com.example.helper;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.example.beans.Order;

@Component
public class PrepareOrder {

	public Order prepareOrder(Order order) throws InterruptedException {
		Date now = new Date();
		System.out.println("Order "+ order.getOrder() + " for table " + order.getTable() + " came to kitchen "+now);
		Thread.currentThread().sleep(45000);
		order.setPreparedAt(new Date());
		System.out.println("Order " + order.getOrder() + " for table " + order.getTable() + " ordered came in kitchen at "
				+ now + " prepared at " + order.getPreparedAt() + "   "+Thread.currentThread().getId());
		return order;
	}
}
