package uk.gov.digital.ho.systemregister.application.messaging;

import javax.enterprise.context.ApplicationScoped;
import com.google.common.eventbus.EventBus;
import uk.gov.digital.ho.systemregister.application.MissingAuthorException;

@ApplicationScoped
public class SR_EventBus {
    private final EventBus instance;

    public SR_EventBus() {
        instance = new EventBus();
    }

    public void publish(AuthoredMessage msg) throws MissingAuthorException {
        if(msg.author == null || msg.author.isValid() == false)
            throw new MissingAuthorException("Publishing a command or event without an author is not allowed");
        instance.post(msg);
    }

    public void subscribe(Object handler) {
        instance.register(handler);
    }
}
