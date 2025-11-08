package co.com.bancolombia.model.exception;

import co.com.bancolombia.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends ProcessorException {

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage) {
        super(cause, technicalMessage);
    }

    public TechnicalException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }

    public TechnicalException(Throwable cause, TechnicalMessage technicalMessage, String param) {
        super(String.format(technicalMessage.getMessage(), param), technicalMessage);
    }
}

