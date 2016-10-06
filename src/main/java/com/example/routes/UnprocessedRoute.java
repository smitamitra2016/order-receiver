package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.aggregator.EmailContentAggregator;
import com.example.expressions.OrderCorrelationExpression;

@Component
public class UnprocessedRoute extends RouteBuilder {

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

		from("quartz://myGroupName/myTimerName?cron=0+0+11+*+*+?").setBody().constant("{ \"preparedAt\": null }")
				.multicast().to("seda:getFood").to("seda:getDrink");

		from("seda:getFood").to(
				"mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoFoodCollection + "&operation=findAll")
				.to("seda:sendEmail");
		from("seda:getDrink").to(
				"mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoDrinkCollection + "&operation=findAll")
				.to("seda:sendEmail");

		from("seda:sendEmail").aggregate(new OrderCorrelationExpression(),new EmailContentAggregator()).completionInterval(60000).to("bean:prepareOrder?method=getEmailContent")
		.to("smtps://smtp.gmail.com?username=smita.mitra.nomadrail@gmail.com&password=Jan@2016&from=smita.mitra.nomadrail@gmail.com&to=smita.mitra.nomadrail@gmail.com&subject=Unproccesed orders");

	}

}
