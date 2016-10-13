package com.example.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.Application;
import com.example.beans.Order;
import com.example.splitter.OrderSplitter;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:external-test.properties")
public class UDPRouteTest extends CamelTestSupport {

	@EndpointInject(uri = "direct:udpSource")
	protected ProducerTemplate udpSource;

	@EndpointInject(uri = "mock:takeOrders")
	protected MockEndpoint takeOrdersEndpoint;

	@Autowired
	private OrderSplitter orderSplitter;

	/**
	 * JNDI registry stores beans used by Camel.
	 */
	private JndiRegistry registry;

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		if (registry == null) {
			registry = super.createRegistry();
			registry.bind("orderSplitter", orderSplitter);
		}
		return registry;
	}

	@Test
	@DirtiesContext
	public void testRoute() throws InterruptedException {
		udpSource.sendBodyAndHeader("order1:HDRINK:2:1:1", "testHeader", "");
		takeOrdersEndpoint.expectedMessageCount(1);
		takeOrdersEndpoint.message(0).predicate(m -> {
			Order order = (Order) m.getIn().getBody();
			return order.getOrder().equals("order1") && order.isOrderADrink();
		});
		takeOrdersEndpoint.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {

		RouteBuilder testBuilder = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:udpSource").split().method("orderSplitter").to("mock:takeOrders");
				//from("")
			}
		};
		return new RouteBuilder[] { testBuilder };
	}

}
