package com.example.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.Application;
import com.example.beans.Order;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class PrepareOrderTest {

	@Autowired
	private PrepareOrder prepareOrder;

	@Test
	public void testPrepareOrder() throws InterruptedException {
		Order newOrder = new Order("Item1", "Food", "0.2", 1, 2, new Date());
		Order order = prepareOrder.prepareOrder((newOrder));
		assertNotNull("Time when preperation complete is not null", order.getPreparedAt());
		assertTrue("Time taken to prepare greater than or equal to timeToPrepare of order",
				order.getPreparedAt().getTime() - newOrder.getTimeOfOrder().getTime() >= 0.2 * 60000);

	}
	
	@Test
	public void testGetEmailContent(){
		List<BasicDBObject> dbCollection = new ArrayList<>();
		BasicDBObject object1 = Mockito.mock(BasicDBObject.class);
		dbCollection.add(object1);
		when(object1.getString("order")).thenReturn("Item1");
		when(object1.getString("table")).thenReturn("1");
		when(object1.getLong("timeOfOrder")).thenReturn(1475683603407L);//Wed Oct 05 17:06:43 BST 2016
		//System.out.println(prepareOrder.getEmailContent(dbCollection));
		assertTrue(prepareOrder.getEmailContent(dbCollection).indexOf("Item1 ordered at table 1 on Wed Oct 05 17:06:43 BST 2016")>0);
		
	}
}
