package firedetection.mc.test;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
import com.ibm.ia.testdriver.data.EventSource;

import firedetection.mc.ConceptFactory;

public class FireDetectionTest {

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
	public void test1CriteriaPerMinute() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String room = "102";
		Event event = testDriver.getConceptFactory(ConceptFactory.class).createSmokeEvent(room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);

		event = testDriver.getConceptFactory(ConceptFactory.class).createCarbonDioxydeEvent(400, room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);
		
		event = testDriver.getConceptFactory(ConceptFactory.class).createCarbonMonoxydeEvent(400, room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);
		
		event = testDriver.getConceptFactory(ConceptFactory.class).createFlameEvent(room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);

		event = testDriver.getConceptFactory(ConceptFactory.class).createHeatEvent(room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);
		
		event = testDriver.getConceptFactory(ConceptFactory.class).createRORHeatEvent(room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);
		
		event = testDriver.getConceptFactory(ConceptFactory.class).createTemperatureEvent(220, room, time);
		testDriver.submitEvent(event);
		time = time.plusMinutes(1);
		
		testDriver.processPendingSchedules(time.plusMinutes(2));
		assertNoDetection(debugReceiver);
	}
	

	@Test
	public void test3CriteriaOnePer10s() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String room = "102";
		Event event = testDriver.getConceptFactory(ConceptFactory.class).createSmokeEvent(room, time);
		testDriver.submitEvent(event);
		time = time.plusSeconds(10);

		event = testDriver.getConceptFactory(ConceptFactory.class).createCarbonDioxydeEvent(400, room, time);
		testDriver.submitEvent(event);
		time = time.plusSeconds(10);
		
		event = testDriver.getConceptFactory(ConceptFactory.class).createCarbonMonoxydeEvent(400, room, time);
		testDriver.submitEvent(event);
		time = time.plusSeconds(10);

		
		testDriver.processPendingSchedules(time.plusMinutes(1));
		
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		printEmittedEvents(debugInfo);
		assertEquals(1, debugInfo.length);
		assertEquals("firedetection.mc.FireEvent", debugInfo[0].getEvent().get$TypeName());
		
		
	}
	
	private void printEmittedEvents(DebugInfo[] debugInfo) {
		for (DebugInfo info : debugInfo) {
			String json;
		try {
			json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
			System.out.println(json);
		} catch (SolutionException | UnsupportedOperationException | GatewayException e) {
			e.printStackTrace();
		} 
		}
	}
	
	private void assertNoDetection(IADebugReceiver debugReceiver) {
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		printEmittedEvents(debugInfo);
		assertEquals(debugInfo.length, 0);
		
	}
	
}