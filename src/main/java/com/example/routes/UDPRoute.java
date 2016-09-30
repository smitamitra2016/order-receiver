package com.example.routes;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.beans.Order;

@Component
public class UDPRoute extends RouteBuilder {

	@Value("${udp.url}")
	private String udpurl;

	@Value("${mongo.db}")
	private String mongoDb;

	@Value("${mongo.food.collection}")
	private String mongoFoodCollection;

	@Value("${mongo.drinks.collection}")
	private String mongoDrinkCollection;

	@Override
	public void configure() throws Exception {
		Predicate predicate = method(Order.class, "isOrderADrink");
		String foodUrl = "mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoFoodCollection;
		String drinksUrl = "mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoDrinkCollection;
		from("netty4:" + udpurl + "?decoder=#datagramPacketDecoder&sync=false").split().method("orderSplitter")
				.to("seda:takeOrders?multipleConsumers=true");
		from("seda:takeOrders?multipleConsumers=true").multicast().choice().when(predicate)
				.to(drinksUrl + "&operation=save").otherwise().to(foodUrl + "&operation=save").end()
				.to("seda:prepareOrders?multipleConsumers=true");
		from("seda:prepareOrders?multipleConsumers=true&concurrentConsumers=15")
				.to("bean:prepareOrder?method=prepareOrder").multicast().choice().when(predicate)
				.to(drinksUrl + "&operation=save").otherwise().to(foodUrl + "operation=save").end()
				.to("seda:serveOrder");
	}

}
