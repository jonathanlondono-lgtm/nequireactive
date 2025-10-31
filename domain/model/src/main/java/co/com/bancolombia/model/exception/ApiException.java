package co.com.bancolombia.model.exception;


import co.com.bancolombia.model.enums.IExceptionMessage;

public class ApiException extends RuntimeException {
    private final IExceptionMessage exceptionMessage;
    private final String param;

    public ApiException(IExceptionMessage exceptionMessage) {
        super(exceptionMessage != null ? exceptionMessage.getMessage() : null);
        this.exceptionMessage = exceptionMessage;
        this.param = null;
    }

    public ApiException(IExceptionMessage exceptionMessage, String param) {
        super(exceptionMessage != null ? String.format(exceptionMessage.getMessage(), param) : null);
        this.exceptionMessage = exceptionMessage;
        this.param = param;
    }

    public IExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }

    public String getParam() {
        return param;
    }
}

