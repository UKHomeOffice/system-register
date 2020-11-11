//package uk.gov.digital.ho.systemregister.test.application.messaging;
//
//import org.junit.jupiter.api.Test;
//import uk.gov.digital.ho.systemregister.application.MissingAuthorException;
//import uk.gov.digital.ho.systemregister.application.messaging.SR_EventBus;
//import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
//import uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent;
//
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class SR_EventBusTest {
//    @Test
//    public void canNOTPublisMonitoredWithNoAuthor(){
//        SR_EventBus eventBus = new SR_EventBus();
//
//        SR_Event evt = new SystemAddedEvent();
//
//        Exception exception = assertThrows(MissingAuthorException.class, () -> {
//            eventBus.publish(evt);
//        });
//
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains("Publishing a command or event without an author is not allowed"));
//    }
//}
