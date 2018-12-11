package missing;

import static org.junit.Assert.*;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acme.missing.AbsenceOfSensorEvent;
import com.acme.missing.BrokenSensorEvent;
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

public class TestMissingEvent {

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
	public void test1() throws SolutionException, GatewayException,
			RoutingException, InterruptedException, MultipleEventSubmitException, UnsupportedOperationException, DataParseException {

		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		EventSource<Event> eventSource = testDriver.createEventSequenceEventSource("SensorEvent.eseq");
		testDriver.submitEvents(eventSource.iterator());
		testDriver.processPendingSchedules("2018-04-12T11:00+02:00");
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			String json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
			System.out.println(json);
		}
		Event ev = debugInfo[0].getEvent();
		assertEquals("com.acme.missing.AbsenceOfSensorEvent",ev.get$TypeName());
		assertEquals(OffsetDateTime.parse("2018-04-11T11:00:02+02:00"), ev.get$Timestamp().toOffsetDateTime());
		//assertTrue( ev instanceof AbsenceOfSensorEvent);
		//assertTrue( debugInfo[1].getEvent() instanceof BrokenSensorEvent);
		
		
	}
	
}