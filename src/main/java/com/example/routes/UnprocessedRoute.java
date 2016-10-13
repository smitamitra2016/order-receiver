package com.example.routes;

import java.util.Date;

import org.apache.camel.Exchange;
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

	@Value("${ftp.path}")
	private String filePath;

	@Value("${ftp.host}")
	private String ftpHost;

	@Value("${ftp.user}")
	private String ftpUser;

	@Value("${ftp.pwd}")
	private String ftpPwd;

	@Override
	public void configure() throws Exception {

		from("quartz://unprocessedOrder/timer?cron=0+35+14+*+*+?").setBody().constant("{ \"preparedAt\": null }")
				.multicast().to("seda:getFood").to("seda:getDrink");

		from("seda:getFood").to(
				"mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoFoodCollection + "&operation=findAll")
				.to("seda:saveUnprocessed");
		from("seda:getDrink").to(
				"mongodb:mongoBean?database=" + mongoDb + "&collection=" + mongoDrinkCollection + "&operation=findAll")
				.to("seda:saveUnprocessed");

		from("seda:saveUnprocessed").aggregate(new OrderCorrelationExpression(), new EmailContentAggregator())
				.completionInterval(60000).multicast().to("direct:sendMail").to("direct:zipContent");

		from("direct:zipContent").setHeader(Exchange.FILE_NAME, constant(new Date() + ".txt"))
				.convertBodyTo(String.class).marshal().zipFile().to("sftp://" + ftpHost + filePath + "?username="
						+ ftpUser + "&password=" + ftpPwd + "&fileName=Unprocessed_" + new Date() + ".zip");

		from("direct:sendMail").to("bean:prepareOrder?method=getEmailContent").to(
				"smtps://smtp.gmail.com?username=smita.mitra.nomadrail@gmail.com&password=Jan@2016&from=smita.mitra.nomadrail@gmail.com&to=smita.mitra.nomadrail@gmail.com&subject=Unproccesed orders");

	}

}
