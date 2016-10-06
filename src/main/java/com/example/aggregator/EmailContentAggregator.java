package com.example.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.mongodb.BasicDBObject;

public class EmailContentAggregator implements AggregationStrategy{

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Message newIn = newExchange.getIn();
		Object newBody = newIn.getBody();
		if (oldExchange != null){
			 Message in = oldExchange.getIn();
			 List<BasicDBObject> oldBody = in.getBody(ArrayList.class);
			 oldBody.addAll((List<BasicDBObject>)newBody);
		 }
		 else{
			 return newExchange;
		 }
		 return oldExchange;
	}

}
