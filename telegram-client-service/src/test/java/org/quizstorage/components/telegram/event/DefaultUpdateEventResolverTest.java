package org.quizstorage.components.telegram.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quizstorage.components.event.DefaultUpdateEventResolver;
import org.quizstorage.components.event.events.CallbackQueryEvent;
import org.quizstorage.components.event.events.CommandEvent;
import org.quizstorage.components.event.events.SimpleMessageEvent;
import org.quizstorage.components.event.events.UpdateEvent;
import org.quizstorage.exceptions.UpdateEventResolvingException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultUpdateEventResolverTest {

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private CallbackQuery callbackQuery;

    @InjectMocks
    private DefaultUpdateEventResolver resolver;

    @Test
    void shouldResolveToCommandEvent() {
        given(update.getMessage()).willReturn(message);
        given(message.isCommand()).willReturn(true);

        UpdateEvent result = resolver.resolve(this, update);

        assertThat(result).isExactlyInstanceOf(CommandEvent.class);
    }

    @Test
    void shouldResolveToSimpleMessageEvent() {
        given(update.getMessage()).willReturn(message);
        given(message.isCommand()).willReturn(false);

        UpdateEvent result = resolver.resolve(this, update);

        assertThat(result).isExactlyInstanceOf(SimpleMessageEvent.class);
    }

    @Test
    void shouldResolveToCallbackQueryEvent() {
        given(update.getMessage()).willReturn(null);
        given(update.getCallbackQuery()).willReturn(callbackQuery);

        UpdateEvent result = resolver.resolve(this, update);

        assertThat(result).isExactlyInstanceOf(CallbackQueryEvent.class);
    }

    @Test
    void shouldThrowUpdateEventResolvingException() {
        given(update.getMessage()).willReturn(null);
        given(update.getCallbackQuery()).willReturn(null);

        assertThatThrownBy(() -> resolver.resolve(this, update))
                .isInstanceOf(UpdateEventResolvingException.class);
    }

}