package com.example.helper;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.example.beans.Order;
import com.mongodb.BasicDBObject;

@Component
public class PrepareOrder {

	public Order prepareOrder(Order order) throws InterruptedException {
		Date now = new Date();
		System.out.println("Order " + order.getOrder() + " for table " + order.getTable() + " came to kitchen " + now);
		if (NumberUtils.isNumber(order.getTimeToPrepare())) {
			float prepTime = Float.parseFloat(order.getTimeToPrepare());
			Thread.currentThread().sleep((long) (prepTime * 60 * 1000));
			order.setPreparedAt(new Date());
		}
		System.out.println("Order " + order.getOrder() + " for table " + order.getTable()
				+ " ordered came in kitchen at " + now + " preparation completed at " + order.getPreparedAt());
		return order;
	}

	public String getEmailContent(List<BasicDBObject> orders) {
		StringBuffer mailContent = new StringBuffer();
		mailContent.append("Unprocessed order for ").append(new Date()).append("\n");
		if (orders != null && orders.size() > 0) {
			for (BasicDBObject order : orders) {
				mailContent.append(order.getString("order")).append(" ordered at table ")
						.append(order.getString("table")).append(" on ").append(new Date(order.getLong("timeOfOrder")))
						.append("\n");
			}
		} else {
			mailContent.append("None");
		}
		return mailContent.toString();
	}
}
