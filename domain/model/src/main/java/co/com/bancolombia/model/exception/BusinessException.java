package co.com.bancolombia.model.exception;

import co.com.bancolombia.model.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getMessage(), technicalMessage);
    }

    public BusinessException(TechnicalMessage technicalMessage, String param) {
        super(String.format(technicalMessage.getMessage(), param), technicalMessage);
    }
}

