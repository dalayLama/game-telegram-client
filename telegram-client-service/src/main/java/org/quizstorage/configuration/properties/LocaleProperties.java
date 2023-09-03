package org.quizstorage.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "locale")
@Getter
@Setter
public class LocaleProperties {

    private String language;

    private String country;

}
