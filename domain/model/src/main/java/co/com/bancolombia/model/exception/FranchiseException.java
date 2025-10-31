package co.com.bancolombia.model.exception;


import co.com.bancolombia.model.enums.IExceptionMessage;

public class FranchiseException extends ApiException {
    public FranchiseException(IExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public FranchiseException(IExceptionMessage exceptionMessage, String param) {
        super(exceptionMessage, param);
    }
}
