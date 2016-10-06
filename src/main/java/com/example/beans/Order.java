package com.example.beans;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;

@Component
public class Order extends BasicDBObject implements Serializable {

	private Object _id;
	private String order;
	private String orderType;
	private int count;
	private int table;
	private Date timeOfOrder;
	private Date preparedAt;
	private String timeToPrepare;

	public String getTimeToPrepare() {
		return timeToPrepare;
	}

	public void setTimeToPrepare(String timeToPrepare) {
		this.timeToPrepare = timeToPrepare;
	}

	public Object get_id() {
		return _id;
	}

	public void set_id(Object _id) {
		this._id = _id;
	}

	public Date getPreparedAt() {
		return preparedAt;
	}

	public void setPreparedAt(Date preparedAt) {
		this.preparedAt = preparedAt;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTable() {
		return table;
	}

	public void setTable(int table) {
		this.table = table;
	}

	public Date getTimeOfOrder() {
		return timeOfOrder;
	}

	public void setTimeOfOrder(Date timeOfOrder) {
		this.timeOfOrder = timeOfOrder;
	}

	public Order(String order, String orderType, String timeToPrepare, int count, int table, Date timeOfOrder) {
		super();
		this.order = order;
		this.orderType = orderType;
		this.count = count;
		this.table = table;
		this.timeOfOrder = timeOfOrder;
		this.timeToPrepare = timeToPrepare;
	}

	public static boolean isOrderADrink(Order o) {
		return o.getOrderType().endsWith("DRINK");
	}

	public static String typeOfDrink(Order o) {
		return StringUtils.substring(o.getOrderType(), 0, 1);
	}

	public String getOrderDetails() {
		return timeOfOrder + ":" + order + ":" + timeToPrepare;
	}

	@Override
	public String toString() {
		return "Order [order=" + order + ", orderType=" + orderType + ", count=" + count + ", table=" + table
				+ ", timeOfOrder=" + timeOfOrder + "]";
	}

}
