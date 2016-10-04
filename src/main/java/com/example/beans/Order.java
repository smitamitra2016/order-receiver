package com.example.beans;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Order implements Serializable {

	private Object _id;
	private String order;
	private String orderType;
	private int count;
	private int table;
	private Date timeOfOrder;
	private Date preparedAt;
	
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

	public Order(String order, String orderType, int count, int table, Date timeOfOrder) {
		super();
		this.order = order;
		this.orderType = orderType;
		this.count = count;
		this.table = table;
		this.timeOfOrder = timeOfOrder;
	}

	public static boolean isOrderADrink(Order o) {
		return o.getOrderType().endsWith("DRINK");
	}

	@Override
	public String toString() {
		return "Order [order=" + order + ", orderType=" + orderType + ", count=" + count + ", table=" + table
				+ ", timeOfOrder=" + timeOfOrder + "]";
	}

}
