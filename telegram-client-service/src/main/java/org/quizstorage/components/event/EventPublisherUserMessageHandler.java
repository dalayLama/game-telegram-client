package org.quizstorage.components.event;

import lombok.RequiredArgsConstructor;
import org.quizstorage.annotations.CatchException;
import org.quizstorage.annotations.WrapUserResolvableException;
import org.quizstorage.components.event.events.UpdateEvent;
import org.quizstorage.components.telegram.UserMessageHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class EventPublisherUserMessageHandler implements UserMessageHandler {

    private final ApplicationEventPublisher eventPublisher;

    private final UpdateEventResolver updateEventResolver;

    @Override
    @WrapUserResolvableException
    @CatchException
    public void handle(Update update) {
        UpdateEvent event = updateEventResolver.resolve(this, update);
        eventPublisher.publishEvent(event);
    }

}
