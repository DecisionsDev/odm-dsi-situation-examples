package firedetection2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.OffsetDateTime;

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
	public void testSmokeOnly() throws SolutionException, GatewayException,
			RoutingException, InterruptedException, MultipleEventSubmitException, RuntimeException, DataParseException {

		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		EventSource<Event> eventSource = testDriver.createEventSequenceEventSource("smoke.eseq");
		testDriver.submitEvents(eventSource.iterator());
		testDriver.processPendingSchedules("2018-04-12T11:10+02:00");
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			System.out.println(info.getEvent());
		}
		assertEquals(debugInfo.length, 0);
	}
	

	@Test
	public void testHighTemperatureOnly() throws SolutionException, GatewayException,
			RoutingException, InterruptedException, MultipleEventSubmitException, RuntimeException, DataParseException {

		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		EventSource<Event> eventSource = testDriver.createEventSequenceEventSource("hightemperature.eseq");
		testDriver.submitEvents(eventSource.iterator());
		testDriver.processPendingSchedules("2018-04-12T11:10+02:00");
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			System.out.println(info.getEvent());
		}
		assertEquals(debugInfo.length, 0);
	}
	

	@Test
	public void testFire() throws SolutionException, GatewayException,
			RoutingException, InterruptedException, MultipleEventSubmitException, RuntimeException, DataParseException {

		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		EventSource<Event> eventSource = testDriver.createEventSequenceEventSource("fire.eseq");
		testDriver.submitEvents(eventSource.iterator());
		testDriver.processPendingSchedules("2018-04-12T11:10+02:00");
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			System.out.println(info.getEvent());
		}
		assertEquals(debugInfo.length, 1);
		Event ev = debugInfo[0].getEvent();
		assertEquals("firedetection2.FireEvent",ev.get$TypeName());
		assertEquals(OffsetDateTime.parse("2018-04-10T11:00:06+02:00"), ev.get$Timestamp().toOffsetDateTime());
	}

	@Test
	public void testFireOnlyOnce() throws SolutionException, GatewayException,
			RoutingException, InterruptedException, MultipleEventSubmitException, RuntimeException, DataParseException {

		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		EventSource<Event> eventSource = testDriver.createEventSequenceEventSource("firefire.eseq");
		testDriver.submitEvents(eventSource.iterator());
		testDriver.processPendingSchedules("2018-04-12T11:10+02:00");
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			System.out.println(info.getEvent());
		}
		assertEquals(debugInfo.length, 1);
		Event ev = debugInfo[0].getEvent();
		assertEquals("firedetection2.FireEvent",ev.get$TypeName());
		assertEquals(OffsetDateTime.parse("2018-04-10T11:00:06+02:00"), ev.get$Timestamp().toOffsetDateTime());
	}
}