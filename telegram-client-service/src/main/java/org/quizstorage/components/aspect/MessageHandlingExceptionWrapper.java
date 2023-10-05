package org.quizstorage.components.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quizstorage.components.common.ErrorMessage;
import org.quizstorage.exceptions.MessageHandlingException;
import org.quizstorage.exceptions.QuizTelegramClientException;
import org.quizstorage.utils.UserResolvable;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
@Slf4j
public class MessageHandlingExceptionWrapper {

    @Pointcut("within(@org.quizstorage.annotations.WrapUserResolvableException *) && execution(public * *(..))")
    public void annotatedClass() {}

    @Pointcut("@annotation(org.quizstorage.annotations.WrapUserResolvableException)")
    public void annotatedMethod() {}

    @Around(value = "annotatedClass() || annotatedMethod()")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.warn("Wrapping an exception {}", e.getClass().getName());
            return handleException(joinPoint, e);
        }
    }

    private Object handleException(ProceedingJoinPoint joinPoint, Throwable e) throws Throwable {
        if (e instanceof UserResolvable) {
            log.warn("The error is user resolvable by itself, throwing as is");
            throw e;
        }
        Optional<UserResolvable> userResolvable = getUserResolvable(joinPoint.getArgs());
        if (userResolvable.isPresent()) {
            return wrapAndThrow(userResolvable.get(), e);
        } else {
            log.warn("The error isn't user resolvable, throwing as is");
            throw e;
        }
    }

    private Object wrapAndThrow(UserResolvable resolvable, Throwable e) {
        log.warn("The error is user resolvable, wrapping and throwing");
        if (e instanceof QuizTelegramClientException qtException) {
            throw new MessageHandlingException(resolvable.getUserId(), qtException);
        } else if (e instanceof MessageSourceResolvable msgResolvable){
            throw new MessageHandlingException(resolvable.getUserId(), e, msgResolvable);
        } else {
            throw new MessageHandlingException(resolvable.getUserId(), e, ErrorMessage.UNKNOWN_ERROR);
        }
    }


    private Optional<UserResolvable> getUserResolvable(Object[] args) {
        if (args == null) {
            return Optional.empty();
        }
        return Arrays.stream(args)
                .filter(arg -> arg instanceof UserResolvable)
                .map(arg -> (UserResolvable) arg)
                .findFirst();
    }

}
