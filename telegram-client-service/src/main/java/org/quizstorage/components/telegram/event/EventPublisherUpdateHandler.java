package org.quizstorage.components.telegram.event;

import lombok.RequiredArgsConstructor;
import org.quizstorage.components.telegram.UpdateHandler;
import org.quizstorage.components.telegram.event.events.UpdateEvent;
import org.quizstorage.components.telegram.event.UpdateEventResolver;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class EventPublisherUpdateHandler implements UpdateHandler {

    private final ApplicationEventPublisher eventPublisher;

    private final UpdateEventResolver updateEventResolver;

    @Override
    public void handle(Update update) {
        UpdateEvent event = updateEventResolver.resolve(this, update);
        eventPublisher.publishEvent(event);
    }

}
