package co.com.bancolombia.model.exception;


import co.com.bancolombia.model.enums.IExceptionMessage;

public class BranchException extends ApiException {
    public BranchException(IExceptionMessage exceptionMessage) {
        super(exceptionMessage);
    }

    public BranchException(IExceptionMessage exceptionMessage, String param) {
        super(exceptionMessage, param);
    }
}
