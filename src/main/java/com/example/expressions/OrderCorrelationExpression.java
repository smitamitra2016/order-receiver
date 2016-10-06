package com.example.expressions;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

public class OrderCorrelationExpression implements Expression{

	@Override
	public <T> T evaluate(Exchange exchange, Class<T> type) {
		// TODO Auto-generated method stub
		return (T)exchange.getFromRouteId();
	}

}
