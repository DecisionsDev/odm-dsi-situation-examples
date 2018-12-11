package promotest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acme.store.CancelPromotion;
import com.acme.store.ConceptFactory;
import com.acme.store.NewPromotion;
import com.acme.store.Store;
import com.acme.store.StoreInit;
import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.DataParseException;
import com.ibm.ia.common.EventFactory;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.MultipleEventSubmitException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;


public class PromoTest {

	protected static TestDriver testDriver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDriver = new TestDriver();
		testDriver.connect();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDriver.disconnect();
		testDriver = null;
	}

	@Before
	public void setUp() throws Exception {
		testDriver.deleteAllEntities();
		testDriver.resetSolutionState();
		testDriver.startRecording();
	}

	@After
	public void tearDown() throws Exception {
        testDriver.endTest();
		testDriver.stopRecording();
	}
	
	@Test
	public void testRegularPromotion() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		final ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		StoreInit creationEvent = conceptFactory.createStoreInit(time);
		Relationship<Store> mainstore = testDriver.createRelationship(Store.class, "main store");
		creationEvent.setStore(mainstore);
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(1);
		
		NewPromotion newPromotion = conceptFactory.createNewPromotion(time);
		newPromotion.setStartDate(time.plusMonths(1));
		newPromotion.setEndDate(newPromotion.getStartDate().plusDays(12));
		newPromotion.setRegion("Arizona");
		newPromotion.setStore(mainstore);
		
		testDriver.submitEvent(newPromotion);
		
		testDriver.processPendingSchedules(time.plusMonths(3));
		
		testDriver.waitUntilSolutionIdle();
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		Arrays.stream(debugInfo, 0, debugInfo.length - 2).forEach(info ->
		 {
			String json;
			try {
				json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
				System.out.println(json);
			} catch (SolutionException | UnsupportedOperationException | GatewayException e) {
				e.printStackTrace();
			} 
			assertEquals("com.acme.store.PromotionInXDays",info.getEvent().get$TypeName());
		});
		Event should_be_start = debugInfo[debugInfo.length - 2].getEvent();
		Event should_be_end = debugInfo[debugInfo.length - 1].getEvent();
		assertEquals("com.acme.store.PromotionBegins", should_be_start.get$TypeName());
		assertEquals(newPromotion.getStartDate().withNano(0), should_be_start.get$Timestamp());
		assertEquals("com.acme.store.PromotionEnds", should_be_end.get$TypeName());
		assertEquals(newPromotion.getEndDate().withNano(0), should_be_end.get$Timestamp());
		
		
	}
	

	@Test
	public void testPromotionAnnouncedLate() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		final ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		StoreInit creationEvent = conceptFactory.createStoreInit(time);
		Relationship<Store> mainstore = testDriver.createRelationship(Store.class, "main store");
		creationEvent.setStore(mainstore);
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(1);
		
		NewPromotion newPromotion = conceptFactory.createNewPromotion(time);
		newPromotion.setStartDate(time.plusDays(2));
		newPromotion.setEndDate(newPromotion.getStartDate().plusDays(12));
		newPromotion.setRegion("Arizona");
		newPromotion.setStore(mainstore);
		
		testDriver.submitEvent(newPromotion);
		
		testDriver.processPendingSchedules(time.plusMonths(3));
		
		testDriver.waitUntilSolutionIdle();
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		Arrays.stream(debugInfo, 0, debugInfo.length - 2).forEach(info ->
		 {
			String json;
			try {
				json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
				System.out.println(json);
			} catch (SolutionException | UnsupportedOperationException | GatewayException e) {
				e.printStackTrace();
			} 
			assertEquals("com.acme.store.PromotionInXDays",info.getEvent().get$TypeName());
		});
		Event should_be_start = debugInfo[debugInfo.length - 2].getEvent();
		Event should_be_end = debugInfo[debugInfo.length - 1].getEvent();
		assertEquals("com.acme.store.PromotionBegins", should_be_start.get$TypeName());
		assertEquals(newPromotion.getStartDate().withNano(0), should_be_start.get$Timestamp());
		assertEquals("com.acme.store.PromotionEnds", should_be_end.get$TypeName());
		assertEquals(newPromotion.getEndDate().withNano(0), should_be_end.get$Timestamp());
		
		
	}
	

	@Test
	public void testCancelPromotionInFarFuture() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		final ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		StoreInit creationEvent = conceptFactory.createStoreInit(time);
		Relationship<Store> mainstore = testDriver.createRelationship(Store.class, "main store");
		creationEvent.setStore(mainstore);
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(1);
		
		NewPromotion newPromotion = conceptFactory.createNewPromotion(time);
		newPromotion.setStartDate(time.plusMonths(1));
		newPromotion.setEndDate(newPromotion.getStartDate().plusDays(12));
		newPromotion.setRegion("Arizona");
		newPromotion.setStore(mainstore);
		
		testDriver.submitEvent(newPromotion);
		
		CancelPromotion cancelPromotion = conceptFactory.createCancelPromotion(time.plusDays(2).plusHours(1));
		cancelPromotion.setRegion("Arizona");
		cancelPromotion.setStore(mainstore);
		
		testDriver.submitEvent(cancelPromotion);
		testDriver.processPendingSchedules(time.plusMonths(3));
		
		testDriver.waitUntilSolutionIdle();
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		Arrays.stream(debugInfo).forEach(info ->
		 {
			String json;
			try {
				json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
				System.out.println(json);
			} catch (SolutionException | UnsupportedOperationException | GatewayException e) {
				e.printStackTrace();
			} 
			assertEquals("com.acme.store.PromotionInXDays",info.getEvent().get$TypeName());
			assertTrue(info.getEvent().get$Timestamp().isBefore(cancelPromotion.get$Timestamp()));
		});
	}
	

	@Test
	public void testCancelPromotionInNearFuture() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		final ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		StoreInit creationEvent = conceptFactory.createStoreInit(time);
		Relationship<Store> mainstore = testDriver.createRelationship(Store.class, "main store");
		creationEvent.setStore(mainstore);
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(1);
		
		NewPromotion newPromotion = conceptFactory.createNewPromotion(time);
		newPromotion.setStartDate(time.plusDays(2));
		newPromotion.setEndDate(newPromotion.getStartDate().plusDays(12));
		newPromotion.setRegion("Arizona");
		newPromotion.setStore(mainstore);
		
		testDriver.submitEvent(newPromotion);
		
		CancelPromotion cancelPromotion = conceptFactory.createCancelPromotion(time.plusDays(1).plusHours(1));
		cancelPromotion.setRegion("Arizona");
		cancelPromotion.setStore(mainstore);
		
		testDriver.submitEvent(cancelPromotion);
		testDriver.processPendingSchedules(time.plusMonths(3));
		
		testDriver.waitUntilSolutionIdle();
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		Arrays.stream(debugInfo).forEach(info ->
		 {
			String json;
			try {
				json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
				System.out.println(json);
			} catch (SolutionException | UnsupportedOperationException | GatewayException e) {
				e.printStackTrace();
			} 
			assertEquals("com.acme.store.PromotionInXDays",info.getEvent().get$TypeName());
			assertTrue(info.getEvent().get$Timestamp().isBefore(cancelPromotion.get$Timestamp()));
		});
	}

	
}