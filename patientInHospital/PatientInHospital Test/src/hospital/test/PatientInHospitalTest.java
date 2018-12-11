package hospital.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.OffsetDateTime;
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

import hospital.ConceptFactory;
import hospital.PatientCreationEvent;

public class PatientInHospitalTest {

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
	public void testTwoDays() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		PatientCreationEvent creationEvent = testDriver.getConceptFactory(ConceptFactory.class).createPatientCreationEvent(time);
		creationEvent.setSsn(ssn);
		creationEvent.setName("John Smith");
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(2); // easier to read in monitor
		testDriver.waitUntilSolutionIdle();
		Event event = testDriver.getConceptFactory(ConceptFactory.class).createPatientAdmission(ssn, time);
		testDriver.submitEvent(event);
		time = time.plusDays(2);
		
		testDriver.processPendingSchedules(time);
		time = time.plusHours(3);
		event = testDriver.getConceptFactory(ConceptFactory.class).createPatientRelease(ssn, time);
		testDriver.submitEvent(event);
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			String json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
			System.out.println(json);
			assertEquals("hospital.DailyNotification",info.getEvent().get$TypeName());
		}
		assertEquals(2, debugInfo.length);
	}
	

	@Test
	public void testLessThanADay() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		PatientCreationEvent creationEvent = testDriver.getConceptFactory(ConceptFactory.class).createPatientCreationEvent(time);
		creationEvent.setSsn(ssn);
		creationEvent.setName("John Smith");
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(2); // easier to read in monitor
		testDriver.waitUntilSolutionIdle();
		Event event = testDriver.getConceptFactory(ConceptFactory.class).createPatientAdmission(ssn, time);
		testDriver.submitEvent(event);
		time = time.plusHours(2);
		
		testDriver.processPendingSchedules(time);
		time = time.plusHours(3);
		event = testDriver.getConceptFactory(ConceptFactory.class).createPatientRelease(ssn, time);
		testDriver.submitEvent(event);
		assertNoDetection(debugReceiver);
	}
	

	@Test
	public void testTwoDaysDuplicateAdmission() throws Exception {
		IADebugReceiver debugReceiver = new IADebugReceiver();
		testDriver.addDebugReceiver(debugReceiver);
		ZonedDateTime time = ZonedDateTime.now();
		String ssn = "0123456789";

		PatientCreationEvent creationEvent = testDriver.getConceptFactory(ConceptFactory.class).createPatientCreationEvent(time);
		creationEvent.setSsn(ssn);
		creationEvent.setName("John Smith");
		testDriver.submitEvent(creationEvent);
		
		time = time.plusHours(2); // easier to read in monitor
		testDriver.waitUntilSolutionIdle();
		Event event = testDriver.getConceptFactory(ConceptFactory.class).createPatientAdmission(ssn, time);
		testDriver.submitEvent(event);
		testDriver.submitEvent(event);
		time = time.plusDays(2);
		
		testDriver.processPendingSchedules(time);
		time = time.plusHours(3);
		event = testDriver.getConceptFactory(ConceptFactory.class).createPatientRelease(ssn, time);
		testDriver.submitEvent(event);
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			String json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
			System.out.println(json);
			assertEquals("hospital.DailyNotification",info.getEvent().get$TypeName());
		}
		assertEquals(2, debugInfo.length);
	}
	
	
	private void assertNoDetection(IADebugReceiver debugReceiver) throws Exception {
		DebugInfo[] debugInfo = debugReceiver.getDebugInfo("*");
		for (DebugInfo info : debugInfo) {
			String json = testDriver.getEventFactory().serializeEvent(DataFormat.JSON, info.getEvent());
			System.out.println(json);
		}
		assertEquals(debugInfo.length, 0);
	}
	
}