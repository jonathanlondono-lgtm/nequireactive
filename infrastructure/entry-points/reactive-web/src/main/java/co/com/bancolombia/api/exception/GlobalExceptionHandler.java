package co.com.bancolombia.api.exception;

import co.com.bancolombia.api.dto.response.ErrorResponse;
import co.com.bancolombia.model.exception.ApiException;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.exception.ProductException;
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
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                   WebProperties.Resources resources,
                                   ApplicationContext applicationContext,
                                   ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources, applicationContext);
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

        if (error instanceof ApiException apiException) {
            return handleDomainException(apiException, path);
        }

        if (error instanceof ServerWebInputException inputException) {
            return handleValidationException(inputException, path);
        }

        if (error instanceof IllegalArgumentException illegalArgException) {
            return handleIllegalArgumentException(illegalArgException, path);
        }

        return handleGenericException(error, path);
    }

    private Mono<ServerResponse> handleDomainException(ApiException exception, String path) {
        HttpStatus status = determineDomainExceptionStatus(exception);

        ErrorResponse errorResponse = ErrorResponse.of(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                path
        );

        log.warn("Domain exception occurred: {} - Status: {}", exception.getMessage(), status);

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }

    private HttpStatus determineDomainExceptionStatus(ApiException exception) {
        if (exception.getMessage() != null && exception.getMessage().contains("not found")) {
            return HttpStatus.NOT_FOUND;
        }

        if (exception instanceof ProductException ||
            exception instanceof BranchException ||
            exception instanceof FranchiseException) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private Mono<ServerResponse> handleValidationException(ServerWebInputException exception, String path) {
        String errorMessage = exception.getReason() != null ?
                exception.getReason() :
                "Validation failed";

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                path
        );

        log.warn("Validation exception occurred: {}", errorMessage);

        return ServerResponse
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }

    private Mono<ServerResponse> handleIllegalArgumentException(IllegalArgumentException exception, String path) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                path
        );

        log.warn("Illegal argument exception occurred: {}", exception.getMessage());

        return ServerResponse
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }

    private Mono<ServerResponse> handleGenericException(Throwable error, String path) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
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
