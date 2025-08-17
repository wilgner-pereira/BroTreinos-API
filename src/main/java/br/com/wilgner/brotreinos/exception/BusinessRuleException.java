package br.com.wilgner.brotreinos.exception;

public class BusinessRuleException extends RuntimeException {
    private final ErrorCode errorCode;
    public BusinessRuleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
