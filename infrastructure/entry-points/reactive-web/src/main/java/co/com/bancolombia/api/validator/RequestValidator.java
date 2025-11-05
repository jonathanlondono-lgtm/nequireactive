package co.com.bancolombia.api.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.validation.BeanPropertyBindingResult;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (violations.isEmpty()) {
            return Mono.just(object);
        }

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(object, object.getClass().getName());
        violations.forEach(violation ->
            bindingResult.rejectValue(
                violation.getPropertyPath().toString(),
                violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                violation.getMessage()
            )
        );

        return Mono.error(new WebExchangeBindException(null, bindingResult));
    }
}

