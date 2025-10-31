package co.com.bancolombia.model.exception;


import co.com.bancolombia.model.enums.IExceptionMessage;

public class ProductException extends ApiException {
    public ProductException(IExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public ProductException(IExceptionMessage exceptionMessage, String param) {
        super(exceptionMessage, param);
    }
}