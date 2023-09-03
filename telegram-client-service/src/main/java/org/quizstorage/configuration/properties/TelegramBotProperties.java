package org.quizstorage.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class TelegramBotProperties {

    private String username;

    private String token;

}
