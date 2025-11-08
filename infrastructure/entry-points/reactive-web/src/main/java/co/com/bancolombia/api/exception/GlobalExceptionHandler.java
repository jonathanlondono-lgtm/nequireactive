package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.dto.response.ErrorResponse;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                   ApplicationContext applicationContext,
                                   ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        String path = request.uri().getPath();

        log.error("Error handling request to path: {} - Error: {}", path, error.getMessage(), error);

        if (error instanceof BusinessException businessException) {
            return handleBusinessException(businessException, path);
        }

        if (error instanceof TechnicalException technicalException) {
            return handleTechnicalException(technicalException, path);
        }

        return handleGenericException(error, path);
    }

    private Mono<ServerResponse> handleBusinessException(BusinessException exception, String path) {
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(exception.getTechnicalMessage().getCode()));

        ErrorResponse errorResponse = ErrorResponse.of(
                status.value(),
                exception.getTechnicalMessage().getCode(),
                exception.getMessage(),
                path
        );

        log.warn("Business exception occurred: {} - Status: {} - Code: {}",
                exception.getMessage(), status, exception.getTechnicalMessage().getCode());

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }

    private Mono<ServerResponse> handleTechnicalException(TechnicalException exception, String path) {
        HttpStatus status = HttpStatus.valueOf(Integer.parseInt(exception.getTechnicalMessage().getCode()));

        ErrorResponse errorResponse = ErrorResponse.of(
                status.value(),
                exception.getTechnicalMessage().getCode(),
                exception.getMessage(),
                path
        );

        log.error("Technical exception occurred: {} - Status: {} - Code: {}",
                exception.getMessage(), status, exception.getTechnicalMessage().getCode(), exception);

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }

    private Mono<ServerResponse> handleGenericException(Throwable error, String path) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "500",
                "An unexpected error occurred: " + error.getMessage(),
                path
        );

        log.error("Unexpected exception occurred: {}", error.getMessage(), error);

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }
}
