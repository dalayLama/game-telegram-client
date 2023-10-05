package org.quizstorage.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:messages/commands/descriptions",
                "classpath:messages/errors/errors",
                "classpath:messages/info/info"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
