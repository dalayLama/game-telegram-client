package org.quizstorage.components.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quizstorage.components.common.ErrorMessage;
import org.quizstorage.components.telegram.DialogService;
import org.quizstorage.utils.UserResolvable;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ExceptionsCatcher {

    private final DialogService dialogService;

    @Pointcut("within(@org.quizstorage.annotations.CatchException *) && execution(public * *(..))")
    public void annotatedClass() {}

    @Pointcut("@annotation(org.quizstorage.annotations.CatchException)")
    public void annotatedMethod() {}

    @AfterThrowing(value = "annotatedClass() || annotatedMethod()", throwing = "e")
    public void handleException(Throwable e) {
        log.warn("Caught an exception \"{}\", cause - \"{}\", proceeding",
                e.getClass().getName(),
                Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("null")
        );
        Long userId = e instanceof UserResolvable ? ((UserResolvable) e).getUserId() : null;
        Optional.ofNullable(userId).ifPresentOrElse(
                id -> handleForUser(id, e),
                () -> log.warn("The error isn't user resolvable, throwing as is")
        );
    }


    private void handleForUser(Long userId, Throwable e) {
        log.warn("User has been determined - \"{}\", sending an error message", userId);
        try {
            if (e instanceof MessageSourceResolvable) {
                dialogService.sendMessage(userId, (MessageSourceResolvable) e);
            } else {
                dialogService.sendMessage(userId, ErrorMessage.UNKNOWN_ERROR, e.getMessage());
            }
        } catch (Throwable ee) {
            log.error(ee.getMessage(), ee);
        }


    }

}
