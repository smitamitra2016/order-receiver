package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty4.codec.DatagramPacketDecoder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.example.helper.PrepareOrder;
import com.example.routes.UDPRoute;
import com.example.splitter.OrderSplitter;
import com.mongodb.Mongo;

@Configuration
@SpringBootApplication
@PropertySource(value = "classpath:external.properties")
public class Application {

	@Value("${mongo.host}")
	private String mongoHost;

	@Value("${mongo.port}")
	private String mongoPort;

	@Bean
	public DatagramPacketDecoder datagramPacketDecoder() {
		return new DatagramPacketDecoder();
	}

	@Bean
	public OrderSplitter orderSplitter() {
		return new OrderSplitter();
	}

	@Bean
	public PrepareOrder prepareOrder() {
		return new PrepareOrder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public Mongo mongoBean() {
		int port = -1;
		Mongo mongo = null;
		if (StringUtils.isNumeric(mongoPort)) {
			mongo = new Mongo(mongoHost, Integer.parseInt(mongoPort));
		}
		return mongo;
	}

	@Bean
	public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
		SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
		camelContext.addRoutes(routeBuilder());
		return camelContext;
	}

	@Bean
	public RouteBuilder routeBuilder() {
		return new UDPRoute();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		/*
		 * try { JndiContext jndiContext = new JndiContext();
		 * jndiContext.bind("orderSplitter", new OrderSplitter());
		 * jndiContext.bind("PrepareOrder", new PrepareOrder());
		 * jndiContext.bind("DatagramPacketDecoder", new
		 * DatagramPacketDecoder()); CamelContext context = new
		 * DefaultCamelContext(jndiContext); context.addRoutes(new UDPRoute());
		 * context.start(); ProducerTemplate template =
		 * context.createProducerTemplate(); template.sendBody("direct:order",
		 * "sandwich:N:1:2"); template.sendBody("direct:order",
		 * "coffee:Y:10:1"); template.sendBody("direct:order", "salad:N:1:2");
		 * Thread.sleep(10000); context.stop(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}
}
